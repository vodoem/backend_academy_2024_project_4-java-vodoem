package backend.academy.flame_fractal.processor;

import backend.academy.flame_fractal.domain.FractalImage;
import backend.academy.flame_fractal.domain.Pixel;

public class LogarithmicGammaCorrectionProcessor implements ImageProcessor {
    private final double scaleFactor; // Коэффициент масштаба

    public LogarithmicGammaCorrectionProcessor(double scaleFactor) {
        if (scaleFactor <= 0) {
            throw new IllegalArgumentException("Коэффициент должен быть положительным");
        }
        this.scaleFactor = scaleFactor;
    }

    @Override
    public void process(FractalImage image) {
        for (int y = 0; y < image.height(); y++) {
            for (int x = 0; x < image.width(); x++) {
                Pixel original = image.pixel(x, y);
                int r = correct(original.r());
                int g = correct(original.g());
                int b = correct(original.b());
                image.updatePixel(x, y, new Pixel(r, g, b, original.hitCount()));
            }
        }
    }

    private int correct(int color) {
        // Логарифмическое преобразование цвета
        return (int) (255 * Math.log(1 + scaleFactor * color) / Math.log(1 + scaleFactor * 255));
    }
}
