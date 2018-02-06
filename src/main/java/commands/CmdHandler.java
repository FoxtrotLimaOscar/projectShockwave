package commands;

import core.Permission;
import core.Statics;
import entities.ReactEvent;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import core.BotConfig;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import tools.MsgPresets;
import tools.ProjectTools;
import tools.SubsToolkit;

import java.io.*;
import java.time.OffsetDateTime;
import java.util.Date;
import java.util.HashMap;

public class CmdHandler {
    public static HashMap<String, CmdInterface>  commands = new HashMap<>();
    public static HashMap<String, ReactHandler> reactionTickets = new HashMap<>();

    public static void handleCommand(Command cmd) {

        if (commands.containsKey(cmd.getInvoke().toLowerCase())) {

            cmd.getEvent().getMessage().delete().queue();

            CmdInterface cmdInterface = commands.get(cmd.getInvoke().toLowerCase());
            Permission permission = cmdInterface.permission();
            GuildMessageReceivedEvent event = cmd.getEvent();
            boolean permissionGranted = Permission.hasPermission(event.getMember(), permission);

            if(permissionGranted) {
                cmdInterface.run(cmd);
            } else {
                event.getChannel().sendMessage(MsgPresets.noPermission(permission, event.getMember())).queue();
            }

            logCmd(event, permissionGranted, permission);
        }
    }

    public static MessageEmbed getHelp(String invoke, String prefix) {
        if (commands.containsKey(invoke)) {
            CmdInterface cmdInterface = commands.get(invoke);
            return MsgPresets.cmdHelp(invoke, cmdInterface.description(), cmdInterface.permission(), cmdInterface.syntax(prefix));
        }
        return MsgPresets.helpNoSuchCmd();
    }

    public static void fireReactionTicket(ReactEvent reactEvent) {
        String ID = reactEvent.getMessageID();
        if (reactionTickets.containsKey(ID) && !reactEvent.getUser().isBot()) {
            reactionTickets.remove(ID).emoteUpdate(reactEvent);
        }
    }

    private static void logCmd(GuildMessageReceivedEvent event, boolean permissionGranted, Permission permission) {
        String logChannelid = BotConfig.getLogchannelId();
        String permString;
        User user = event.getAuthor();
        Guild guild = event.getGuild();
        String userString = user.getName() + "(#" + user.getDiscriminator() + ")/" + user.getId();
        String guildString = guild.getName() + "/" + guild.getId();
        String timeString = SubsToolkit.humanizeTimeLog(event.getMessage().getCreationTime());
        if (permissionGranted) {
            permString = "GRNTD";
        } else {
            permString = "RJCTD";
        }
        if (ProjectTools.isChannelID(logChannelid)) {
            try {
                event.getJDA().getTextChannelById(BotConfig.getLogchannelId()).sendMessage(MsgPresets.logCmd(event, permissionGranted, permission)).queue();
                BufferedWriter writer = new BufferedWriter(new FileWriter(new File("cmdlog.txt"), true));
                writer.write(event.getMessage().getContentDisplay() + " - [ " + permString + " | " + timeString + " | " + userString + " | " + guildString + " ]");
                writer.newLine();
                writer.close();
            } catch (IllegalArgumentException e) {
                System.out.println("Please enter a valid Log-Channel-Id or leave the field empty");
                System.exit(1);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}
