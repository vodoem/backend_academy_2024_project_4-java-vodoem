package backend.academy.flame_fractal.renderer;

import backend.academy.flame_fractal.domain.Color;
import backend.academy.flame_fractal.domain.FractalImage;
import backend.academy.flame_fractal.domain.Pixel;
import backend.academy.flame_fractal.domain.Point;
import backend.academy.flame_fractal.domain.Rect;
import backend.academy.flame_fractal.transformations.Transformation;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public abstract class AbstractFractalRenderer implements FractalRenderer {
    protected final Rect world;
    protected final int maxIterations;
    protected final int symmetry;
    protected final List<Transformation> transformations;
    protected final Map<Transformation, Color> transformationColors;

    public AbstractFractalRenderer(
        Rect world,
        int maxIterations,
        int symmetry,
        List<Transformation> transformations,
        Map<Transformation, Color> transformationColors
    ) {
        this.world = world;
        this.maxIterations = maxIterations;
        this.symmetry = symmetry;
        this.transformations = transformations;
        this.transformationColors = new HashMap<>(transformationColors);
    }

    protected void renderSamples(FractalImage image, int samples, Random rng) {
        for (int num = 0; num < samples; num++) {
            Point point = randomPointInViewPort(rng);
            for (int iter = 0; iter < maxIterations; iter++) {
                Transformation transformation = randomTransformation(rng);
                point = transformation.apply(point);
                if (symmetry > 0) {
                    for (int s = 0; s < symmetry; s++) {
                        Point symPoint = rotate(point, s * 2 * Math.PI / symmetry);
                        applyChanges(image, symPoint, transformation);
                    }
                } else {
                    applyChanges(image, point, transformation);
                }
            }
        }
    }

    protected Point randomPointInViewPort(Random rng) {
        double x = world.x() + rng.nextDouble() * world.width();
        double y = world.y() + rng.nextDouble() * world.height();
        return new Point(x, y);
    }

    protected Transformation randomTransformation(Random rng) {
        return transformations.get(rng.nextInt(transformations.size()));
    }

    public abstract void render(FractalImage image, int samples, long seed);

    protected void applyChanges(FractalImage image, Point point, Transformation transformation) {
        if (world.contains(point)) {
            int canvasX = extension(image.width(), world.x(), world.x() + world.width(), point.x());
            int canvasY = extension(image.height(), world.y(), world.y() + world.height(), point.y());

            if (image.contains(canvasX, canvasY)) {
                updatePixel(image, canvasX, canvasY, transformation);
            }
        }
    }

    protected void updatePixel(FractalImage image, int x, int y, Transformation transformation) {
        Pixel oldPixel = image.pixel(x, y);
        Color color = transformationColors.get(transformation);
        Pixel newPixel = oldPixel.mixColor(color.red(), color.green(), color.blue());
        image.updatePixel(x, y, newPixel);
    }

    protected int extension(int size, double min, double max, double point) {
        return size - (int) Math.ceil((max - point) / (max - min) * size);
    }

    protected Point rotate(Point point, double angle) {
        double cosTheta = Math.cos(angle);
        double sinTheta = Math.sin(angle);
        double x = point.x() * cosTheta - point.y() * sinTheta;
        double y = point.x() * sinTheta + point.y() * cosTheta;
        return new Point(x, y);
    }
}
