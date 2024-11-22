package backend.academy.flame_fractal.processor;

import backend.academy.flame_fractal.domain.FractalImage;

@FunctionalInterface
public interface ImageProcessor {
    void process(FractalImage image);
}
