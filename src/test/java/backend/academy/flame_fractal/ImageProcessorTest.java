package backend.academy.flame_fractal;

import backend.academy.flame_fractal.domain.FractalImage;
import backend.academy.flame_fractal.processor.GammaCorrectionProcessor;
import backend.academy.flame_fractal.processor.ImageProcessor;
import backend.academy.flame_fractal.processor.LogarithmicGammaCorrectionProcessor;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatCode;

public class ImageProcessorTest {
    @Test
    void testGammaCorrectionProcessorDoesNotThrowErrors() {
        FractalImage image = FractalImage.create(1920, 1080);
        ImageProcessor gammaProcessor = new GammaCorrectionProcessor(2.2);

        assertThatCode(() -> gammaProcessor.process(image)).doesNotThrowAnyException();
    }

    @Test
    void testLogarithmicGammaCorrectionProcessorDoesNotThrowErrors() {
        FractalImage image = FractalImage.create(1920, 1080);
        ImageProcessor logGammaProcessor = new LogarithmicGammaCorrectionProcessor(1.0);

        assertThatCode(() -> logGammaProcessor.process(image)).doesNotThrowAnyException();
    }
}
