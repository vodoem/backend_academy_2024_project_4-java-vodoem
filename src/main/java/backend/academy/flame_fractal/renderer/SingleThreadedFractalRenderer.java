package backend.academy.flame_fractal.renderer;

import backend.academy.flame_fractal.domain.Color;
import backend.academy.flame_fractal.domain.FractalImage;
import backend.academy.flame_fractal.domain.Rect;
import backend.academy.flame_fractal.transformations.Transformation;
import java.io.PrintStream;
import java.nio.ByteBuffer;
import java.security.SecureRandom;
import java.util.List;
import java.util.Map;

public class SingleThreadedFractalRenderer extends AbstractFractalRenderer {
    private static final PrintStream PRINT_STREAM = System.out;
    private static final double BILLION = 1e9;

    public SingleThreadedFractalRenderer(
        Rect world,
        int maxIterations,
        int symmetry,
        List<Transformation> transformations,
        Map<Transformation, Color> transformationColors
    ) {
        super(world, maxIterations, symmetry, transformations, transformationColors);
    }

    @Override
    public void render(FractalImage image, int samples, long seed) {
        byte[] seedBytes = ByteBuffer.allocate(Long.BYTES).putLong(seed).array();
        SecureRandom rng = new SecureRandom(seedBytes);
        long startTime = System.nanoTime();
        renderSamples(image, samples, rng);
        long endTime = System.nanoTime();
        PRINT_STREAM.printf("Однопоточный рендеринг завершен: %d выборок, %d потоков, время: %.2f секунд%n",
            samples, 1, (endTime - startTime) / BILLION);
    }

}
