package backend.academy.flame_fractal.processor;

import backend.academy.flame_fractal.domain.FractalImage;
import backend.academy.flame_fractal.domain.Pixel;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public abstract class MultiThreadedImageProcessor implements ImageProcessor {
    private static final int THREAD_COUNT = Runtime.getRuntime().availableProcessors();

    @Override
    public void process(FractalImage image) {
        ExecutorService executor = Executors.newFixedThreadPool(THREAD_COUNT);

        for (int y = 0; y < image.height(); y++) {
            int row = y; // Для использования в лямбде
            executor.submit(() -> processRow(image, row));
        }

        executor.shutdown();
        try {
            boolean terminated = executor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
            if (!terminated) {
                throw new RuntimeException("Обработка изображения не завершилась в ожидаемое время");
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Обработка изображения была прервана", e);
        }
    }

    private void processRow(FractalImage image, int y) {
        for (int x = 0; x < image.width(); x++) {
            Pixel original = image.pixel(x, y);
            int r = correct(original.r());
            int g = correct(original.g());
            int b = correct(original.b());
            image.updatePixel(x, y, new Pixel(r, g, b, original.hitCount()));
        }
    }

    protected abstract int correct(int color);
}

