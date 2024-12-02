package backend.academy.flame_fractal.renderer;

import backend.academy.flame_fractal.domain.FractalImage;

public interface FractalRenderer {
    void render(FractalImage image, int samples, long seed);
}
