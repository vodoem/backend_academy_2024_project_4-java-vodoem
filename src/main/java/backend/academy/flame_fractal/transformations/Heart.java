package backend.academy.flame_fractal.transformations;

import backend.academy.flame_fractal.domain.Point;

//превращающая точки в форму сердца.
public class Heart implements Transformation {
    @Override
    public Point apply(Point point) {
        double r = Math.sqrt(point.x() * point.x() + point.y() * point.y());
        double factor = 1.0 / r;
        double x = factor * (point.x() * Math.sin(r) - point.y() * Math.cos(r));
        double y = factor * (point.x() * Math.cos(r) + point.y() * Math.sin(r));
        return new Point(x, y);
    }
}
