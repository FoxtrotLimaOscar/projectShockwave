package tools;

public class ProjectTools {
    public static String getThumbnail(String raw) {
        return "https://img.youtube.com/vi/" + raw.substring(32, 43) + "/mqdefault.jpg";
    }
    public static boolean isChannelID(String id) {
        return id.length() == 18 && id.chars().allMatch(Character::isDigit);
    }
    public static int convertVolume(int vol, boolean toLP) {
        if (toLP) {
            return vol/10;
        } else {
            return vol*10;
        }
    }
}
