package tools;

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
}
