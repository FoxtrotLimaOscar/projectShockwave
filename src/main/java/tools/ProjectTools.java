package tools;

public class ProjectTools {
    public static String getThumbnail(String raw) {
        return "https://img.youtube.com/vi/" + raw.substring(32, 43) + "/mqdefault.jpg";
    }
}
