package backend.academy.flame_fractal.transformations;

import backend.academy.flame_fractal.domain.Point;

//Сжимает точки в форме подковы, создавая сильные искажения.
public class HorseShoe implements Transformation {
    @Override
    public Point apply(Point point) {
        double r = Math.sqrt(point.x() * point.x() + point.y() * point.y());
        double x = (point.x() - point.y()) / r;
        double y = (point.x() + point.y()) / r;
        return new Point(x, y);
    }
}
