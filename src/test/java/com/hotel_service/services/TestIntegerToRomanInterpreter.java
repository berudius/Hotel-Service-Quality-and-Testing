package com.hotel_service.services;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/*
    @author Berkeshchuk
    @project hotel-service
    @class DishService
    @version 1.0.0
    @since 5/3/2025-11.28
*/
public class TestIntegerToRomanInterpreter {
    
    @Test
    void test1() {
        assertEquals("I", IntegerToRomanInterpreter.interpret(1));
    }

    @Test
    void test2() {
        assertEquals("II", IntegerToRomanInterpreter.interpret(2));
    }

    @Test
    void test3() {
        assertEquals("III", IntegerToRomanInterpreter.interpret(3));
    }

    @Test
    void test4() {
        assertEquals("IV", IntegerToRomanInterpreter.interpret(4));
    }

    @Test
    void test5() {
        assertEquals("V", IntegerToRomanInterpreter.interpret(5));
    }

    @Test
    void test6() {
        assertEquals("VI", IntegerToRomanInterpreter.interpret(6));
    }

    @Test
    void test9() {
        assertEquals("IX", IntegerToRomanInterpreter.interpret(9));
    }

    @Test
    void test10() {
        assertEquals("X", IntegerToRomanInterpreter.interpret(10));
    }

    @Test
    void test11() {
        assertEquals("XI", IntegerToRomanInterpreter.interpret(11));
    }

    @Test
    void test14() {
        assertEquals("XIV", IntegerToRomanInterpreter.interpret(14));
    }

    @Test
    void test15() {
        assertEquals("XV", IntegerToRomanInterpreter.interpret(15));
    }

    @Test
    void test19() {
        assertEquals("XIX", IntegerToRomanInterpreter.interpret(19));
    }

    @Test
    void test40() {
        assertEquals("XL", IntegerToRomanInterpreter.interpret(40));
    }

    @Test
    void test50() {
        assertEquals("L", IntegerToRomanInterpreter.interpret(50));
    }

    @Test
    void test90() {
        assertEquals("XC", IntegerToRomanInterpreter.interpret(90));
    }

    @Test
    void test100() {
        assertEquals("C", IntegerToRomanInterpreter.interpret(100));
    }

    @Test
    void test400() {
        assertEquals("CD", IntegerToRomanInterpreter.interpret(400));
    }

    @Test
    void test500() {
        assertEquals("D", IntegerToRomanInterpreter.interpret(500));
    }

    @Test
    void test900() {
        assertEquals("CM", IntegerToRomanInterpreter.interpret(900));
    }

    @Test
    void test1000() {
        assertEquals("M", IntegerToRomanInterpreter.interpret(1000));
    }

    @Test
    void test1994() {
        assertEquals("MCMXCIV", IntegerToRomanInterpreter.interpret(1994));
    }

    @Test
    void test3999() {
        assertEquals("MMMCMXCIX", IntegerToRomanInterpreter.interpret(3999));
    }

    @Test
    void testZeroThrowsException() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            IntegerToRomanInterpreter.interpret(0);
        });
        assertEquals("Число повинно бути в межах 1-3999", exception.getMessage());
    }

    @Test
    void testNegativeNumberThrowsException() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            IntegerToRomanInterpreter.interpret(-5);
        });
        assertEquals("Число повинно бути в межах 1-3999", exception.getMessage());
    }

    @Test
    void testNumberGreaterThan3999ThrowsException() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            IntegerToRomanInterpreter.interpret(4000);
        });
        assertEquals("Число повинно бути в межах 1-3999", exception.getMessage());
    }
}


