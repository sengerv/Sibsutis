package org.example;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Set;

import static org.mockito.Mockito.*;

class StatisticsTest {

    // Тестирует метод printShortStat, проверяя корректность чтения файлов и обработки данных
    @Test
    void testPrintShortStat(@TempDir Path tempDir) throws IOException {
        Path integersFile = tempDir.resolve("integers.txt");
        Files.write(integersFile, Set.of("10", "20", "30"));

        Path floatsFile = tempDir.resolve("floats.txt");
        Files.write(floatsFile, Set.of("1.5", "2.5", "3.5"));

        Path stringsFile = tempDir.resolve("strings.txt");
        Files.write(stringsFile, Set.of("abc", "def", "ghij"));

        DataClassifier classifier = mock(DataClassifier.class);
        Arguments arguments = mock(Arguments.class);

        when(arguments.getPrefix()).thenReturn("");
        when(arguments.getPathResult()).thenReturn(tempDir.toString());

        Statistics statistics = new Statistics(classifier, arguments);

        statistics.printShortStat();

        verify(arguments, times(1)).getPrefix();
        verify(arguments, times(1)).getPathResult();
    }

    // Тестирует метод printFullStat, проверяя корректность чтения файлов и полной статистики
    @Test
    void testPrintFullStat(@TempDir Path tempDir) throws IOException {
        Path integersFile = tempDir.resolve("integers.txt");
        Files.write(integersFile, Set.of("10", "20", "30"));

        Path floatsFile = tempDir.resolve("floats.txt");
        Files.write(floatsFile, Set.of("1.5", "2.5", "3.5"));

        Path stringsFile = tempDir.resolve("strings.txt");
        Files.write(stringsFile, Set.of("abc", "def", "ghij"));

        DataClassifier classifier = mock(DataClassifier.class);
        Arguments arguments = mock(Arguments.class);

        when(arguments.getPrefix()).thenReturn("");
        when(arguments.getPathResult()).thenReturn(tempDir.toString());

        Statistics statistics = new Statistics(classifier, arguments);

        statistics.printFullStat();

        verify(arguments, times(1)).getPrefix();
        verify(arguments, times(1)).getPathResult();
    }

    // Тестирует режим StatMode.NONE, проверяя, что данные не читаются и не обрабатываются
    @Test
    void testPrintStatisticsNoneMode() {
        DataClassifier classifier = mock(DataClassifier.class);
        Arguments arguments = mock(Arguments.class);

        Statistics statistics = new Statistics(classifier, arguments);

        statistics.printStatistics(Statistics.StatMode.NONE);

        verify(arguments, never()).getPrefix();
        verify(arguments, never()).getPathResult();
    }
}