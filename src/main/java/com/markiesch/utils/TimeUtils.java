package com.markiesch.utils;

import java.util.concurrent.TimeUnit;

public class TimeUtils {
    public static String makeReadable(Long time) {
        if (time == null) return "";

        String string = "";

        long days = TimeUnit.MILLISECONDS.toDays(time);
        long hours = TimeUnit.MILLISECONDS.toHours(time) - TimeUnit.DAYS.toHours(TimeUnit.MILLISECONDS.toDays(time));
        long minutes = TimeUnit.MILLISECONDS.toMinutes(time) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(time));
        long seconds = TimeUnit.MILLISECONDS.toSeconds(time) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(time));

        if (days != 0L) string += " " + days + "d";
        if (hours != 0L) string += " " + hours + "h";
        if (minutes != 0L) string += " " + minutes + "m";
        if (seconds != 0L) string += " " + seconds + "s";

        if (string.isEmpty()) string = "Permanent";
        return string.trim();
    }

    public static long parseTime(String input) {
        long result = 0L;
        StringBuilder number = new StringBuilder();
        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);
            if (Character.isDigit(c)) {
                number.append(c);
            } else if (Character.isLetter(c) && number.length() > 0) {
                result += convert(Integer.parseInt(number.toString()), c);
                number = new StringBuilder();
            }
        }
        return result;
    }

    private static long convert(long value, char unit) {
        return switch (unit) {
            case 'y' -> value * 1000L * 60L * 60L * 24L * 365;
            case 'd' -> value * 1000L * 60L * 60L * 24L;
            case 'h' -> value * 1000L * 60L * 60L;
            case 'm' -> value * 1000L * 60L;
            case 's' -> value * 1000L;
            default -> 0L;
        };
    }
}
