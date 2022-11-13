package com.petro.calculator;

import org.junit.Test;

public class PolishNotationTest {
    @Test
    public void convertToPolishNotation(){
        String input = "1+2x3";
        String[] output = Calculator.reversePolishNotation(input);
        assert output[0].equals("1");
        assert output[1].equals("+");
        assert output[2].equals("x");
        assert output[3].equals("2");
        assert output[4].equals("3");

    }
}