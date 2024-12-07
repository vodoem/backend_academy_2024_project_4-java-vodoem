package backend.academy.flame_fractal.domain;

import java.io.PrintStream;

public record Pixel(int r, int g, int b, int hitCount) {
    private static final PrintStream PRINT_STREAM = System.out;
    private static final int MAX_COLOR = 255;

    public Pixel {
        if ((r < 0 || r > MAX_COLOR) || (g < 0 || g > MAX_COLOR) || (b < 0 || b > MAX_COLOR)) {
            PRINT_STREAM.println(r + " " + g + " " + b + " " + hitCount);
            throw new IllegalArgumentException("RGB значения должны быть в пределах [0, 255]");
        }
        if (hitCount < 0) {
            throw new IllegalArgumentException("hitCount должен быть неотрицательным");
        }
    }

    public Pixel mixColor(int red, int green, int blue) {
        int newR = Math.min(MAX_COLOR, Math.max(0, (this.r * this.hitCount + red) / (this.hitCount + 1)));
        int newG = Math.min(MAX_COLOR, Math.max(0, (this.g * this.hitCount + green) / (this.hitCount + 1)));
        int newB = Math.min(MAX_COLOR, Math.max(0, (this.b * this.hitCount + blue) / (this.hitCount + 1)));
        return new Pixel(newR, newG, newB, this.hitCount + 1);
    }
}

