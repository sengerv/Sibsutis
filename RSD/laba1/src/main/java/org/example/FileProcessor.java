package org.example;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Set;


public class FileProcessor {
    private final DataClassifier classifier;
    private final Arguments arguments;

    public FileProcessor(DataClassifier classifier, Arguments arguments){
        this.classifier = classifier;
        this.arguments = arguments;
    }

    public void readFile(String filePath) throws IOException{
        try {
            BufferedReader reader = new BufferedReader(new FileReader(filePath));
            String line;
            while ((line = reader.readLine()) != null) {
                classifier.classify(line);
            }
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
            throw e;
        }
    }

    public void writeResults() throws IOException {
        String prefix = arguments.getPrefix();
        writeDataToFile(arguments.getPathResult(), prefix + "integers.txt", classifier.getInteger());
        writeDataToFile(arguments.getPathResult(), prefix + "floats.txt", classifier.getFloat());
        writeDataToFile(arguments.getPathResult(), prefix + "strings.txt", classifier.getString());
    }

    private <T> void writeDataToFile(String PathResult, String file, Set<T> data) throws IOException {
        if (data.isEmpty()) {
            return;
        }

        Path filePath = Paths.get(PathResult, file);

        StandardOpenOption[] options = arguments.getAddToExist()
                ? new StandardOpenOption[]{StandardOpenOption.CREATE, StandardOpenOption.APPEND}
                : new StandardOpenOption[]{StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING};

        try (BufferedWriter writer = Files.newBufferedWriter(filePath, options)) {
            for (T item : data) {
                writer.write(item.toString());
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error writing to file " + file + ": " + e.getMessage());
            throw e;
        }
    }
}
