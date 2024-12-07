package backend.academy.flame_fractal.processor;


public class LogarithmicGammaCorrectionProcessor extends MultiThreadedImageProcessor {
    private static final double MAX_COLOR = 255;
    private final double scaleFactor; // Коэффициент масштаба

    public LogarithmicGammaCorrectionProcessor(double scaleFactor) {
        this.scaleFactor = scaleFactor;
    }

    protected int correct(int color) {
        // Логарифмическое преобразование цвета
        return (int) (MAX_COLOR * Math.log(1 + scaleFactor * color) / Math.log(1 + scaleFactor * MAX_COLOR));
    }
}
