package backend.academy.flame_fractal.transformations;

import backend.academy.flame_fractal.domain.Point;

//превращающая точки в форму сердца.
public class HeartTransformation implements Transformation {
    @Override
    public Point apply(Point point) {
        double x = point.x();
        double y = point.y();

        double r = Math.sqrt(x * x + y * y);

        double thetaR = Math.atan2(y, x);

        double newX = r * Math.sin(thetaR * r);
        double newY = -r * Math.cos(thetaR * r);

        return new Point(newX, newY);
    }
}
