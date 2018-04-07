package com.bluehook.models;

public class MathService {

    public static Integer calculate(String operation, Integer x, Integer y) {
        if (operation.equals("add")) {
            return x + y;
        } else if (operation.equals("subtract")) {
            return x - y;
        } else if (operation.equals("multiply")) {
            return x * y;
        } else {
            return x / y;
        }
    }

}
