package core;

import com.sun.deploy.util.StringUtils;
import commands.CmdHandler;
import commands.information.*;
import commands.music.*;
import commands.management.*;
import listener.msgListener;
import listener.reactListener;
import listener.readyListener;
import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.OnlineStatus;
import core.database.Database;
import tools.SubsToolkit;

import javax.security.auth.login.LoginException;
import java.io.FileNotFoundException;

public class Main {

    private static JDABuilder builder;

    public static void main(String[] arguments) throws InterruptedException, FileNotFoundException {


        BotConfig.load();
        BotConfig.securityCheck();
        Database.load();
        //System.out.println(SubsToolkit.boolString(Database.check()));


        //Builder setup
        builder = new JDABuilder(AccountType.BOT);
        builder.setToken(BotConfig.getToken());
        builder.setAutoReconnect(true);
        builder.setStatus(OnlineStatus.ONLINE);
        builder.setGame(Database.getBot().getGame());
        addListeners();
        addCommands();

        try {
            JDA jda = builder.buildBlocking();
        } catch (LoginException e) {
            System.out.println("Invalid token! Change it in the config.txt file!");
            System.exit(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }


    private static void addCommands() {
        CmdHandler.commands.put("ping", new CmdPing());
        CmdHandler.commands.put("shutdown", new CmdShutdown());
        CmdHandler.commands.put("help", new CmdHelp());
        CmdHandler.commands.put("settings", new CmdSettings());
        CmdHandler.commands.put("play", new CmdPlay());
        CmdHandler.commands.put("skip", new CmdSkip());
        CmdHandler.commands.put("stop", new CmdStop());
        CmdHandler.commands.put("dev", new CmdDev());
        CmdHandler.commands.put("search", new CmdSearch());
        CmdHandler.commands.put("map", new CmdMap());
        CmdHandler.commands.put("setguild", new CmdSetGuild());
        CmdHandler.commands.put("setuser", new CmdSetUser());
        CmdHandler.commands.put("setbot", new CmdSetBot());
    }


    private static void addListeners() {
        builder.addEventListener(new readyListener());
        builder.addEventListener(new reactListener());
        builder.addEventListener(new msgListener());
    }
}
