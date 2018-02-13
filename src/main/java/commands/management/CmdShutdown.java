package commands.management;

import commands.*;
import core.Permission;
import core.database.groups.GSettings;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.User;
import core.database.Database;
import tools.MsgPresets;

public class CmdShutdown implements CmdInterface, ReactHandler {
    private User user;
    private String reason = null;

    @Override
    public Permission permission() {
        return Permission.BOT;
    }

    @Override
    public void run(Command cmd) {
        Message msg = cmd.getEvent().getChannel().sendMessage(MsgPresets.ShutdownAccept()).complete();
        msg.addReaction("✅").queue();
        msg.addReaction("❌").queue();
        CmdHandler.queueReactionTicket(new MsgLink(msg), this);
        user = cmd.getEvent().getAuthor();
        if (cmd.hasParam(1)) {
            this.reason = cmd.getRaw(1);
        }
    }

    @Override
    public void emoteUpdate(ReactEvent reactEvent) {
        boolean sameUser = this.user.equals(reactEvent.getUser());
        if (reactEvent.getEmote().equals("✅") && sameUser) {
            Message msg = reactEvent.getMessage();
            msg.editMessage(MsgPresets.shutdownFinal(this.reason)).complete();
            msg.clearReactions().complete();
            shutdownWarn(reactEvent.getMessage().getGuild(), this.reason);
            System.exit(1);
        } else if (reactEvent.getEmote().equals("❌") && sameUser) {
            Message msg = reactEvent.getMessage();
            msg.delete().queue();
        } else {
            CmdHandler.queueReactionTicket(reactEvent.getMessageLink(), this);
        }
    }

    @Override
    public String syntax(String prefix) {
        return prefix + "shutdownFinal";
    }

    @Override
    public String description() {
        return "Fährt den Bot herunter";
    }

    @Override
    public String details() {
        return description();
    }

    private static void shutdownWarn(Guild exception, String reason) {
        for (Guild index : exception.getJDA().getGuilds()) {
            GSettings guildSettings = Database.getGuild(index);
            if (guildSettings.getBootMessage() && !index.getId().equals(exception.getId())) guildSettings.getBotChannel().sendMessage(MsgPresets.shutdownFinal(reason)).complete();
        }
    }
}
