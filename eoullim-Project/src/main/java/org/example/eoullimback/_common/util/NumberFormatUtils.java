package org.example.eoullimback._common.util;

public class NumberFormatUtils {

    public static String formatK(long count) {
        if (count < 1000) return String.valueOf(count);
        return String.format("%.1fk", count / 1000.0);
    }

    public static String formatComma(long count) {
        return String.format("%,d", count);
    }

    public static double calculateRate(long today, long yesterday) {
        if (yesterday == 0) {
            return today == 0 ? 0.0 : 100.0;
        };
        return Math.round(
                ((double) (today - yesterday) / yesterday) * 1000
        ) / 10.0;
    }
}
