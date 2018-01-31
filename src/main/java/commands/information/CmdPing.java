package commands.information;

import commands.CmdInterface;
import commands.Command;
import core.Permission;
import entities.ReactEvent;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageEmbed;
import tools.MsgPresets;

public class CmdPing implements CmdInterface {

    @Override
    public Permission permission() {
        return Permission.NONE;
    }

    @Override
    public void run(Command cmd) {
        cmd.getEvent().getTextChannel().sendMessage(MsgPresets.Ping(cmd.getEvent().getJDA())).queue();
    }

    @Override
    public void emoteUpdate(ReactEvent reactEvent) {

    }

    @Override
    public String syntax(String prefix) {
        return prefix + "ping";
    }

    @Override
    public String description() {
        return "Gibt die aktuelle Latenzzeit an";
    }
}
