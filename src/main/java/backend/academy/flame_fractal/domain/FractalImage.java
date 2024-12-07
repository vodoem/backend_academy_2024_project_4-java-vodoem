package backend.academy.flame_fractal.domain;

import java.util.Arrays;

public record FractalImage(Pixel[] data, int width, int height) {
    public static FractalImage create(int width, int height) {
        Pixel[] data = new Pixel[width * height];
        Arrays.fill(data, new Pixel(0, 0, 0, 0));
        return new FractalImage(data, width, height);
    }

    public boolean contains(int x, int y) {
        return x >= 0 && x < width && y >= 0 && y < height;
    }

    public Pixel pixel(int x, int y) {
        if (!contains(x, y)) {
            throw new IllegalArgumentException("Координаты выходят за пределы");
        }
        return data[y * width + x];
    }

    public void updatePixel(int x, int y, Pixel pixel) {
        if (!contains(x, y)) {
            throw new IllegalArgumentException("Координаты за пределами");
        }
        data[y * width + x] = pixel;
    }
}
