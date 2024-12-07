package backend.academy.flame_fractal.transformations;

import backend.academy.flame_fractal.domain.Point;

public class WavesTransformation implements Transformation {
    private static final double B = 0.2;
    private static final double C = 0.3;
    private static final int E = 5;
    private static final int F = 4;

    @Override
    public Point apply(Point point) {

        double x = point.x();
        double y = point.y();

        double newX = x + B * Math.sin(y / (C * C));
        double newY = y + E * Math.sin(x / (F * F));

        return new Point(newX, newY);
    }

}
