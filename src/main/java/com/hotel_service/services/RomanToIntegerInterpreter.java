package com.hotel_service.services;

/*
    @author Berkeshchuk
    @project hotel-service
    @class DishService
    @version 1.0.0
    @since 5/3/2025-11.28
*/
public class RomanToIntegerInterpreter {

    private static class RomanNumeral {
        String symbol;
        int value;

        RomanNumeral(String symbol, int value) {
            this.symbol = symbol;
            this.value = value;
        }
    }

    private static final RomanNumeral[] romanNumerals = {
        new RomanNumeral("CM", 900),
        new RomanNumeral("CD", 400),
        new RomanNumeral("XC", 90),
        new RomanNumeral("XL", 40),
        new RomanNumeral("IX", 9),
        new RomanNumeral("IV", 4),
        new RomanNumeral("M", 1000),
        new RomanNumeral("D", 500),
        new RomanNumeral("C", 100),
        new RomanNumeral("L", 50),
        new RomanNumeral("X", 10),
        new RomanNumeral("V", 5),
        new RomanNumeral("I", 1)
    };

    public static int interpret(String roman) {
        if (!roman.matches("^(M{0,3})(CM|CD|D?C{0,3})(XC|XL|L?X{0,3})(IX|IV|V?I{0,3})$")) {
            throw new IllegalArgumentException("Некоректне римське число: " + roman);
        }

        int index = 0;
        int result = 0;

        while (index < roman.length()) {
            boolean matched = false;

            for (RomanNumeral numeral : romanNumerals) {
                if (roman.startsWith(numeral.symbol, index)) {
                    result += numeral.value;
                    index += numeral.symbol.length();
                    matched = true;
                    break;
                }
            }

            if (!matched) {
                throw new IllegalArgumentException("Некоректне римське число: " + roman);
            }
        }

        return result;
    }
}

