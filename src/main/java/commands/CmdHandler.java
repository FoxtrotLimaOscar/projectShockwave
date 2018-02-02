package commands;

import core.Permission;
import entities.ReactEvent;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import settings.BotConfig;
import tools.MsgPresets;

import java.util.HashMap;

public class CmdHandler {
    public static HashMap<String, CmdInterface>  commands = new HashMap<>();
    public static HashMap<String, CmdInterface> reactionTickets = new HashMap<>();

    public static void handleCommand(Command cmd) {

        if (commands.containsKey(cmd.getInvoke().toLowerCase())) {

            cmd.getEvent().getMessage().delete().queue();

            CmdInterface cmdInterface = commands.get(cmd.getInvoke());
            Permission permission = cmdInterface.permission();
            MessageReceivedEvent event = cmd.getEvent();
            boolean permissionGranted = Permission.hasPermission(event.getMember(), permission);

            if(permissionGranted) {
                cmdInterface.run(cmd);
            } else {
                event.getTextChannel().sendMessage(MsgPresets.noPermission(permission, event.getMember())).queue();
            }

            try {
                event.getJDA().getTextChannelById(BotConfig.getLogchannelId()).sendMessage(MsgPresets.logCmd(event, permissionGranted, permission)).queue();
            } catch (IllegalArgumentException e) {
                //DO NOTHING
            }

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
}
