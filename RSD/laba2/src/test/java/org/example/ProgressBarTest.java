package org.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import static org.junit.jupiter.api.Assertions.*;

class ProgressBarTest {
    private ProgressBar progressBar;
    private final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

    @BeforeEach
    void setUp() {
        // Перенаправляем вывод в консоль в outputStream для проверки
        System.setOut(new PrintStream(outputStream));
        progressBar = new ProgressBar(100, '#');
    }

    @Test
    void testUpdateProgress() {
        progressBar.updateProgress(10);
        assertEquals(10, progressBar.getProgress());
    }

    @Test
    void testDisplayProgress() {
        progressBar.updateProgress(50); // Устанавливаем прогресс на 50%
        progressBar.displayProgress(); // Выводим прогресс
        String expectedOutput = "[#########################                         ]"; // 50% прогресса
        assertTrue(outputStream.toString().contains(expectedOutput)); // Проверяем вывод
    }

    @Test
    public void testCompleteProgress() {
        progressBar.completeProgress();
        String actualOutput = outputStream.toString().trim();
        String expectedProgressBar = "[##################################################]";
        assertTrue(actualOutput.contains(expectedProgressBar), "Final progress bar should be complete.");
        assertTrue(actualOutput.contains("Thread completed!"), "The output should contain the completion message.");
    }

    @Test
    void testProgressDoesNotExceedMaxLength() {
        for (int i = 0; i < 150; i++) {
            progressBar.updateProgress(1);
        }
        assertEquals(100, progressBar.getProgress());
    }
}