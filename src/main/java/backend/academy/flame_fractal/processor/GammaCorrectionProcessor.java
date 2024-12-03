package backend.academy.flame_fractal.processor;


public class GammaCorrectionProcessor extends MultiThreadedImageProcessor {
    private final double gamma;

    public GammaCorrectionProcessor(double gamma) {
        if (gamma <= 0) {
            throw new IllegalArgumentException("Гамма должна быть положительной");
        }
        this.gamma = gamma;
    }

    protected int correct(int color) {
        return (int) (255 * Math.pow(color / 255.0, gamma));
    }
}
