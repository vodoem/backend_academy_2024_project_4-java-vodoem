package backend.academy.flame_fractal.configurator;

import backend.academy.flame_fractal.domain.Color;
import backend.academy.flame_fractal.domain.Rect;
import backend.academy.flame_fractal.processor.GammaCorrectionProcessor;
import backend.academy.flame_fractal.processor.ImageProcessor;
import backend.academy.flame_fractal.processor.LogarithmicGammaCorrectionProcessor;
import backend.academy.flame_fractal.transformations.Transformation;
import backend.academy.flame_fractal.utils.ImageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;

public class UserConfigurator {
    private final Scanner scanner = new Scanner(System.in);

    public int configureSymmetry() {
        System.out.print("Введите количество симметричных частей (рекомендуется от 1 до 10): ");
        return Integer.parseInt(scanner.nextLine());
    }

    public long configureSeed() {
        System.out.print("Введите seed для генерации фрактала (или нажмите Enter для случайного): ");
        String input = scanner.nextLine();
        return input.isEmpty() ? new Random().nextLong() : Long.parseLong(input);
    }

    public int configureSamples() {
        System.out.print("Введите количество выборок (по умолчанию 50000): ");
        String input = scanner.nextLine();
        return input.isBlank() ? 50000 : Integer.parseInt(input);
    }

    public short configureIterations() {
        System.out.print("Введите количество итераций на выборку (по умолчанию 100): ");
        String input = scanner.nextLine();
        return input.isBlank() ? 100 : Short.parseShort(input);
    }

    public List<Transformation> configureTransformations(List<Transformation> availableTransformations) {
        System.out.println("Выберите трансформации для генерации фрактала:");
        for (int i = 0; i < availableTransformations.size(); i++) {
            System.out.printf("%d. %s%n", i + 1, availableTransformations.get(i).getClass().getSimpleName());
        }
        System.out.println("Введите номера трансформаций через запятую (например, 1,5,6): ");
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
                System.out.printf("Предупреждение: Трансформация с номером %d пропущена, так как она недействительна.%n", index);
            }
        }

        if (chosenTransformations.isEmpty()) {
            throw new IllegalArgumentException("Должна быть выбрана хотя бы одна трансформация.");
        }
        return chosenTransformations;
    }

    public Map<Transformation, Color> configureColors(List<Transformation> transformations) {
        Map<Transformation, Color> colors = new HashMap<>();

        System.out.println("Выберите цвета для выбранных трансформаций (в формате R,G,B, например: 255,0,0):");
        for (Transformation transformation : transformations) {
            System.out.printf("Цвет для %s: ", transformation.getClass().getSimpleName());
            String input = scanner.nextLine();

            try {
                String[] rgb = input.split(",");
                int r = Integer.parseInt(rgb[0].trim());
                int g = Integer.parseInt(rgb[1].trim());
                int b = Integer.parseInt(rgb[2].trim());

                colors.put(transformation, new Color(r, g, b));
            } catch (Exception e) {
                throw new IllegalArgumentException("Ошибка ввода цвета. Убедитесь, что вы ввели значения в формате R,G,B.", e);
            }
        }

        return colors;
    }

    public int[] configureImageSize() {
        System.out.print("Введите ширину изображения (например, 1920): ");
        int width = Integer.parseInt(scanner.nextLine());
        System.out.print("Введите высоту изображения (например, 1080): ");
        int height = Integer.parseInt(scanner.nextLine());

        if (width <= 0 || height <= 0) {
            throw new IllegalArgumentException("Размеры изображения должны быть положительными.");
        }

        return new int[]{width, height};
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
        System.out.print("Введите количество потоков для рендеринга (1 для одного потока, больше 1 для многопоточного рендеринга): ");
        int threads = Integer.parseInt(scanner.nextLine());

        if (threads < 1) {
            throw new IllegalArgumentException("Количество потоков должно быть не менее 1.");
        }

        return threads;
    }

    public ImageFormat configureImageFormat() {
        System.out.println("Выберите формат изображения:");
        for (ImageFormat format : ImageFormat.values()) {
            System.out.printf("%d. %s%n", format.ordinal() + 1, format);
        }
        System.out.print("Введите номер формата (например, 1 для JPEG): ");
        int choice = Integer.parseInt(scanner.nextLine());

        if (choice < 1 || choice > ImageFormat.values().length) {
            throw new IllegalArgumentException("Неверный номер формата.");
        }

        return ImageFormat.values()[choice - 1];
    }

    public ImageProcessor configureImageProcessor() {
        System.out.println("Выберите корректор для изображения:");
        System.out.println("1. GammaCorrectionProcessor");
        System.out.println("2. LogarithmicGammaCorrectionProcessor");
        System.out.println("3. Без коррекции");
        System.out.print("Введите номер (1, 2 или 3): ");
        int choice = Integer.parseInt(scanner.nextLine());

        if (choice == 3) {
            System.out.println("Корректор не выбран.");
            return null;
        }

        if (choice < 1 || choice > 2) {
            throw new IllegalArgumentException("Неверный выбор корректора.");
        }

        System.out.print("Введите значение scaleFactor (рекомендуется от 0.1 до 10): ");
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
