package commands.music;

import commands.CmdInterface;
import commands.Command;
import commands.music.utils.PlayerManager;
import core.Permission;
import entities.ReactEvent;
import net.dv8tion.jda.core.entities.Guild;

public class CmdSkip implements CmdInterface {
    @Override
    public Permission permission() {
        return Permission.NONE;
    }

    @Override
    public void run(Command cmd) {
        Guild guild = cmd.getEvent().getGuild();
        if (PlayerManager.isIdle(guild)) return;
        PlayerManager.skip(guild);
    }

    @Override
    public void emoteUpdate(ReactEvent reactEvent) {

    }

    @Override
    public String syntax(String prefix) {
        return prefix + "skip";
    }

    @Override
    public String description() {
        return "Überspringt den aktuell gespielten Track";
    }
}
