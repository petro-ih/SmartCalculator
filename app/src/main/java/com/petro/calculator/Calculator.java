package com.petro.calculator;

import android.text.TextUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.StringTokenizer;
import java.util.function.BiFunction;
import java.util.function.Function;

public class Calculator {
    private static final String OPERATIONS = "+-*/"; // 2 parameters, e.g.: a + b
    private static final String FUNCTIONS = "SHCBTKLG"; // 1 parameter, e.g.: sin(a)
    private static final String L_BRACKET = "(";
    private static final String R_BRACKET = ")";
    private static final Map<String, Function<Double, Double>> FUNCTIONS_MAP = new HashMap<>();
    private static final Map<String, BiFunction<Double, Double, Double>> OPERATORS_MAP = new HashMap<>();

    static {
        FUNCTIONS_MAP.put("S", Math::sin);
        FUNCTIONS_MAP.put("H", Math::sinh);
        FUNCTIONS_MAP.put("C", Math::cos);
        FUNCTIONS_MAP.put("B", Math::cosh);
        FUNCTIONS_MAP.put("T", Math::tan);
        FUNCTIONS_MAP.put("K", Math::tanh);
        FUNCTIONS_MAP.put("L", Math::log);
        FUNCTIONS_MAP.put("G", Math::log10);

        //noinspection Convert2MethodRef
        OPERATORS_MAP.put("+", (a, b) -> a + b);
        OPERATORS_MAP.put("-", (a, b) -> a - b);
        OPERATORS_MAP.put("*", (a, b) -> a * b);
        OPERATORS_MAP.put("/", (a, b) -> a / b);
    }

    private static SymbolType getType(String value) {
        if (TextUtils.isDigitsOnly(value)) return SymbolType.DIGIT;
        if (OPERATIONS.contains(value)) return SymbolType.OPERATION;
        if (FUNCTIONS.contains(value)) return SymbolType.FUNCTION;
        if (value.equals(L_BRACKET)) return SymbolType.L_BRACKET;
        if (value.equals(R_BRACKET)) return SymbolType.R_BRACKET;
        throw new UnsupportedOperationException("Unsupported symbol type for " + value);
    }

    private static String[] tokenize(String value) {
        StringTokenizer tokenizer = new StringTokenizer(value, OPERATIONS + FUNCTIONS + L_BRACKET + R_BRACKET, true);
        ArrayList<String> result = new ArrayList<>();
        while (tokenizer.hasMoreTokens()) result.add(tokenizer.nextToken());
        return result.toArray(new String[0]);
    }

    private static String[] reversePolishNotation(String[] tokens) {
        ArrayList<String> finalString = new ArrayList<>();
        Stack stack = new Stack(1000);
        for (String token : tokens) {
// Если символ является числом или постфиксной функцией (например, ! — факториал), добавляем его к выходной строке.
            if (getType(token) == SymbolType.DIGIT) finalString.add(token);
// Если символ является префиксной функцией (например, sin — синус), помещаем его в стек.
            else if (getType(token) == SymbolType.FUNCTION) stack.push(token);
// Если символ является открывающей скобкой, помещаем его в стек.
            else if (getType(token) == SymbolType.L_BRACKET) stack.push(token);
// Если символ является закрывающей скобкой
            else if (getType(token) == SymbolType.R_BRACKET) {
// До тех пор, пока верхним элементом стека не станет открывающая скобка,
// выталкиваем элементы из стека в выходную строку.
// При этом открывающая скобка удаляется из стека,
// но в выходную строку не добавляется.
// Если стек закончился раньше, чем мы встретили открывающую скобку,
// это означает, что в выражении либо неверно поставлен разделитель,
// либо не согласованы скобки.
                while (!stack.peak().equals(L_BRACKET)) {
                    finalString.add(stack.pop());
                }
                stack.pop();
            }
//            Если символ является бинарной операцией о1, тогда:
//            1) пока на вершине стека префиксная функция…
//… ИЛИ операция на вершине стека приоритетнее или такого же уровня приоритета как o1
//… ИЛИ операция на вершине стека левоассоциативная с приоритетом как у o1
//… выталкиваем верхний элемент стека в выходную строку;
//            2) помещаем операцию o1 в стек.
            else if (getType(token) == SymbolType.OPERATION) {
                int priority = getOperatorPriority(token);
                while (stack.size() > 0 && priority <= getOperatorPriority(stack.peak())) {
                    finalString.add(stack.pop());
                }
                stack.push(token);
            }

        }
//        Когда входная строка закончилась, выталкиваем все символы из стека в выходную строку.
//        В стеке должны были остаться только символы операций;
//        если это не так, значит в выражении не согласованы скобки.
        while (stack.size() > 0) {
            finalString.add(stack.pop());
        }

        return finalString.toArray(new String[0]);
    }

    private static int getOperatorPriority(String operator) {
        if (operator.equals(L_BRACKET) || operator.equals(R_BRACKET)) return 0;
        if (operator.equals("+") || operator.equals("-")) return 1;
        if (operator.equals("*") || operator.equals("/")) return 2;
        if (operator.equals("^") || operator.equals("Q") || operator.equals("!")) return 3;
        return 4;
    }

    /*
     * 1 Обработка входного символа
     * 1.1 Если на вход подан операнд, он помещается на вершину стека.
     * 1.2 Если на вход подан знак операции, то соответствующая операция выполняется над
     *    требуемым количеством значений, извлечённых из стека, взятых в порядке добавления.
     *    Результат выполненной операции кладётся на вершину стека.
     * 2. Если входной набор символов обработан не полностью, перейти к шагу 1.
     * 3. После полной обработки входного набора символов результат вычисления выражения
     *    лежит на вершине стека.
     */
    private static double evaluate(String[] values) {
        Stack stack = new Stack(1000);
        for (String value : values) {
            if (getType(value) == SymbolType.DIGIT) {
                stack.push(value);
            } else if (getType(value) == SymbolType.OPERATION) {
                double a = Double.parseDouble(stack.pop());
                double b = Double.parseDouble(stack.pop());
                String result = String.valueOf(Objects.requireNonNull(OPERATORS_MAP.get(value)).apply(a, b));
                stack.push(result);
            } else if (getType(value) == SymbolType.FUNCTION) {
                double a = Double.parseDouble(stack.pop());
                String result = String.valueOf(Objects.requireNonNull(FUNCTIONS_MAP.get(value)).apply(a));
                stack.push(result);
            }
        }
        return Double.parseDouble(stack.pop());
    }

    public static String calculate(String problem) {
        String problemWithReplacedSymbols = problem
                .replace("sin", "S")
                .replace("sinh", "H")
                .replace("cos", "C")
                .replace("cosh", "B")
                .replace("tan", "T")
                .replace("tanh", "K")
                .replace("ln", "L")
                .replace("lg", "G")
                .replace("x", "*");

        String[] tokens = tokenize(problemWithReplacedSymbols);
        String[] polishNotation = reversePolishNotation(tokens);
        double result = evaluate(polishNotation);
        return String.valueOf(result);
    }

    private enum SymbolType {
        DIGIT, OPERATION, FUNCTION, L_BRACKET, R_BRACKET, UNSUPPORTED
    }
}
