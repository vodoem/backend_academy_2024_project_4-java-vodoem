package backend.academy.flame_fractal.transformations;

import backend.academy.flame_fractal.domain.Point;

//Создает эффект симметричного отражения точек, будто вы смотрите в калейдоскоп.
public class Kaleidoscope implements Transformation {
    @Override
    public Point apply(Point point) {

        double x = Math.sin(point.x()) * Math.abs(point.x());
        double y = Math.cos(point.y()) * Math.abs(point.y());

        if (x > 0 && y < 0) {
            x = -x;
        } else if (x < 0 && y > 0) {
            y = -y;
        }

        return new Point(x, y);
    }

}
