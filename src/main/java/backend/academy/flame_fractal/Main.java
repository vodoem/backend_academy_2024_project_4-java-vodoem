package backend.academy.flame_fractal;

import backend.academy.flame_fractal.domain.Color;
import backend.academy.flame_fractal.domain.FractalImage;
import backend.academy.flame_fractal.domain.Rect;
import backend.academy.flame_fractal.processor.GammaCorrectionProcessor;
import backend.academy.flame_fractal.processor.ImageProcessor;
import backend.academy.flame_fractal.renderer.FractalRenderer;
import backend.academy.flame_fractal.renderer.MultiThreadedFractalRenderer;
import backend.academy.flame_fractal.renderer.SingleThreadedFractalRenderer;
import backend.academy.flame_fractal.transformations.BubbleTransformation;
import backend.academy.flame_fractal.transformations.JuliaScopeTransformation;
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
        int symmetry = 0; // Количество симметричных частей
        long seed = new Random().nextLong(); // Начальное значение для генератора случайных чисел

        // Прямоугольник, который будет использоваться для генерации фрактала
        Rect world = new Rect(-1.777, -1.0, 3.554, 2.0);

        // Список трансформаций, которые будут использоваться для генерации фрактала
        List<Transformation> transformations = List.of(


            new WavesTransformation(),
            new BubbleTransformation(),
            new JuliaScopeTransformation()


        );

        Map<Transformation, Color> colors = Map.of(
            //transformations.get(0), new Color(0, 49, 83),
            transformations.get(0), new Color(0, 84, 200),
            transformations.get(1), new Color(10, 243, 10),
            transformations.get(2), new Color(230, 77, 80)
//
//            transformations.get(2), new Color(124, 252, 0)
        );

        // Количество выборок и итераций на выборку
        int samples = 60001; // Количество выборок
        short iterPerSample = 1000; // Количество итераций на выборку

        // Создание пустого холста для изображения фрактала.
        FractalImage canvasForSinglethreadedRendering = FractalImage.create(1920, 1080);
        FractalImage canvasForMultithreadedRendering = FractalImage.create(1920, 1080);

        //многопоточная версия рендеринга
        FractalRenderer
            multiRenderer = new MultiThreadedFractalRenderer(world, iterPerSample, symmetry, transformations, colors, 20);

        multiRenderer.render(canvasForMultithreadedRendering, samples, seed);

        //однопоточная версия рендеринга
        FractalRenderer
            singleRenderer = new SingleThreadedFractalRenderer(world, iterPerSample, symmetry, transformations, colors);

        singleRenderer.render(canvasForSinglethreadedRendering, samples, seed);


        ImageProcessor processor = new GammaCorrectionProcessor(0.2);
        processor.process(canvasForMultithreadedRendering);


        ImageUtils.save(canvasForMultithreadedRendering, Path.of("fractals/fractal_output.png"), ImageFormat.PNG);
    }
}
