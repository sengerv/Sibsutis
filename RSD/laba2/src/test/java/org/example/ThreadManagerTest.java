package org.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ThreadManagerTest {
    private ThreadManager threadManager;

    @BeforeEach
    void setUp() {
        // Инициализация ThreadManager перед каждым тестом
        threadManager = new ThreadManager(3, 10); // 3 потока, каждый выполняет задачу длиной 10
    }

    @Test
    void testInitializeThreads() {
        // Проверка инициализации потоков
        threadManager.initializeThreads();
        assertEquals(3, threadManager.getThreads().size()); // Должно быть 3 потока
    }

    @Test
    void testRunAndStopThreads() throws InterruptedException {
        // Проверка запуска и завершения потоков
        threadManager.initializeThreads();
        threadManager.runThread(); // Запускаем потоки
        threadManager.stopThread(); // Ожидаем завершения потоков

        // Проверяем, что все потоки завершены
        for (WorkerThread thread : threadManager.getThreads()) {
            assertTrue(thread.isFinished()); // Каждый поток должен быть завершен
        }
    }

    @Test
    void testWatcherProgress() {
        // Проверка вывода результатов
        threadManager.initializeThreads();
        threadManager.runThread();
        threadManager.stopThread();

        // Проверяем, что результаты выводятся корректно
        assertDoesNotThrow(() -> threadManager.watcherProgress()); // Метод не должен выбрасывать исключений
    }
}