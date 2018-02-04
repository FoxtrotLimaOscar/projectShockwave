package commands.management;

import commands.CmdHandler;
import commands.CmdInterface;
import commands.Command;
import commands.ReactHandler;
import core.Permission;
import core.database.groups.GSettings;
import entities.ReactEvent;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.User;
import core.database.Database;
import tools.MsgPresets;

public class CmdShutdown implements CmdInterface, ReactHandler {
    private User user;

    @Override
    public Permission permission() {
        return Permission.BOT;
    }

    @Override
    public void run(Command cmd) {
        Message msg = cmd.getEvent().getChannel().sendMessage(MsgPresets.ShutdownAccept()).complete();
        msg.addReaction("✅").queue();
        msg.addReaction("❌").queue();
        CmdHandler.reactionTickets.put(msg.getId(), this);
        user = cmd.getEvent().getAuthor();
    }

    @Override
    public void emoteUpdate(ReactEvent reactEvent) {
        boolean sameUser = this.user.equals(reactEvent.getUser());
        if (reactEvent.getEmote().equals("✅") && sameUser) {
            Message msg = reactEvent.getMessage();
            msg.editMessage(MsgPresets.Shutdown()).complete();
            msg.clearReactions().complete();
            shutdownWarn(reactEvent.getMessage().getGuild());
            System.exit(1);
        } else if (reactEvent.getEmote().equals("❌") && sameUser) {
            Message msg = reactEvent.getMessage();
            msg.delete().queue();
        } else {
            CmdHandler.reactionTickets.put(reactEvent.getMessageID(), this);
        }
    }

    @Override
    public String syntax(String prefix) {
        return prefix + "shutdown";
    }

    @Override
    public String description() {
        return "Fährt den Bot herunter";
    }

    private static void shutdownWarn(Guild exception) {
        for (Guild index : exception.getJDA().getGuilds()) {
            GSettings guildSettings = Database.getGuild(index);
            if (guildSettings.getBootMessage() && !index.getId().equals(exception.getId())) guildSettings.getBotChannel().sendMessage(MsgPresets.Shutdown()).complete();
        }
    }
}
