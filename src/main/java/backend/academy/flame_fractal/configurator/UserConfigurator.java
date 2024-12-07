package backend.academy.flame_fractal.configurator;

import backend.academy.flame_fractal.domain.Color;
import backend.academy.flame_fractal.domain.Rect;
import backend.academy.flame_fractal.processor.GammaCorrectionProcessor;
import backend.academy.flame_fractal.processor.ImageProcessor;
import backend.academy.flame_fractal.processor.LogarithmicGammaCorrectionProcessor;
import backend.academy.flame_fractal.transformations.Transformation;
import backend.academy.flame_fractal.utils.ImageFormat;
import backend.academy.flame_fractal.utils.ImageSize;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class UserConfigurator {
    private static final PrintStream PRINT_STREAM = System.out;
    private static final int DEFAULT_ITERATIONS = 100;
    private static final int DEFAULT_SAMPLES = 50000;
    private static final int DEFAULT_IMAGE_WIDTH = 1920;
    private static final int DEFAULT_IMAGE_HEIGHT = 1080;
    private static final int NO_CORRECTION_CHOICE = 3;
    private static final String OUTPUT_FORMAT = "%d. %s%n";
    private static final SecureRandom RANDOM = new SecureRandom();

    private final Scanner scanner = new Scanner(System.in, StandardCharsets.UTF_8);

    public int configureSymmetry() {
        PRINT_STREAM.print("Введите количество симметричных частей (рекомендуется от 1 до 10): ");
        return Integer.parseInt(scanner.nextLine());
    }

    public long configureSeed() {
        PRINT_STREAM.print("Введите seed для генерации фрактала (или нажмите Enter для случайного): ");
        String input = scanner.nextLine();
        return input.isEmpty() ? RANDOM.nextLong() : Long.parseLong(input);
    }

    public int configureSamples() {
        PRINT_STREAM.print("Введите количество выборок (по умолчанию 50000): ");
        String input = scanner.nextLine();
        return input.isBlank() ? DEFAULT_SAMPLES : Integer.parseInt(input);
    }

    public short configureIterations() {
        PRINT_STREAM.print("Введите количество итераций на выборку (по умолчанию 100): ");
        String input = scanner.nextLine();
        return input.isBlank() ? DEFAULT_ITERATIONS : Short.parseShort(input);
    }

    public List<Transformation> configureTransformations(List<Transformation> availableTransformations) {
        PRINT_STREAM.println("Выберите трансформации для генерации фрактала:");
        for (int i = 0; i < availableTransformations.size(); i++) {
            PRINT_STREAM.printf(OUTPUT_FORMAT, i + 1, availableTransformations.get(i).getClass().getSimpleName());
        }
        PRINT_STREAM.println("Введите номера трансформаций через запятую (например, 1,5,6): ");
        String input = scanner.nextLine();

        List<Integer> chosenIndices = Arrays.stream(input.split(","))
            .map(String::trim)
            .map(Integer::parseInt)
            .toList();

        List<Transformation> chosenTransformations = new ArrayList<>();
        for (int index : chosenIndices) {
            if (index >= 1 && index <= availableTransformations.size()) {
                chosenTransformations.add(availableTransformations.get(index - 1));
            } else {
                PRINT_STREAM.printf(
                    "Предупреждение: Трансформация с номером %d пропущена, так как она недействительна.%n", index);
            }
        }

        if (chosenTransformations.isEmpty()) {
            throw new IllegalArgumentException("Должна быть выбрана хотя бы одна трансформация.");
        }
        return chosenTransformations;
    }

    public Map<Transformation, Color> configureColors(List<Transformation> transformations) {
        Map<Transformation, Color> colors = new HashMap<>();

        PRINT_STREAM.println("Выберите цвета для выбранных трансформаций (в формате R,G,B, например: 255,0,0):");
        for (Transformation transformation : transformations) {
            PRINT_STREAM.printf("Цвет для %s: ", transformation.getClass().getSimpleName());
            String input = scanner.nextLine();

            try {
                String[] rgb = input.split(",");
                int r = Integer.parseInt(rgb[0].trim());
                int g = Integer.parseInt(rgb[1].trim());
                int b = Integer.parseInt(rgb[2].trim());

                colors.put(transformation, new Color(r, g, b));
            } catch (Exception e) {
                throw new IllegalArgumentException(
                    "Ошибка ввода цвета. Убедитесь, что вы ввели значения в формате R,G,B.", e);
            }
        }

        return colors;
    }

    public ImageSize configureImageSize() {
        PRINT_STREAM.print("Введите ширину изображения (по умолчанию 1920): ");
        String input = scanner.nextLine();
        int width = input.isBlank() ? DEFAULT_IMAGE_WIDTH : Integer.parseInt(input);
        PRINT_STREAM.print("Введите высоту изображения (например, 1080): ");
        input = scanner.nextLine();
        int height = input.isBlank() ? DEFAULT_IMAGE_HEIGHT : Integer.parseInt(input);

        if (width <= 0 || height <= 0) {
            throw new IllegalArgumentException("Размеры изображения должны быть положительными.");
        }

        return new ImageSize(width, height);
    }

    public Rect computeRect(int width, int height) {
        double aspectRatio = (double) width / height;
        double x = -aspectRatio;
        double y = -1.0;
        double rectWidth = Math.abs(x + x);
        double rectHeight = Math.abs(y + y);
        return new Rect(x, y, rectWidth, rectHeight);
    }

    public int configureThreads() {
        PRINT_STREAM.print(
            "Введите количество потоков для рендеринга"
                + "(1 для одного потока, больше 1 для многопоточного рендеринга): ");
        int threads = Integer.parseInt(scanner.nextLine());

        if (threads < 1) {
            throw new IllegalArgumentException("Количество потоков должно быть не менее 1.");
        }

        return threads;
    }

    public ImageFormat configureImageFormat() {
        PRINT_STREAM.println("Выберите формат изображения:");
        for (ImageFormat format : ImageFormat.values()) {
            PRINT_STREAM.printf(OUTPUT_FORMAT, format.ordinal() + 1, format);
        }
        PRINT_STREAM.print("Введите номер формата (например, 1 для JPEG): ");
        int choice = Integer.parseInt(scanner.nextLine());

        if (choice < 1 || choice > ImageFormat.values().length) {
            throw new IllegalArgumentException("Неверный номер формата.");
        }

        return ImageFormat.values()[choice - 1];
    }

    public ImageProcessor configureImageProcessor() {
        PRINT_STREAM.println("Выберите корректор для изображения:");
        PRINT_STREAM.println("1. GammaCorrectionProcessor");
        PRINT_STREAM.println("2. LogarithmicGammaCorrectionProcessor");
        PRINT_STREAM.println("3. Без коррекции");
        PRINT_STREAM.print("Введите номер (1, 2 или 3): ");
        int choice = Integer.parseInt(scanner.nextLine());

        if (choice == NO_CORRECTION_CHOICE) {
            PRINT_STREAM.println("Корректор не выбран.");
            return null;
        }

        if (choice < 1 || choice > 2) {
            throw new IllegalArgumentException("Неверный выбор корректора.");
        }

        PRINT_STREAM.print("Введите значение scaleFactor (рекомендуется от 0.1 до 10): ");
        double scaleFactor = Double.parseDouble(scanner.nextLine());

        if (scaleFactor <= 0) {
            throw new IllegalArgumentException("scaleFactor должен быть положительным.");
        }

        if (choice == 1) {
            return new GammaCorrectionProcessor(scaleFactor);
        } else {
            return new LogarithmicGammaCorrectionProcessor(scaleFactor);
        }
    }
}
