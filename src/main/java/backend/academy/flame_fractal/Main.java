package backend.academy.flame_fractal;

import backend.academy.flame_fractal.configurator.UserConfigurator;
import backend.academy.flame_fractal.domain.Color;
import backend.academy.flame_fractal.domain.FractalImage;
import backend.academy.flame_fractal.domain.Rect;
import backend.academy.flame_fractal.processor.ImageProcessor;
import backend.academy.flame_fractal.renderer.FractalRenderer;
import backend.academy.flame_fractal.renderer.MultiThreadedFractalRenderer;
import backend.academy.flame_fractal.renderer.SingleThreadedFractalRenderer;
import backend.academy.flame_fractal.transformations.BubbleTransformation;
import backend.academy.flame_fractal.transformations.HandkerchiefTransformation;
import backend.academy.flame_fractal.transformations.HeartTransformation;
import backend.academy.flame_fractal.transformations.HyperbolicTransformation;
import backend.academy.flame_fractal.transformations.JuliaScopeTransformation;
import backend.academy.flame_fractal.transformations.Transformation;
import backend.academy.flame_fractal.transformations.WavesTransformation;
import backend.academy.flame_fractal.utils.ImageFormat;
import backend.academy.flame_fractal.utils.ImageSize;
import backend.academy.flame_fractal.utils.ImageUtils;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import lombok.experimental.UtilityClass;

@UtilityClass
public class Main {
    private static final PrintStream PRINT_STREAM = System.out;


    public static void main(String[] args) throws IOException {

        UserConfigurator configurator = new UserConfigurator();

        int symmetry = configurator.configureSymmetry();
        long seed = configurator.configureSeed();
        int samples = configurator.configureSamples();
        short iterPerSample = configurator.configureIterations();

        List<Transformation> availableTransformations = List.of(
            new BubbleTransformation(),
            new HandkerchiefTransformation(),
            new HeartTransformation(),
            new HyperbolicTransformation(),
            new JuliaScopeTransformation(),
            new WavesTransformation()
        );

        List<Transformation> transformations = configurator.configureTransformations(availableTransformations);
        Map<Transformation, Color> colors = configurator.configureColors(transformations);

        ImageSize imageSize = configurator.configureImageSize();

        int imageWidth = imageSize.width();
        int imageHeight = imageSize.height();

        FractalImage canvas = FractalImage.create(imageWidth, imageHeight);

        Rect world = configurator.computeRect(imageWidth, imageHeight);

        ImageFormat format = configurator.configureImageFormat();

        int threads = configurator.configureThreads();

        ImageProcessor processor = configurator.configureImageProcessor();

        FractalRenderer renderer;
        if (threads == 1) {
            renderer = new SingleThreadedFractalRenderer(world, iterPerSample, symmetry, transformations, colors);

        } else {
            renderer =
                new MultiThreadedFractalRenderer(world, iterPerSample, symmetry, transformations, colors, threads);

        }

        renderer.render(canvas, samples, seed);

        if (processor != null) {
            processor.process(canvas);
        }

        Path outputDirectory = Paths.get("fractals");
        Path outputFile = outputDirectory.resolve("fractal_output.png");

        if (!Files.exists(outputDirectory)) {
            Files.createDirectories(outputDirectory);
        }

        // Сохранение изображения
        ImageUtils.save(canvas, outputFile, format);
        PRINT_STREAM.println("Изображение успешно сохранено: " + outputFile);
    }
}
