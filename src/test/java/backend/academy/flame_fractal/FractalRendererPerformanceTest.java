package backend.academy.flame_fractal;

import backend.academy.flame_fractal.domain.Color;
import backend.academy.flame_fractal.domain.FractalImage;
import backend.academy.flame_fractal.domain.Rect;
import backend.academy.flame_fractal.renderer.FractalRenderer;
import backend.academy.flame_fractal.renderer.MultiThreadedFractalRenderer;
import backend.academy.flame_fractal.renderer.SingleThreadedFractalRenderer;
import backend.academy.flame_fractal.transformations.BubbleTransformation;
import backend.academy.flame_fractal.transformations.Transformation;
import org.junit.jupiter.api.Test;
import java.util.List;
import java.util.Map;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class FractalRendererPerformanceTest {
    @Test
    void testMultiThreadedRenderingIsFasterThanSingleThreaded() {
        FractalImage image = FractalImage.create(1000, 1000);
        Transformation transformation = new BubbleTransformation();
        Color color = new Color(0, 0, 0);
        FractalRenderer singleRenderer = new SingleThreadedFractalRenderer(
            new Rect(-1.0, -1.0, 2.0, 2.0),
            1000,
            1,
            List.of(transformation),
            Map.of(transformation, color)
        );

        FractalRenderer multiRenderer = new MultiThreadedFractalRenderer(
            new Rect(-1.0, -1.0, 2.0, 2.0),
            1000,
            1,
            List.of(transformation),
            Map.of(transformation, color),
            10
        );

        long singleThreadedTime = measureExecutionTime(() -> singleRenderer.render(image, 10000, 42));
        long multiThreadedTime = measureExecutionTime(() -> multiRenderer.render(image, 10000, 42));

        assertThat(multiThreadedTime).isLessThan(singleThreadedTime);
    }

    private long measureExecutionTime(Runnable task) {
        long start = System.nanoTime();
        task.run();
        return System.nanoTime() - start;
    }
}
