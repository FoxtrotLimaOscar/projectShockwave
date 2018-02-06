package commands.information;

import commands.CmdHandler;
import commands.CmdInterface;
import commands.Command;
import core.Permission;
import core.database.Database;
import tools.MsgPresets;

import java.util.concurrent.TimeUnit;

public class CmdHelp implements CmdInterface {
    @Override
    public Permission permission() {
        return Permission.NONE;
    }

    @Override
    public void run(Command cmd) {
        if (cmd.getSlices().length  == 2) {
            String cmdName = cmd.getSlice(1);
            cmd.getEvent().getChannel().sendMessage(CmdHandler.getHelp(cmdName, Database.getGuild(cmd.getEvent().getGuild()).getPrefix())).queue(message -> message.delete().queueAfter(1, TimeUnit.MINUTES));
        } else {
            cmd.getEvent().getChannel().sendMessage(MsgPresets.sendHelp()).queue(message -> message.delete().queueAfter(1, TimeUnit.MINUTES));
            cmd.getEvent().getAuthor().openPrivateChannel().complete().sendMessage(MsgPresets.helpAll()).queue();
        }
    }

    @Override
    public String syntax(String prefix) {
        return prefix + "help < leer | Befehlsname >";
    }

    @Override
    public String description() {
        return "Gibt dir Infos über einen oder alle Befehle";
    }
}
