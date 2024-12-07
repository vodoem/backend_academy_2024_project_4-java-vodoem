package backend.academy.flame_fractal.renderer;

import backend.academy.flame_fractal.domain.Color;
import backend.academy.flame_fractal.domain.FractalImage;
import backend.academy.flame_fractal.domain.Rect;
import backend.academy.flame_fractal.transformations.Transformation;
import java.io.PrintStream;
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

public class MultiThreadedFractalRenderer extends AbstractFractalRenderer {
    private static final Logger LOGGER = Logger.getLogger(MultiThreadedFractalRenderer.class.getName());
    private static final PrintStream PRINT_STREAM = System.out;
    private static final double BILLION = 1e9;
    private static final ThreadLocal<ThreadLocalRandom> RNG = ThreadLocal.withInitial(ThreadLocalRandom::current);
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
        super(world, maxIterations, symmetry, transformations, transformationColors);
        this.threadCount = threadCount <= 0 ? 1 : threadCount;
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

        int samplesPerThread = samples / threadCount;
        List<Future<?>> futures = new ArrayList<>(threadCount);

        for (int i = 0; i < threadCount; i++) {
            int threadSamples = (i == threadCount - 1) ? samplesPerThread + samples % threadCount
                : samplesPerThread;
            futures.add(executor.submit(() -> renderSamples(image, threadSamples, RNG.get())));
        }

        for (Future<?> future : futures) {
            try {
                future.get();
            } catch (InterruptedException | ExecutionException e) {
                LOGGER.log(Level.SEVERE, "Ошибка во время выполнения потока", e);
            }
        }

        executor.shutdown();

        long endTime = System.nanoTime();
        PRINT_STREAM.printf("Многопоточный рендеринг завершен: %d выборок, %d потоков, время: %.2f секунд%n",
            samples, threadCount, (endTime - startTime) / BILLION);
    }


    @Override
    protected void updatePixel(FractalImage image, int x, int y, Transformation transformation) {
        ReentrantLock lock = pixelLocks[x][y];
        lock.lock();
        try {
            super.updatePixel(image, x, y, transformation);
        } finally {
            lock.unlock();
        }
    }
}
