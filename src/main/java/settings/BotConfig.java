package settings;

import core.Main;
import net.dv8tion.jda.core.entities.Channel;
import tools.BooleanTools;
import tools.Configuration;
import tools.StringTools;

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class BotConfig {

    private static  String tokenTakeFirst;
    private static String[] tokenCodes = new String[3];
    private static String[] tokenNames = new String[3];
    private static String[] botOperatorIds = new String[3];
    private static String defaultForcedMC;
    private static String defaultPrefix;
    private static String defaultVolume;
    private static String defaultBootMessage;
    private static String playlistLimit;
    private static String logchannelId;
    private static String logAll;

    public static void loadBotSettings() {
        //Create default properties
        ArrayList <String> defaults = new ArrayList<String>();
        defaults.add("token-take-first=false");
        defaults.add("token-code-1=________________________.______.___________________________");
        defaults.add("token-name-1=Default name");
        defaults.add("token-code-2=");
        defaults.add("token-name-2=");
        defaults.add("token-code-3=");
        defaults.add("token-name-3=");
        defaults.add("");
        defaults.add("botoperator-id-1=");
        defaults.add("botoperator-id-2=");
        defaults.add("botoperator-id-3=");
        defaults.add("");
        defaults.add("default-forcedmusicchannel=false");
        defaults.add("default-prefix=#");
        defaults.add("default-volume=100");
        defaults.add("default-bootmessage=true");
        defaults.add("");
        defaults.add("playlist-limit=-1");
        defaults.add("log-channel-id=");

        File file = new File("config.txt");
        Configuration config = new Configuration(file, defaults);

        tokenTakeFirst = config.get("token-take-first");
        tokenCodes[0] = config.get("token-code-1");
        tokenNames[0] = config.get("token-name-1");
        tokenCodes[1] = config.get("token-code-2");
        tokenNames[1] = config.get("token-name-2");
        tokenCodes[2] = config.get("token-code-3");
        tokenNames[2] = config.get("token-name-3");
        botOperatorIds[0] = config.get("botoperator-id-1");
        botOperatorIds[1] = config.get("botoperator-id-2");
        botOperatorIds[2] = config.get("botoperator-id-3");
        defaultForcedMC = config.get("default-forcedmusicchannel");
        defaultPrefix = config.get("default-prefix");
        defaultVolume = config.get("default-volume");
        defaultBootMessage = config.get("default-bootmessage");
        playlistLimit = config.get("playlist-limit");
        logchannelId = config.get("log-channel-id");

        securityCheck();
    }

    private static void securityCheck(){
        if(!(doRequieredExist() && rightParameters())) {
            System.exit(1);
        }
    }

    private static boolean doRequieredExist() {
        if (!StringTools.isBoolean(tokenTakeFirst)
                && StringTools.isEmpty(tokenCodes[0])
                && StringTools.isEmpty(tokenNames[0])
                && StringTools.isEmpty(botOperatorIds[0])
                && !StringTools.isBoolean(defaultForcedMC)
                && StringTools.isEmpty(defaultPrefix)
                && StringTools.isEmpty(defaultVolume)
                && StringTools.isEmpty(playlistLimit)) {
            System.out.println("Invalid or missing arguments in config.txt file");
            return false;
        }
        return true;
    }

    private static boolean rightParameters() {
        if(getDefaultVolume() > 200 || getDefaultVolume() < 0) {
            System.out.println("The default-volume should be between 0 and 200");
            return false;
        } else if (getPlaylistLimit() < -1) {
            System.out.println("The playlist-limit must be -1 or more");
        }
        return true;
    }

    public static String getToken() {
        int selection = 0;
        if(!BooleanTools.getBoolFromString(tokenTakeFirst)) {
            Scanner scanner = new Scanner(System.in);
            String[] readableNames = StringTools.removeEmpty(tokenNames);
            boolean accept = false;
            System.out.print("Choose the token the BOT will start with\n");
            while (!accept) {
                for(int index = 0; index < readableNames.length; index++) {
                    System.out.print((index + 1) + ": \"" + readableNames[index] + "\"\n");
                }
                selection = scanner.nextInt() - 1;
                if(selection > -1 && selection < readableNames.length) {
                    accept = true;
                } else {
                    System.out.print(StringTools.spacer(20, ' ') + "Incorrect selection, try again\n");
                }
            }
        }
        System.out.println("The BOT will start as \"" + tokenNames[selection] + "\".\n");
        return tokenCodes[selection];
    }

    public static String[] getBotOperatorIds() {
        return botOperatorIds;
    }

    public static boolean getDefaultForcedMC() {
        return BooleanTools.getBoolFromString(defaultForcedMC);
    }

    public static String getDefaultPrefix() {
        return defaultPrefix;
    }

    public static int getDefaultVolume() {
        return Integer.parseInt(defaultVolume);
    }

    public static boolean getDefaultBootMessage() {
        return BooleanTools.getBoolFromString(defaultBootMessage);
    }

    public static int getPlaylistLimit() {
        return Integer.parseInt(playlistLimit);
    }

    public static String getLogchannelId() {
        return logchannelId;
    }
}
