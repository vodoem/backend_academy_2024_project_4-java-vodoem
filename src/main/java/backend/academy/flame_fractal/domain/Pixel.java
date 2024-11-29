package backend.academy.flame_fractal.domain;

public record Pixel(int r, int g, int b, int hitCount) {
    public Pixel {
        if ((r < 0 || r > 255) || (g < 0 || g > 255) || (b < 0 || b > 255)) {
            System.out.println(r + " " + g + " " + b + " " + hitCount);
            throw new IllegalArgumentException("RGB значения должны быть в пределах [0, 255]");
        }
        if (hitCount < 0) {
            throw new IllegalArgumentException("hitCount должен быть неотрицательным");
        }
    }
    public Pixel mixColor(int red, int green, int blue) {
        int newR = Math.min(255, Math.max(0, (this.r * this.hitCount + red) / (this.hitCount + 1)));
        int newG = Math.min(255, Math.max(0, (this.g * this.hitCount + green) / (this.hitCount + 1)));
        int newB = Math.min(255, Math.max(0, (this.b * this.hitCount + blue) / (this.hitCount + 1)));
        return new Pixel(newR, newG, newB, this.hitCount + 1);
    }
}

