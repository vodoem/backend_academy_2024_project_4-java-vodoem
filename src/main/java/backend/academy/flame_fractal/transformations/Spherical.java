package backend.academy.flame_fractal.transformations;

import backend.academy.flame_fractal.domain.Point;

//деформирует пространство, создавая эффект, который визуально напоминает проекцию на сферу
public class Spherical implements Transformation {
    @Override
    public Point apply(Point point) {
        double r2 = point.x() * point.x() + point.y() * point.y();
        double factor = 1.0 / r2;
        return new Point(factor * point.x(), factor * point.y());
    }
}
