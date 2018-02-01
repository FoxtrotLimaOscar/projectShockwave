package commands.management;

import commands.CmdHandler;
import commands.CmdInterface;
import commands.Command;
import core.Permission;
import entities.ReactEvent;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageEmbed;
import tools.MsgPresets;

public class CmdSettings implements CmdInterface {

    private int page = 0;
    private Member member;

    @Override
    public Permission permission() {
        return Permission.NONE;
    }

    @Override
    public void run(Command cmd) {
        this.member = cmd.getEvent().getMember();
        if (cmd.getSlices().length > 1) {
            page = Integer.getInteger(cmd.getSlice(1));
            if (page > 3 || page < 0) {
                page = 0;
            }
        }
        Message msg = cmd.getEvent().getTextChannel().sendMessage(getPageContent()).complete();
        msg.addReaction("⬅").queue();
        msg.addReaction("➡").queue();
        msg.addReaction("❌").queue();
        CmdHandler.reactionTickets.put(msg.getId(), this);
    }

    @Override
    public void emoteUpdate(ReactEvent reactEvent) {
        boolean sameUser = this.member.getUser().equals(reactEvent.getUser());
        if (sameUser && reactEvent.getEmote().equals("⬅")) {
            page--;
            CmdHandler.reactionTickets.put(reactEvent.getMessageID(), this);
            correctPage();
            reactEvent.getMessage().editMessage(getPageContent()).queue();
        } else if (sameUser && reactEvent.getEmote().equals("➡")) {
            page++;
            CmdHandler.reactionTickets.put(reactEvent.getMessageID(), this);
            correctPage();
            reactEvent.getMessage().editMessage(getPageContent()).queue();
        } else if (sameUser && reactEvent.getEmote().equals("❌")) {
            reactEvent.getMessage().delete().queue();
        }
    }

    @Override
    public String syntax(String prefix) {
        return prefix + "settings < nichts | seitenzahl >";
    }

    @Override
    public String description() {
        return "Übersicht aller Einstellungen";
    }

    private MessageEmbed getPageContent() {
        switch (page) {
            case 1:
                return MsgPresets.settingsPageUser(this.member.getUser());
            case 2:
                return MsgPresets.settingsPageGuild(this.member.getGuild());
            case 3:
                return MsgPresets.settingsPageBot();
            default:
                return MsgPresets.settingsPageFront();
        }
    }

    private void correctPage() {
        if (page < 0) {
            page = 3;
        } else if (page > 3) {
            page = 0;
        }
    }
}
