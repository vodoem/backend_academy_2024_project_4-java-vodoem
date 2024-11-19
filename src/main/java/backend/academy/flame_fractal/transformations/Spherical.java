package backend.academy.flame_fractal.transformations;

import backend.academy.flame_fractal.domain.Point;

//factor = 1/(x^2 + y^2)
public class Spherical implements Transformation {
    @Override
    public Point apply(Point point) {
        double r2 = point.x() * point.x() + point.y() * point.y();
        double factor = 1.0 / r2;
        return new Point(factor * point.x(), factor * point.y());
    }
}
