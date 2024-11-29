package backend.academy.flame_fractal;

import backend.academy.flame_fractal.domain.Color;
import backend.academy.flame_fractal.domain.FractalImage;
import backend.academy.flame_fractal.domain.Rect;
import backend.academy.flame_fractal.processor.LogarithmicGammaCorrectionProcessor;
import backend.academy.flame_fractal.renderer.FractalRenderer;
import backend.academy.flame_fractal.transformations.BubbleTransformation;
import backend.academy.flame_fractal.transformations.Transformation;
import backend.academy.flame_fractal.transformations.WavesTransformation;
import backend.academy.flame_fractal.utils.ImageFormat;
import backend.academy.flame_fractal.utils.ImageUtils;
import lombok.experimental.UtilityClass;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.Random;

@UtilityClass
public class Main {
    public static void main(String[] args) throws IOException {
        int symmetry = 7; // Количество симметричных частей
        long seed = new Random().nextLong(); // Начальное значение для генератора случайных чисел

        // Прямоугольник, который будет использоваться для генерации фрактала
        Rect world = new Rect(-1.777, -1.0, 3.554, 2.0);

        // Список трансформаций, которые будут использоваться для генерации фрактала
        List<Transformation> transformations = List.of(


            new WavesTransformation(),
            new BubbleTransformation()

        );

        Map<Transformation, Color> colors = Map.of(
            //transformations.get(0), new Color(0, 49, 83),
            transformations.get(0), new Color(0, 84, 31),
            transformations.get(1), new Color(161, 133, 148)
//
//            transformations.get(2), new Color(124, 252, 0)
        );

        // Количество выборок и итераций на выборку
        int samples = 80000; // Количество выборок
        short iterPerSample = 7000; // Количество итераций на выборку

        // Создание холста для изображения фрактала
        FractalImage canvas = FractalImage.create(1920, 1080);

        // Создание рендерера фрактала с параметрами симметрии
        FractalRenderer renderer = new FractalRenderer(world, iterPerSample, symmetry, transformations, colors);

        // Рендеринг фрактала
        renderer.render(canvas, samples, seed);

        LogarithmicGammaCorrectionProcessor processor = new LogarithmicGammaCorrectionProcessor(0.2);
        processor.process(canvas);

        // Сохранение или отображение результата
        // Например, сохраняем изображение на диск
        ImageUtils.save(canvas, Path.of("fractals/fractal_output.png"), ImageFormat.PNG);
    }
}
