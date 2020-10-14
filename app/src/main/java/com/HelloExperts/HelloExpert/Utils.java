package com.HelloExperts.HelloExpert;


public class Utils {

    private static final long B = 1;
    private static final long KB = B * 1024;
    private static final long MB = KB * 1024;
    private static final long GB = MB * 1024;

    public static double parseSpeed(double bytes, boolean inBits) {
        double value = inBits ? bytes * 8 : bytes;

        return value*8/MB;

    }

}