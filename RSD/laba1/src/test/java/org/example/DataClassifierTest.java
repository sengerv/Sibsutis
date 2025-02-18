package org.example;

import org.junit.jupiter.api.Test;
import java.util.Set;
import static org.junit.jupiter.api.Assertions.*;

class DataClassifierTest {

    // Тестирует метод classify, проверяя корректность классификации строк как целые числа, числа с плавающей точкой и строки
    @Test
    void testClassify() {

        DataClassifier classifier = new DataClassifier();

        classifier.classify("123");
        classifier.classify("45.67");
        classifier.classify("abc");
        classifier.classify("42");
        classifier.classify("0.5");
        classifier.classify("xyz");

        Set<Integer> integers = classifier.getInteger();
        Set<Float> floats = classifier.getFloat();
        Set<String> strings = classifier.getString();

        assertEquals(2, integers.size());
        assertTrue(integers.contains(123));
        assertTrue(integers.contains(42));

        assertEquals(2, floats.size());
        assertTrue(floats.contains(45.67f));
        assertTrue(floats.contains(0.5f));

        assertEquals(2, strings.size());
        assertTrue(strings.contains("abc"));
        assertTrue(strings.contains("xyz"));
    }
}