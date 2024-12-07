package backend.academy.flame_fractal.processor;


public class GammaCorrectionProcessor extends MultiThreadedImageProcessor {
    private static final double MAX_COLOR = 255;
    private final double gamma;

    public GammaCorrectionProcessor(double gamma) {
        this.gamma = gamma;
    }

    protected int correct(int color) {
        return (int) (MAX_COLOR * Math.pow(color / MAX_COLOR, gamma));
    }
}
