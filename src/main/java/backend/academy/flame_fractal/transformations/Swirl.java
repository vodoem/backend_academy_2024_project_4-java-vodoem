package backend.academy.flame_fractal.transformations;

import backend.academy.flame_fractal.domain.Point;

//Создает эффект закручивания точек вокруг центра.
public class Swirl implements Transformation {
    @Override
    public Point apply(Point point) {
        double r2 = point.x() * point.x() + point.y() * point.y();
        double sinR = Math.sin(r2);
        double cosR = Math.cos(r2);
        double x = point.x() * cosR - point.y() * sinR;
        double y = point.x() * sinR + point.y() * cosR;
        return new Point(x, y);
    }
}
