package backend.academy.flame_fractal;

import backend.academy.flame_fractal.domain.FractalImage;
import backend.academy.flame_fractal.processor.GammaCorrectionProcessor;
import backend.academy.flame_fractal.processor.ImageProcessor;
import backend.academy.flame_fractal.processor.LogarithmicGammaCorrectionProcessor;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class ImageProcessorOutputTest {
    @Test
    void testGammaCorrectionProcessorAltersImage() {
        FractalImage originalImage = FractalImage.create(1920, 1080);
        FractalImage processedImage = FractalImage.create(1920, 1080);
        ImageProcessor gammaProcessor = new GammaCorrectionProcessor(2.2);

        gammaProcessor.process(processedImage);

        assertThat(processedImage).isNotEqualTo(originalImage);
    }

    @Test
    void testLogarithmicGammaCorrectionProcessorAltersImage() {
        FractalImage originalImage = FractalImage.create(1920, 1080);
        FractalImage processedImage = FractalImage.create(1920, 1080);
        ImageProcessor logGammaProcessor = new LogarithmicGammaCorrectionProcessor(1.0);

        logGammaProcessor.process(processedImage);

        assertThat(processedImage).isNotEqualTo(originalImage);
    }
}
