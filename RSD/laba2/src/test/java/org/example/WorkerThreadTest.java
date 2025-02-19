package org.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class WorkerThreadTest {

    private WorkerThread workerThread;
    private ProgressBar progressBar;
    private TimeTracker timeTracker;
    private final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

    @BeforeEach
    public void setUp() {
        progressBar = new ProgressBar(10, '#');
        timeTracker = new TimeTracker();
        workerThread = new WorkerThread(1, 10, progressBar, timeTracker);
        System.setOut(new PrintStream(outputStream));
    }

    @Test // Проверяет, что поток завершился корректно и прогресс-бар достиг 100%
    public void testWorkerThreadCompletion() throws InterruptedException {
        Thread thread = new Thread(workerThread);
        thread.start();
        thread.join();

        assertTrue(workerThread.isFinished(), "The thread should be finished");
        String actualOutput = outputStream.toString();
        String[] outputs = actualOutput.split("Thread completed!");
        String lastProgressBarOutput = outputs[0].substring(outputs[0].lastIndexOf("\r")).trim();
        String expectedFinalProgressBar = "[##################################################]";
        assertEquals(expectedFinalProgressBar, lastProgressBarOutput, "Final progress bar should be complete.");
    }


    @Test // Проверяет корректность работы таймера, что время запуска и остановки отслежено
    public void testTimeTracking() throws InterruptedException {
        Thread thread = new Thread(workerThread);
        thread.start();
        thread.join();

        assertTrue(timeTracker.getStartTime() > 0, "The timer should track start time");
        assertTrue(timeTracker.getEndTime() > 0, "The timer should track end time");
        assertTrue(timeTracker.getElapsedTime() >= 1000, "The elapsed time should be at least 1 second");
    }

    @Test // Проверяет правильность вычисления результата (sqrt суммы)
    public void testResultCalculation() throws InterruptedException {
        Thread thread = new Thread(workerThread);
        thread.start();
        thread.join();

        double expectedResult = 0;
        for (int i = 0; i < 10; i++) {
            expectedResult += Math.sqrt(i);
        }
        assertEquals(expectedResult, workerThread.getResult(), 0.01);
    }
}