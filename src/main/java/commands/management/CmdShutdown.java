package commands.management;

import commands.CmdHandler;
import commands.CmdInterface;
import commands.Command;
import core.Permission;
import entities.ReactEvent;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.User;
import tools.MsgPresets;

public class CmdShutdown implements CmdInterface {
    private User user;

    @Override
    public Permission permission() {
        return Permission.BOT;
    }

    @Override
    public void run(Command cmd) {
        Message msg = cmd.getEvent().getTextChannel().sendMessage(MsgPresets.ShutdownAccept()).complete();
        msg.addReaction("✅").queue();
        msg.addReaction("❌").queue();
        CmdHandler.reactionTickets.put(msg.getId(), this);
        user = cmd.getEvent().getAuthor();
    }

    @Override
    public void emoteUpdate(ReactEvent reactEvent) {
        boolean sameUser = user.equals(reactEvent.getUser());
        if (reactEvent.getEmote().equals("✅") && sameUser) {
            Message msg = reactEvent.getMessage();
            msg.editMessage(MsgPresets.Shutdown()).complete();
            msg.clearReactions().complete();
            System.exit(1);
        }
        if (reactEvent.getEmote().equals("❌") && sameUser) {
            Message msg = reactEvent.getMessage();
            msg.delete().queue();
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
}
