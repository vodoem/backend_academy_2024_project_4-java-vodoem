package backend.academy.flame_fractal.transformations;

import backend.academy.flame_fractal.domain.Point;
import java.util.function.Function;

public interface Transformation extends Function<Point, Point> {
}
