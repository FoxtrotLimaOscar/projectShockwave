package tools;

import java.time.OffsetDateTime;

public class SubsToolkit {
    public static String boolSymbol(boolean bool) {
        if (bool) {
            return "✅";
        } else {
            return "❌";
        }
    }
    public static int lowerOf(int x, int y) {
        if(x > y) {
            return y;
        } else {
            return x;
        }
    }
    public static boolean getBoolFromString(String input) {
        return input.toLowerCase().equals("true");
    }
    public static String boolString(boolean bool) {
        if (bool) {
            return "true";
        } else {
            return "false";
        }
    }
    public static String humanizeTimeGer(OffsetDateTime odt) {
        return String.format("%02d", odt.getDayOfMonth()) + "." + String.format("%02d", odt.getMonthValue()) + "." +
                String.format("%04d", odt.getYear()) + " um " + String.format("%02d", odt.getHour()) + ":" + String.format("%02d", odt.getMinute());
    }
    public static String humanizeTimeLog(OffsetDateTime odt) {
        return String.format("%02d", odt.getDayOfMonth()) + "." + String.format("%02d", odt.getMonthValue()) + "." +
                String.format("%04d", odt.getYear()) + "-" + String.format("%02d", odt.getHour()) + ":" + String.format("%02d", odt.getMinute());
    }

    public static String limitString(String string, int length) {
        if (string.length() <= length) {
            return string;
        } else {
            return string.substring(0, length);
        }
    }
}
