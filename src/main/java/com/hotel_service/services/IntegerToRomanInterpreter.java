package com.hotel_service.services;

/*
    @author Berkeshchuk
    @project hotel-service
    @class DishService
    @version 1.0.0
    @since 5/3/2025-11.28
*/
public class IntegerToRomanInterpreter  {
    private static class RomanNumeral {
        int value;
        String symbol;

        RomanNumeral(int value, String symbol) {
            this.value = value;
            this.symbol = symbol;
        }
    }

    private static final RomanNumeral[] romanNumerals = {
        new RomanNumeral(1000, "M"),
        new RomanNumeral(900, "CM"),
        new RomanNumeral(500, "D"),
        new RomanNumeral(400, "CD"),
        new RomanNumeral(100, "C"),
        new RomanNumeral(90, "XC"),
        new RomanNumeral(50, "L"),
        new RomanNumeral(40, "XL"),
        new RomanNumeral(10, "X"),
        new RomanNumeral(9, "IX"),
        new RomanNumeral(5, "V"),
        new RomanNumeral(4, "IV"),
        new RomanNumeral(1, "I")
    };

    public static String interpret(int number) {
        if (number <= 0 || number > 3999) {
            throw new IllegalArgumentException("Число повинно бути в межах 1-3999");
        }

        String result = "";

        for (RomanNumeral numeral : romanNumerals) {
            while (number >= numeral.value) {
                result += numeral.symbol;
                number -= numeral.value;
            }
        }

        return result;
    }
}
