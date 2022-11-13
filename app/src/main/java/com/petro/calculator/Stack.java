package com.petro.calculator;

public class Stack {
    private final String[] arr;
    private int pointer;

    Stack(int size) {
        arr = new String[size];
        pointer = -1;
    }

    public void push(String item) {
        arr[++pointer] = item;
    }

    public String pop() {
        return arr[pointer--];
    }

    public String peak() {
        return arr[pointer];
    }

    public int size() {
        return pointer + 1;
    }

    public boolean isFull() {
        return pointer == arr.length - 1;
    }

    public boolean isEmpty() {
        return pointer == -1;
    }
}
