package tools;

import java.util.ArrayList;

public class StringTools {
    public static String[] removeEmpty(String[] strs) {
        ArrayList<String> strList= new ArrayList<>();
        for(String str : strs) {
            if (str != null && !str.equals("")) {
                strList.add(str);
            }
        }
        return strList.toArray(new String[0]);
    }

    public static boolean isEmpty(String str) {
        return str == null || str.equals("");
    }

    public static boolean isBoolean(String str) {
        str = str.toLowerCase();
        return str.equals("true") || str.equals("false");
    }

    public static String spacer(int length, char fill) {
        String out = "";
        while (length > 0) {
            out += fill;
            length--;
        }
        return out;
    }
    public static String trim(String string, char fill, int size) {
        final int length = string.length();
        if(length < size) {
            for (int loop = 0; loop < size - length; loop++) {
                string += fill;
            }
        } else if(length > size) {
            string = string.substring(0, size);
        }
        return string;
    }
    public static String align(String string, char fill, int size, Enum align) {
        final int rawLength = string.length();
        switch (align.toString()) {
            default:
            case "LEFT":
                if(rawLength < size) {
                    for (int loop = 0; loop < size - rawLength; loop++) {
                        string += fill;
                    }
                } else if(rawLength > size) {
                    string = string.substring(0, size);
                }
                break;
            case "RIGHT":
                if(rawLength < size) {
                    String content = string;
                    string = "";
                    for (int loop = 0; loop < size - rawLength; loop++) {
                        string += fill;
                    }
                    string += content;
                } else if(rawLength > size) {
                    string = string.substring(0, size);
                }
                break;
            case "CENTER":
                if(rawLength < size) {
                    int front = (size - rawLength) / 2;
                    int back = size - (front + rawLength);
                    String content = string;
                    string = "";
                    for (int loop = 0; loop < front; loop++) {
                        string += fill;
                    }
                    string += content;
                    for (int loop = 0; loop < back; loop++) {
                        string += fill;
                    }
                } else if(rawLength > size) {
                    string = string.substring(0, size);
                }
                break;
        }
        return string;
    }
}
