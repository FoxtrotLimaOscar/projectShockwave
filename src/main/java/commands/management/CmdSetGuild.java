package commands.management;

import commands.CmdInterface;
import commands.Command;
import core.Permission;
import core.database.Database;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;

public class CmdSetGuild implements CmdInterface {
    @Override
    public Permission permission() {
        return Permission.GUILD;
    }

    @Override
    public void run(Command cmd) {
        if (!cmd.hasParam(1)) return;
        switch (cmd.getSlice(1)) {
            case "prefix":
                if (cmd.hasParam(2)) {
                }
                break;
        }
    }

    @Override
    public String syntax(String prefix) {
        return null;
    }

    @Override
    public String description() {
        return null;
    }

    @Override
    public String details() {
        return null;
    }
}
