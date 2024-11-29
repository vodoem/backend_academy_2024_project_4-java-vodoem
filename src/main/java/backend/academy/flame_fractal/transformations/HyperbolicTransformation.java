package backend.academy.flame_fractal.transformations;

import backend.academy.flame_fractal.domain.Point;

public class HyperbolicTransformation implements Transformation {
    @Override
    public Point apply(Point point) {
        double x = point.x();
        double y = point.y();

        double r = Math.sqrt(x * x + y * y);

        double theta = Math.atan2(y, x);

        double newX = Math.sin(theta) / r;
        double newY = r * Math.cos(theta);

        return new Point(newX, newY);
    }
}
