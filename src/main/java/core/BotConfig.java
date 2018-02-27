package core;

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


    private static Config config;

    public static void load() {
        File file = new File("config.txt");
        config = new Config(file, getDefaults());
    }
    public static void securityCheck(){
        String error = null;
        if (!config.checkSingle(STARTOPTIONS_AUTO)) {
            error = "\"" + STARTOPTIONS_NAMES + "\" must have a value";
        }
        if (!config.checkArray(STARTOPTIONS_NAMES)) {
            error = "\"" + STARTOPTIONS_NAMES + "\" must contain at least one value";
        }
        if (!config.checkArray(STARTOPTIONS_TOKENS)) {
            error = "\"" + STARTOPTIONS_TOKENS + "\" must contain at least one value";
        }
        if (config.item(STARTOPTIONS_TOKENS).getArray().size() != config.item(STARTOPTIONS_NAMES).getArray().size()) {
            error = "\"" + STARTOPTIONS_TOKENS + "\" and \"" + STARTOPTIONS_NAMES + "\" must have the amount of values";
        }
        if (!config.checkArray(BOTOWNERIDS)) {
            error = "\"" + BOTOWNERIDS + "\"" + " must contain at least one value";
        }

        if (error != null) {
            System.out.println("Flaw inside of config.txt:\n" + error);
            System.exit(1);
        }
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
        return SubsToolkit.getBoolFromString(config.item(STARTOPTIONS_AUTO).getSingle());
    }
    private static ArrayList<String> getStartoptionsTokens() {
        return config.item(STARTOPTIONS_TOKENS).getArray();
    }
    private static ArrayList<String> getStartoptionsNames() {
        return config.item(STARTOPTIONS_NAMES).getArray();
    }
    public static ArrayList<String> getBotownerIds() {
        return config.item(BOTOWNERIDS).getArray();
    }
    public static boolean getDefaultForcedMC() {
        return BooleanTools.getBoolFromString(config.item(DEFAULT_FORCEDMC).getSingle());
    }
    public static String getDefaultPrefix() {
        return config.item(DEFAULT_PREFIX).getSingle();
    }
    public static int getDefaultVolume() {
        return Integer.parseInt(config.item(DEFAULT_VOLUME).getSingle());
    }
    public static boolean getDefaultBootMessage() {
        return BooleanTools.getBoolFromString(config.item(DEFAULT_SENDBOOTMESSAGE).getSingle());
    }
    public static int getPlaylistLimit() {
        return Integer.parseInt(config.item(DEFAULT_PLAYLISTLIMIT).getSingle());
    }
    public static String getLogchannelId() {
        return config.item(LOGCHANNELID).getSingle();
    }


    private static Defaults getDefaults() {
        Defaults defaults = new Defaults();
        defaults.addDescription("Single Example");
        defaults.addDescription("EXAMPLE_SINGEL=singleitem");
        defaults.addDescription("Array Example");
        defaults.addDescription("EXAMPLE_ARRAY=firstitem;seconditem;thirditem;");
        defaults.add(new ConfigItem(STARTOPTIONS_AUTO, "false"));
        defaults.add(new ConfigItem(STARTOPTIONS_TOKENS, "exampletoken1;"));
        defaults.add(new ConfigItem(STARTOPTIONS_NAMES, "examplename1;"));
        defaults.add(new ConfigItem(BOTOWNERIDS, "exampleuserid1;"));
        defaults.add(new ConfigItem(LOGCHANNELID, ""));
        defaults.addFreeLine();
        defaults.add(new ConfigItem(DEFAULT_FORCEDMC, "false"));
        defaults.add(new ConfigItem(DEFAULT_PREFIX, "#"));
        defaults.add(new ConfigItem(DEFAULT_VOLUME, "100"));
        defaults.add(new ConfigItem(DEFAULT_SENDBOOTMESSAGE, "true"));
        defaults.add(new ConfigItem(DEFAULT_PLAYLISTLIMIT, "50"));
        return defaults;
    }

    private static final String STARTOPTIONS_AUTO = "STARTOPTIONS_AUTO";
    private static final String STARTOPTIONS_TOKENS = "STARTOPTIONS_TOKENS";
    private static final String STARTOPTIONS_NAMES = "STARTOPTIONS_NAMES";
    private static final String BOTOWNERIDS = "BOTOWNERIDS";
    private static final String LOGCHANNELID = "LOGCHANNELID";
    private static final String DEFAULT_FORCEDMC = "DEFAULT_FORCEDMC";
    private static final String DEFAULT_PREFIX = "DEFAULT_PREFIX";
    private static final String DEFAULT_VOLUME = "DEFAULT_VOLUME";
    private static final String DEFAULT_SENDBOOTMESSAGE = "DEFAULT_SENDBOOTMESSAGE";
    private static final String DEFAULT_PLAYLISTLIMIT = "DEFAULT_PLAYLISTLIMIT";
}
