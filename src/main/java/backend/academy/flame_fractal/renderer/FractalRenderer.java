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

public class FractalRenderer {
    private final Rect world; // Область отображения фрактала
    private final int maxIterations; // Максимальное число итераций для алгоритма
    private final int symmetry; // Число симметрий
    private final List<Transformation> transformations; // Список трансформаций
    private final Map<Transformation, Color> transformationColors; // Цвета трансформаций

    public FractalRenderer(Rect world, int maxIterations, int symmetry, List<Transformation> transformations, Map<Transformation, Color> transformationColors) {
        if (maxIterations <= 0) {
            throw new IllegalArgumentException("Число итераций должно быть положительным");
        }
        if (symmetry < 0) {
            throw new IllegalArgumentException("Симметрия должна быть не отрицательной");
        }
        if (transformations.size() != transformationColors.size()) {
            throw new IllegalArgumentException("Каждой трансформации должен соответствовать цвет");
        }

        this.world = world;
        this.maxIterations = maxIterations;
        this.symmetry = symmetry;
        this.transformations = transformations;
        this.transformationColors = new HashMap<>(transformationColors);
    }

    public void render(FractalImage image, int samples, long seed) {
        Random rng = new Random(seed);
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

    private void applyChanges(FractalImage image, Point point, Transformation transformation) {
        if (world.contains(point)) {
            int canvasX = extension(image.width(), world.x(), world.x() + world.width(), point.x());
            int canvasY = extension(image.height(), world.y(), world.y() + world.height(), point.y());

            if (image.contains(canvasX, canvasY)) {
                updatePixel(image, canvasX, canvasY, transformation);
            }
        }
    }

    private void updatePixel(FractalImage image, int x, int y, Transformation transformation) {
        Pixel oldPixel = image.pixel(x, y);
        Color color = transformationColors.get(transformation);
        Pixel newPixel = oldPixel.mixColor(color.red(), color.green(), color.blue());
        image.updatePixel(x, y, newPixel);
    }

    private Point randomPointInViewPort(Random rng) {
        double x = world.x() + rng.nextDouble() * world.width();
        double y = world.y() + rng.nextDouble() * world.height();
        return new Point(x, y);
    }

    private Transformation randomTransformation(Random rng) {
        return transformations.get(rng.nextInt(transformations.size()));
    }

    private int extension(int size, double min, double max, double point) {
        return size - (int) Math.ceil((max - point) / (max - min) * size);
    }

    private Point rotate(Point point, double angle) {
        double cosTheta = Math.cos(angle);
        double sinTheta = Math.sin(angle);
        double x = point.x() * cosTheta - point.y() * sinTheta;
        double y = point.x() * sinTheta + point.y() * cosTheta;
        return new Point(x, y);
    }
}
