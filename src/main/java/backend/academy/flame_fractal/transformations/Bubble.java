package backend.academy.flame_fractal.transformations;

import backend.academy.flame_fractal.domain.Point;

//Сжимает точки к центру, создавая эффект пузыря.
public class Bubble implements Transformation {
    @Override
    public Point apply(Point point) {
        double r2 = point.x() * point.x() + point.y() * point.y();
        double factor = 4.0 / (r2 + 4);
        double x = point.x() * factor;
        double y = point.y() * factor;
        return new Point(x, y);
    }
}
