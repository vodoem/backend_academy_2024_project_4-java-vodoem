package backend.academy.flame_fractal.transformations;

import backend.academy.flame_fractal.domain.Point;
import java.security.SecureRandom;

public class JuliaScopeTransformation implements Transformation {
    private static final double JULIA_SCOPE_POWER = 10.0;
    private static final double JULIA_SCOPE_DIST = 5.0;
    private static final ThreadLocal<SecureRandom> THREAD_LOCAL_RANDOM = ThreadLocal.withInitial(SecureRandom::new);

    @Override
    public Point apply(Point point) {
        double x = point.x();
        double y = point.y();

        double r = Math.sqrt(x * x + y * y);
        double phi = Math.atan2(y, x); // Angle in radians

        SecureRandom random = THREAD_LOCAL_RANDOM.get();
        double p3 = Math.floor(Math.abs(JULIA_SCOPE_POWER) * random.nextDouble());

        double t = (random.nextDouble(-1, 1) * phi + 2 * Math.PI * p3) / JULIA_SCOPE_POWER;

        double newX = Math.pow(r, JULIA_SCOPE_DIST / JULIA_SCOPE_POWER) * Math.cos(t);
        double newY = Math.pow(r, JULIA_SCOPE_DIST / JULIA_SCOPE_POWER) * Math.sin(t);

        return new Point(newX, newY);
    }
}
