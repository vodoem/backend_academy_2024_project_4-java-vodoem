package backend.academy.flame_fractal.processor;


public class LogarithmicGammaCorrectionProcessor extends MultiThreadedImageProcessor {
    private final double scaleFactor; // Коэффициент масштаба

    public LogarithmicGammaCorrectionProcessor(double scaleFactor) {
        if (scaleFactor <= 0) {
            throw new IllegalArgumentException("Коэффициент должен быть положительным");
        }
        this.scaleFactor = scaleFactor;
    }

    protected int correct(int color) {
        // Логарифмическое преобразование цвета
        return (int) (255 * Math.log(1 + scaleFactor * color) / Math.log(1 + scaleFactor * 255));
    }
}
