package settings;

import tools.BooleanTools;
import tools.StringTools;
import tools.SubsConfig.Config;
import tools.SubsConfig.ConfigItem;
import tools.SubsConfig.Defaults;
import tools.SubsToolkit;

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class BotConfig {

    private static File file = new File("config.txt");
    private static Config config = new Config(file, getDefaults());

    public static boolean securityCheck(){
        String error = null;
        if (config.checkSingle(keys.STARTOPTIONS_AUTO)) {
            error = keys.STARTOPTIONS_NAMES.toString() + "must have a value";
        }
        if (config.checkArray(keys.STARTOPTIONS_NAMES)) {
            error = keys.STARTOPTIONS_NAMES.toString() + " must contain at least one value";
        }
        if (config.checkArray(keys.STARTOPTIONS_TOKENS)) {
            error = keys.STARTOPTIONS_TOKENS.toString() + " must contain at least one value";
        }
        if (config.item(keys.STARTOPTIONS_TOKENS).getArray().size() != config.item(keys.STARTOPTIONS_NAMES).getArray().size()) {
            error = keys.STARTOPTIONS_TOKENS.toString() + " and " + keys.STARTOPTIONS_NAMES + "must have the same length";
        }
        if (config.checkArray(keys.BOTOWNERIDS)) {
            error = keys.BOTOWNERIDS.toString() + " must contain at least one value";
        }

        if (error != null) {
            System.out.println("Error with config.txt" + error);
            System.exit(1);
            return false;
        }
        return true;
    }

    public static String getToken() {
        int selection = 0;
        ArrayList<String> names = getStartoptionsNames();
        if(!getStartoptionsAuto()) {
            Scanner scanner = new Scanner(System.in);
            boolean accept = false;
            System.out.print("Choose the token the BOT will start with\n");
            while (!accept) {
                for(int index = 0; index < names.size(); index++) {
                    System.out.print((index + 1) + ": \"" + names.get(index) + "\"\n");
                }
                selection = scanner.nextInt() - 1;
                if(selection >= 0 && selection < names.size()) {
                    accept = true;
                } else {
                    System.out.print(StringTools.spacer(20, ' ') + "Incorrect selection, try again\n");
                }
            }
        }
        System.out.println("The BOT will start as \"" + names.get(selection) + "\".\n");
        return getStartoptionsTokens().get(selection);
    }

    private static boolean getStartoptionsAuto() {
        return SubsToolkit.getBoolFromString(config.item(keys.STARTOPTIONS_AUTO).getLine());
    }
    private static ArrayList<String> getStartoptionsTokens() {
        return config.item(keys.STARTOPTIONS_TOKENS).getArray();
    }
    private static ArrayList<String> getStartoptionsNames() {
        return config.item(keys.STARTOPTIONS_NAMES).getArray();
    }
    public static ArrayList<String> getBotownerIds() {
        return config.item(keys.BOTOWNERIDS).getArray();
    }
    public static boolean getDefaultForcedMC() {
        return BooleanTools.getBoolFromString(config.item(keys.DEFAULT_FORCEDMC).getSingle());
    }
    public static String getDefaultPrefix() {
        return config.item(keys.DEFAULT_PREFIX).getSingle();
    }
    public static int getDefaultVolume() {
        return Integer.parseInt(config.item(keys.DEFAULT_VOLUME).getSingle());
    }
    public static boolean getDefaultBootMessage() {
        return BooleanTools.getBoolFromString(config.item(keys.DEFAULT_SENDBOOTMESSAGE).getSingle());
    }
    public static int getPlaylistLimit() {
        return Integer.parseInt(config.item(keys.DEFAULT_PLAYLISTLIMIT).getSingle());
    }
    public static String getLogchannelId() {
        return config.item(keys.LOGCHANNELID).getSingle();
    }


    private static Defaults getDefaults() {
        Defaults defaults = new Defaults();
        defaults.add(new ConfigItem(keys.STARTOPTIONS_AUTO, "false"));
        defaults.add(new ConfigItem(keys.STARTOPTIONS_TOKENS, "exampletoken1;"));
        defaults.add(new ConfigItem(keys.STARTOPTIONS_NAMES, "examplename1;"));
        defaults.add(new ConfigItem(keys.BOTOWNERIDS, "exampleuserid1;"));
        defaults.add(new ConfigItem(keys.LOGCHANNELID, ""));
        defaults.addFreeLine();
        defaults.add(new ConfigItem(keys.DEFAULT_FORCEDMC, "false"));
        defaults.add(new ConfigItem(keys.DEFAULT_PREFIX, "#"));
        defaults.add(new ConfigItem(keys.DEFAULT_VOLUME, "100"));
        defaults.add(new ConfigItem(keys.DEFAULT_SENDBOOTMESSAGE, "true"));
        defaults.add(new ConfigItem(keys.DEFAULT_PLAYLISTLIMIT, "50"));
        return defaults;
    }

    private enum keys {
        STARTOPTIONS_AUTO, STARTOPTIONS_TOKENS, STARTOPTIONS_NAMES, BOTOWNERIDS, LOGCHANNELID, DEFAULT_FORCEDMC, DEFAULT_PREFIX,
        DEFAULT_VOLUME, DEFAULT_SENDBOOTMESSAGE, DEFAULT_PLAYLISTLIMIT;
    }
}
