package commands.music;

import commands.CmdInterface;
import commands.Command;
import commands.music.utils.PlayerManager;
import core.Permission;
import entities.ReactEvent;
import net.dv8tion.jda.core.entities.Guild;

public class CmdStop implements CmdInterface {
    @Override
    public Permission permission() {
        return Permission.DJ;
    }

    @Override
    public void run(Command cmd) {
        Guild guild = cmd.getEvent().getGuild();
        if (PlayerManager.isIdle(guild)) return;
        PlayerManager.getTrackManager(guild).purgeQueue();
        PlayerManager.skip(guild);
        guild.getAudioManager().closeAudioConnection();
    }

    @Override
    public void emoteUpdate(ReactEvent reactEvent) {

    }

    @Override
    public String syntax(String prefix) {
        return prefix + "stop";
    }

    @Override
    public String description() {
        return "Stoppt die Wiedergabe";
    }
}
