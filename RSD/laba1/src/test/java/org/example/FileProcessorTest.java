package org.example;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FileProcessorTest {

    // Тестирует чтение файла и классификацию содержимого
    @Test
    void testReadFile() throws IOException {
        Path tempFile = Files.createTempFile("testfile", ".txt");
        Files.write(tempFile, "line1\nline2".getBytes());

        DataClassifier classifier = new DataClassifier();
        Arguments arguments = new Arguments(List.of(), "./", "-", false, Statistics.StatMode.NONE);

        FileProcessor fileProcessor = new FileProcessor(classifier, arguments);

        fileProcessor.readFile(tempFile.toString());

        assertTrue(
                !classifier.getInteger().isEmpty() ||
                        !classifier.getFloat().isEmpty() ||
                        !classifier.getString().isEmpty()
        );
    }

    // Тестирует запись результатов классификации в файлы
    @Test
    void testWriteResults(@TempDir Path tempDir) throws IOException {

        DataClassifier classifier = new DataClassifier();
        classifier.classify("123");
        classifier.classify("123.45");
        classifier.classify("abc");

        Arguments arguments = new Arguments(List.of(), tempDir.toString(), "test_", false, Statistics.StatMode.NONE);

        FileProcessor fileProcessor = new FileProcessor(classifier, arguments);

        fileProcessor.writeResults();

        assertTrue(Files.exists(tempDir.resolve("test_integers.txt")));
        assertTrue(Files.exists(tempDir.resolve("test_floats.txt")));
        assertTrue(Files.exists(tempDir.resolve("test_strings.txt")));
    }
}
