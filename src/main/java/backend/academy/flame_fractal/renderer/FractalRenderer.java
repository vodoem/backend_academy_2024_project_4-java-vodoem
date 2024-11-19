package backend.academy.flame_fractal.renderer;

import backend.academy.flame_fractal.domain.FractalImage;
import backend.academy.flame_fractal.domain.Pixel;
import backend.academy.flame_fractal.domain.Point;
import backend.academy.flame_fractal.domain.Rect;
import backend.academy.flame_fractal.transformations.Transformation;

import java.util.List;
import java.util.Random;

public class FractalRenderer {
    private final int symmetry;
    private final long seed;
    private final int mixRed;
    private final int mixGreen;
    private final int mixBlue;

    public FractalRenderer(int symmetry, long seed, int mixRed, int mixGreen, int mixBlue) {
        this.symmetry = symmetry;
        this.seed = seed;
        this.mixRed = mixRed;
        this.mixGreen = mixGreen;
        this.mixBlue = mixBlue;
    }

    public FractalImage render(
        FractalImage canvas,
        Rect world,
        List<Transformation> variations,
        int samples,
        short iterPerSample
    ) {
        Random random = new Random(seed);

        for (int num = 0; num < samples; ++num) {
            Point pw = randomPointInRect(world, random);

            for (short step = 0; step < iterPerSample; ++step) {
                // Случайным образом выбираем трансформацию
                Transformation variation = randomTransformation(variations, random);

                // Применяем трансформацию к точке
                pw = variation.apply(pw);

                // Обрабатываем симметричные точки
                double theta2 = 0.0;
                for (int s = 0; s < symmetry; theta2 += Math.PI * 2 / symmetry, ++s) {
                    Point rotated = rotate(pw, theta2);

                    if (!world.contains(rotated)) {
                        continue;
                    }

                    Pixel pixel = mapToCanvas(world, rotated, canvas);
                    if (pixel == null) {
                        continue;
                    }

                    // Обрабатываем пиксель (лок на время работы и смешивание цвета)
                    pixel = pixel.mixColor(mixRed, mixGreen, mixBlue);
                    canvas.updatePixel((int) rotated.x(), (int) rotated.y(), pixel);
                }
            }
        }

        return canvas;
    }

    // Генерация случайной точки в прямоугольнике
    private Point randomPointInRect(Rect rect, Random random) {
        double x = rect.x() + random.nextDouble() * rect.width();
        double y = rect.y() + random.nextDouble() * rect.height();
        return new Point(x, y);
    }

    // Случайный выбор трансформации
    private Transformation randomTransformation(List<Transformation> variations, Random random) {
        return variations.get(random.nextInt(variations.size()));
    }

    // Поворот точки на угол
    private Point rotate(Point point, double theta) {
        double cosTheta = Math.cos(theta);
        double sinTheta = Math.sin(theta);
        double x = point.x() * cosTheta - point.y() * sinTheta;
        double y = point.x() * sinTheta + point.y() * cosTheta;
        return new Point(x, y);
    }

    // Масштабирование координат на холсте
    private Pixel mapToCanvas(Rect world, Point point, FractalImage canvas) {
        int x = mapX(world, point.x(), canvas.width());
        int y = mapY(world, point.y(), canvas.height());

        if (!canvas.contains(x, y)) {
            return null; // Если точка вне холста, возвращаем null
        }

        return canvas.pixel(x, y);
    }

    // Преобразование координаты X в координату холста
    private int mapX(Rect world, double x, int width) {
        return (int) ((x - world.x()) / world.width() * (width - 1));
    }

    // Преобразование координаты Y в координату холста
    private int mapY(Rect world, double y, int height) {
        return (int) ((y - world.y()) / world.height() * (height - 1));
    }
}
