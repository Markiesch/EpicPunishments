package com.markiesch.utils;

import java.util.concurrent.TimeUnit;

public class TimeUtils {
    public static String makeReadable(Long time) {
        if (time == null) return "";

        String string = "";

        long days = TimeUnit.SECONDS.toDays(time);
        long hours = TimeUnit.SECONDS.toHours(time) - TimeUnit.DAYS.toHours(TimeUnit.SECONDS.toDays(time));
        long minutes = TimeUnit.SECONDS.toMinutes(time) - TimeUnit.HOURS.toMinutes(TimeUnit.SECONDS.toHours(time));
        long seconds = TimeUnit.SECONDS.toSeconds(time) - TimeUnit.MINUTES.toSeconds(TimeUnit.SECONDS.toMinutes(time));

        if (days != 0L) string += " " + days + "d";
        if (hours != 0L) string += " " + hours + "h";
        if (minutes != 0L) string += " " + minutes + "m";
        if (seconds != 0L) string += " " + seconds + "s";

        if (string.isEmpty()) string = "Permanent";
        return string.trim();
    }

    public static long parseTime(String input) {
        StringBuilder number = new StringBuilder();
        long result = 0L;

        for (char c : input.toCharArray()) {
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
        return switch (Character.toLowerCase(unit)) {
            case 'y' -> value * 60L * 60L * 24L * 365L;
            case 'w' -> value * 60L * 60L * 24L * 7L;
            case 'd' -> value * 60L * 60L * 24L;
            case 'h' -> value * 60L * 60L;
            case 'm' -> value * 60L;
            case 's' -> value;
            default -> 0L;
        };
    }
}
