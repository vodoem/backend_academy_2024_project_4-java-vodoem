package backend.academy.flame_fractal.transformations;

import backend.academy.flame_fractal.domain.Point;

public class WavesTransformation implements Transformation {
    @Override
    public Point apply(Point point) {

        double x = point.x();
        double y = point.y();

        double b = 0.2;
        double e = 5;
        double c = 0.3;
        double f = 4;
        double newX = x + b * Math.sin(y / (c * c));
        double newY = y + e * Math.sin(x / (f * f));

        return new Point(newX, newY);
    }

}
