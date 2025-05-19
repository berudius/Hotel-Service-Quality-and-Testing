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
public class TestRomanToIntegerInterpreter {
     @Test
    void testI() {
        assertEquals(1, RomanToIntegerInterpreter.interpret("I"));
    }

    @Test
    void testIV() {
        assertEquals(4, RomanToIntegerInterpreter.interpret("IV"));
    }

    @Test
    void testIX() {
        assertEquals(9, RomanToIntegerInterpreter.interpret("IX"));
    }

    @Test
    void testXIV() {
        assertEquals(14, RomanToIntegerInterpreter.interpret("XIV"));
    }

    @Test
    void testXL() {
        assertEquals(40, RomanToIntegerInterpreter.interpret("XL"));
    }

    @Test
    void testXC() {
        assertEquals(90, RomanToIntegerInterpreter.interpret("XC"));
    }

    @Test
    void testCD() {
        assertEquals(400, RomanToIntegerInterpreter.interpret("CD"));
    }

    @Test
    void testCM() {
        assertEquals(900, RomanToIntegerInterpreter.interpret("CM"));
    }

    @Test
    void testMCMXCIV() {
        assertEquals(1994, RomanToIntegerInterpreter.interpret("MCMXCIV"));
    }

    @Test
    void testInvalidRomanNumeralThrowsException() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            RomanToIntegerInterpreter.interpret("IIIV");
        });
        assertEquals("Некоректне римське число: IIIV", exception.getMessage());
    }
}
