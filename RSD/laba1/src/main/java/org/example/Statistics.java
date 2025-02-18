package org.example;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Statistics {

    public enum StatMode {
        NONE, SHORT, FULL
    }

    private final DataClassifier classifier;
    private final Arguments arguments;

    public Statistics(DataClassifier classifier, Arguments arguments) {
        this.classifier = classifier;
        this.arguments = arguments;
    }

    public void printStatistics(StatMode mode) {
        switch (mode) {
            case SHORT:
                printShortStat();
                break;
            case FULL:
                printFullStat();
                break;
            case NONE:
                System.out.println("No statistics mode selected.");
                break;
        }
    }

    public void printShortStat() {
        // Получаем пути к файлам и данные
        String prefix = arguments.getPrefix();
        String pathResult = arguments.getPathResult();

        Set<Integer> integers = readDataFromFile(pathResult, prefix + "integers.txt", Integer::parseInt);
        Set<Float> floats = readDataFromFile(pathResult, prefix + "floats.txt", Float::parseFloat);
        Set<String> strings = readDataFromFile(pathResult, prefix + "strings.txt", Function.identity());

        printNumberStatistics(integers, "Integers");
        printNumberStatistics(floats, "Floats");
        printStringStatistics(strings);
    }

    public void printFullStat() {
        // Получаем пути к файлам и данные
        String prefix = arguments.getPrefix();
        String pathResult = arguments.getPathResult();

        Set<Integer> integers = readDataFromFile(pathResult, prefix + "integers.txt", Integer::parseInt);
        Set<Float> floats = readDataFromFile(pathResult, prefix + "floats.txt", Float::parseFloat);
        Set<String> strings = readDataFromFile(pathResult, prefix + "strings.txt", Function.identity());

        printNumberStatistics(integers, "Integers");
        printNumberStatistics(floats, "Floats");
        printStringStatistics(strings);
    }

    private <T> Set<T> readDataFromFile(String pathResult, String fileName, Function<String, T> parser) {
        Path filePath = Paths.get(pathResult, fileName);
        if (!Files.exists(filePath)) {
            System.out.println("File " + filePath + " does not exist.");
            return Collections.emptySet();
        }
        try {
            return Files.readAllLines(filePath).stream()
                    .map(parser)
                    .collect(Collectors.toSet());
        } catch (IOException | NumberFormatException e) {
            System.err.println("Error reading file " + fileName + ": " + e.getMessage());
            return Collections.emptySet();
        }
    }

    private void printNumberStatistics(Set<? extends Number> numbers, String label) {
        if (numbers.isEmpty()) {
            System.out.println(label + ": No data available.");
            return;
        }

        double min = Double.MAX_VALUE;
        double max = Double.MIN_VALUE;
        double sum = 0;

        for (Number number : numbers) {
            double value = number.doubleValue();
            if (value < min) {
                min = value;
            }
            if (value > max) {
                max = value;
            }
            sum += value;
        }

        double average = sum / numbers.size();

        System.out.println(label + " statistics:");
        System.out.println("  Count: " + numbers.size());
        System.out.println("  Minimum: " + min);
        System.out.println("  Maximum: " + max);
        System.out.println("  Sum: " + sum);
        System.out.println("  Average: " + average);
    }

    private void printStringStatistics(Set<String> strings) {
        if (strings.isEmpty()) {
            System.out.println("Strings: No data available.");
            return;
        }

        int minLen = Integer.MAX_VALUE;
        int maxLen = Integer.MIN_VALUE;

        for (String str : strings) {
            int length = str.length();
            if (length < minLen) {
                minLen = length;
            }
            if (length > maxLen) {
                maxLen = length;
            }
        }

        System.out.println("String statistics:");
        System.out.println("  Total strings: " + strings.size());
        System.out.println("  Shortest string length: " + (minLen != Integer.MAX_VALUE ? minLen : -1) + " characters");
        System.out.println("  Longest string length: " + (maxLen != Integer.MIN_VALUE ? maxLen : -1) + " characters");
    }
}
