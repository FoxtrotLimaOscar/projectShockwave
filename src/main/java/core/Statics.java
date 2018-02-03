package core;

import java.awt.*;
import java.util.Date;

public class Statics {

    // The number of the current version
    // first number             big changes
    // second number       small changes
    // third number            bugfixes
    public final String VERSION_NUMBER = "1.4.2";

    // The name of the current version (second and third number)
    public final String VERSION_NAME = "justChillin";

    //The Timecode the bot started running
    public static Date BOT_LASTRESTART;


    //Database accesses
    public static int databaseAccesses = 0;

    // All static YouTube playlists
    public static final String YTPLY_THEFATRAT = "https://www.youtube.com/playlist?list=PLrKKVx01xqZYfm2MicuFB5SIpw3KfWaAH";
    public static final String YTPLY_EIGHTIES = "https://www.youtube.com/playlist?list=PLCD0445C57F2B7F41";
    public static final String YTPLY_NINETIES = "https://www.youtube.com/playlist?list=PL7DA3D097D6FDBC02";
    public static final String YTPLY_DOCTORWHO = "https://www.youtube.com/playlist?list=PLpX6JWPXkZ3R7VKjgPRZPt-kCFQ31Ipuz";

    public static final String SYMBOL_PERMISSION_BOT = "\uD83D\uDD34";
    public static final String SYMBOL_PERMISSION_DJ = "\uD83C\uDFA7";
    public static final String SYMBOL_PERMISSION_ADMIN = "\uD83D\uDD35";
    public static final String SYMBOL_PERMISSION_NONE = "\uD83D\uDEAB";
    public static final Color COLOR_PERMISSION_BOT = Color.RED;
    public static final Color COLOR_PERMISSION_DJ = Color.MAGENTA;
    public static final Color COLOR_PERMISSION_ADMIN = Color.ORANGE;
    public static final Color COLOR_PERMISSION_NONE = Color.LIGHT_GRAY;
}
