package backend.academy.flame_fractal.utils;

import backend.academy.flame_fractal.domain.FractalImage;
import backend.academy.flame_fractal.domain.Pixel;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Path;
import javax.imageio.ImageIO;

public class ImageUtils {
    private static final int SIXTEEN_BIT = 16;
    private static final int EIGHT_BIT = 8;

    private ImageUtils() {
    }

    public static void save(FractalImage image, Path filename, ImageFormat format) throws IOException {
        BufferedImage bufferedImage = createBufferedImage(image);
        String formatName = format.name().toLowerCase();
        if (!ImageIO.write(bufferedImage, formatName, filename.toFile())) {
            throw new IOException("Не удалось сохранить изображение в формате " + format);
        }
    }

    private static BufferedImage createBufferedImage(FractalImage image) {
        BufferedImage bufferedImage = new BufferedImage(image.width(), image.height(), BufferedImage.TYPE_INT_RGB);

        for (int y = 0; y < image.height(); y++) {
            for (int x = 0; x < image.width(); x++) {
                Pixel pixel = image.pixel(x, y);
                int rgb = (pixel.r() << SIXTEEN_BIT) | (pixel.g() << EIGHT_BIT)
                    | pixel.b(); // преобразование нашего rgb в 24-битное представление rgb при помощи побитового сдвига
                bufferedImage.setRGB(x, y, rgb);
            }
        }

        return bufferedImage;
    }
}
