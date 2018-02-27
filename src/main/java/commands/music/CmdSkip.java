package commands.music;

import commands.CmdInterface;
import commands.Command;
import commands.music.utils.PlayerManager;
import commands.music.utils.TrackManager;
import core.Permission;
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
        if (cmd.hasParam(1) && cmd.getSlice(1).equals("playlist")) {
            PlayerManager.skipPlaylist(guild);
        } else {
            PlayerManager.skip(guild);
        }
    }

    @Override
    public String syntax(String prefix) {
        return prefix + "skip < playlist >";
    }

    @Override
    public String description() {
        return "Überspringt den aktuell gespielten Track";
    }

    @Override
    public String details() {
        return description();
    }
}
