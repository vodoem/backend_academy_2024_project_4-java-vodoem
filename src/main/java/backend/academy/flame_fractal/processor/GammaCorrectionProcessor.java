package backend.academy.flame_fractal.processor;

import backend.academy.flame_fractal.domain.FractalImage;
import backend.academy.flame_fractal.domain.Pixel;

public class GammaCorrectionProcessor implements ImageProcessor {
    private final double gamma;

    public GammaCorrectionProcessor(double gamma) {
        if (gamma <= 0) {
            throw new IllegalArgumentException("Гамма должна быть положительной");
        }
        this.gamma = gamma;
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
        return (int) (255 * Math.pow(color / 255.0, gamma));
    }
}
