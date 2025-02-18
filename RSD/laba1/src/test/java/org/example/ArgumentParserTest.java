package org.example;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.io.IOException;

class ArgumentParserTest {

    // Тестирует корректный парсинг аргументов командной строки
    @Test
    void testParseWithValidArguments() throws IOException {
        String[] args = {"file1.txt", "file2.txt", "-o", "/output/path", "-p", "prefix_", "-a", "-s"};
        Arguments arguments = ArgumentParser.parse(args);

        assertEquals(2, arguments.getFiles().size());
        assertEquals("file1.txt", arguments.getFiles().get(0));
        assertEquals("file2.txt", arguments.getFiles().get(1));
        assertEquals("/output/path", arguments.getPathResult());
        assertEquals("prefix_", arguments.getPrefix());
        assertTrue(arguments.getAddToExist());
        assertEquals(Statistics.StatMode.SHORT, arguments.getStatMode());
    }

    // Тестирует ситуацию, когда аргументы не переданы
    @Test
    void testParseWithNoArguments() {
        String[] args = {};
        assertThrows(IOException.class, () -> ArgumentParser.parse(args));
    }

    // Тестирует ошибку при пропущенном пути после опции -o
    @Test
    void testParseWithMissingPathAfterO() {
        String[] args = {"file1.txt", "-o"};
        assertThrows(IOException.class, () -> ArgumentParser.parse(args));
    }

    // Тестирует ошибку при пропущенном префиксе после опции -p
    @Test
    void testParseWithMissingPrefixAfterP() {
        String[] args = {"file1.txt", "-p"};
        assertThrows(IOException.class, () -> ArgumentParser.parse(args));
    }

    // Тестирует ошибку при передаче обеих опций для статуса -s и -f
    @Test
    void testParseWithBothStatModes() {
        String[] args = {"file1.txt", "-s", "-f"};
        assertThrows(IllegalArgumentException.class, () -> ArgumentParser.parse(args));
    }

    // Тестирует корректный парсинг с использованием значений по умолчанию
    @Test
    void testParseWithDefaultValues() throws IOException {
        String[] args = {"file1.txt"};
        Arguments arguments = ArgumentParser.parse(args);

        assertEquals(1, arguments.getFiles().size());
        assertEquals("file1.txt", arguments.getFiles().get(0));
        assertEquals("./", arguments.getPathResult());
        assertEquals("-", arguments.getPrefix());
        assertFalse(arguments.getAddToExist());
        assertEquals(Statistics.StatMode.NONE, arguments.getStatMode());
    }

    // Тестирует парсинг с режимом FULL для статистики
    @Test
    void testParseWithFullStatMode() throws IOException {
        String[] args = {"file1.txt", "-f"};
        Arguments arguments = ArgumentParser.parse(args);

        assertEquals(Statistics.StatMode.FULL, arguments.getStatMode());
    }
}