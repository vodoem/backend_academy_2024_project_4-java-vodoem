package backend.academy.flame_fractal.renderer;

import backend.academy.flame_fractal.domain.Color;
import backend.academy.flame_fractal.domain.FractalImage;
import backend.academy.flame_fractal.domain.Pixel;
import backend.academy.flame_fractal.domain.Point;
import backend.academy.flame_fractal.domain.Rect;
import backend.academy.flame_fractal.transformations.Transformation;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MultiThreadedFractalRenderer implements FractalRenderer {
    private static final Logger logger = Logger.getLogger(MultiThreadedFractalRenderer.class.getName());
    private final Rect world;
    private final int maxIterations;
    private final int symmetry;
    private final List<Transformation> transformations;
    private final Map<Transformation, Color> transformationColors;
    private final int threadCount;
    private ReentrantLock[][] pixelLocks;

    public MultiThreadedFractalRenderer(
        Rect world,
        int maxIterations,
        int symmetry,
        List<Transformation> transformations,
        Map<Transformation, Color> transformationColors,
        int threadCount
    ) {
        if (threadCount <= 0) {
            throw new IllegalArgumentException("Количество потоков должно быть больше 0");
        }
        this.world = world;
        this.maxIterations = maxIterations;
        this.symmetry = symmetry;
        this.transformations = transformations;
        this.transformationColors = transformationColors;
        this.threadCount = threadCount;
    }

    @Override
    public void render(FractalImage image, int samples, long seed) {
        ExecutorService executor = Executors.newFixedThreadPool(threadCount);
        long startTime = System.nanoTime();

        int imageWidth = image.width();
        int imageHeight = image.height();
        pixelLocks = new ReentrantLock[imageWidth][imageHeight];
        for (int x = 0; x < imageWidth; x++) {
            for (int y = 0; y < imageHeight; y++) {
                pixelLocks[x][y] = new ReentrantLock();
            }
        }

        // Деление выборок на потоки
        int samplesPerThread = samples / threadCount;
        List<Future<?>> futures = new ArrayList<>();

        for (int i = 0; i < threadCount; i++) {
            int threadSamples = (i == threadCount - 1) ? samplesPerThread + samples % threadCount :
                samplesPerThread; //последний созданный поток будет выполнять немного больше операций
            futures.add(executor.submit(() -> renderSamples(image, threadSamples)));
        }

        // Ожидание завершения потоков
        for (Future<?> future : futures) {
            try {
                future.get();
            } catch (InterruptedException | ExecutionException e) {
                logger.log(Level.SEVERE, "Ошибка во время выполнения потока", e);
            }
        }

        executor.shutdown();

        long endTime = System.nanoTime();
        System.out.printf("Многопоточный рендеринг завершен: %d выборок, %d потоков, время: %.2f секунд%n",
            samples, threadCount, (endTime - startTime) / 1e9);
    }

    private void renderSamples(FractalImage image, int samples) {
        ThreadLocalRandom rng = ThreadLocalRandom.current();
        for (int num = 0; num < samples; num++) {
            Point point = randomPointInViewPort(rng);
            for (int iter = 0; iter < maxIterations; iter++) {
                Transformation transformation = randomTransformation(rng);
                point = transformation.apply(point);
                if (symmetry > 0) {
                    for (int s = 0; s < symmetry; s++) {
                        Point symPoint = rotate(point, s * 2 * Math.PI / symmetry);
                        applyChanges(image, symPoint, transformation);
                    }
                } else {
                    applyChanges(image, point, transformation);
                }
            }
        }
    }

    private Point randomPointInViewPort(ThreadLocalRandom rng) {
        double x = world.x() + rng.nextDouble() * world.width();
        double y = world.y() + rng.nextDouble() * world.height();
        return new Point(x, y);
    }

    private Transformation randomTransformation(ThreadLocalRandom rng) {
        return transformations.get(rng.nextInt(transformations.size()));
    }

    private void applyChanges(FractalImage image, Point point, Transformation transformation) {
        if (world.contains(point)) {
            int canvasX = extension(image.width(), world.x(), world.x() + world.width(), point.x());
            int canvasY = extension(image.height(), world.y(), world.y() + world.height(), point.y());

            if (image.contains(canvasX, canvasY)) {
                updatePixel(image, canvasX, canvasY, transformation);
            }
        }
    }

    private void updatePixel(FractalImage image, int x, int y, Transformation transformation) {
        ReentrantLock lock = pixelLocks[x][y];
        lock.lock();
        try {
            Pixel oldPixel = image.pixel(x, y);
            Color color = transformationColors.get(transformation);
            Pixel newPixel = oldPixel.mixColor(color.red(), color.green(), color.blue());
            image.updatePixel(x, y, newPixel);
        } finally {
            lock.unlock();
        }
    }

    private int extension(int size, double min, double max, double point) {
        return size - (int) Math.ceil((max - point) / (max - min) * size);
    }

    private Point rotate(Point point, double angle) {
        double cosTheta = Math.cos(angle);
        double sinTheta = Math.sin(angle);
        double x = point.x() * cosTheta - point.y() * sinTheta;
        double y = point.x() * sinTheta + point.y() * cosTheta;
        return new Point(x, y);
    }
}
