package backend.academy.flame_fractal.transformations;

import backend.academy.flame_fractal.domain.Point;

public class BubbleTransformation implements Transformation {
    @Override
    public Point apply(Point point) {
        double x = point.x();
        double y = point.y();

        double rSquared = x * x + y * y;
        double denominator = rSquared + 4;
        double scalar = 4.0 / denominator;
        double newX = scalar * x;
        double newY = scalar * y;

        return new Point(newX, newY);
    }
}
