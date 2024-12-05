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
import static org.assertj.core.api.AssertionsForClassTypes.assertThatCode;

public class FractalRendererTest {
    @Test
    void testSingleThreadedRenderingDoesNotThrowErrors() {
        FractalImage image = FractalImage.create(1000, 1000);
        Transformation transformation = new BubbleTransformation();
        FractalRenderer renderer = new SingleThreadedFractalRenderer(
            new Rect(-1.0, -1.0, 2.0, 2.0),
            100,
            1,
            List.of(transformation),
            Map.of(transformation, new Color(0, 0, 0))
        );

        assertThatCode(() -> renderer.render(image, 100, 42))
            .doesNotThrowAnyException();
    }

    @Test
    void testMultiThreadedRenderingDoesNotThrowErrors() {
        FractalImage image = FractalImage.create(1000, 1000);
        FractalRenderer renderer = new MultiThreadedFractalRenderer(
            new Rect(-1.0, -1.0, 2.0, 2.0),
            100,
            1,
            List.of(new BubbleTransformation()),
            Map.of(new BubbleTransformation(), new Color(0, 0, 0)),
            5
        );

        assertThatCode(() -> renderer.render(image, 100, 42))
            .doesNotThrowAnyException();
    }
}
