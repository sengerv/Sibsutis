package org.example;

import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        try {
            Arguments arguments = ArgumentParser.parse(args);
            DataClassifier classifier = new DataClassifier();
            FileProcessor fileProcessor = new FileProcessor(classifier, arguments);

            for (String filePath : arguments.getFiles()) {
                System.out.println("Reading file: " + filePath);
                fileProcessor.readFile(filePath);
            }
            fileProcessor.writeResults();

            Statistics statistics = new Statistics(classifier, arguments);
            Statistics.StatMode mode = arguments.getStatMode();
            statistics.printStatistics(mode);

        } catch (IllegalArgumentException | IOException e) {
            System.err.println("Error processing file: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("An unexpected error occurred: " + e.getMessage());
        }
    }
}
