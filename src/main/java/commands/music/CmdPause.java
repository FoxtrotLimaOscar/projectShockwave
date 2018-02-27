package commands.music;

import commands.CmdInterface;
import commands.Command;
import commands.music.utils.PlayerManager;
import commands.music.utils.TrackManager;
import core.Permission;
import net.dv8tion.jda.core.entities.Guild;
import tools.MsgPresets;

import java.util.concurrent.TimeUnit;

public class CmdPause implements CmdInterface {
    @Override
    public Permission permission() {
        return Permission.DJ;
    }

    @Override
    public void run(Command cmd) {
        Guild guild = cmd.getEvent().getGuild();
        TrackManager trackManager = PlayerManager.getTrackManager(guild);
        if (PlayerManager.isIdle(guild)) return;
        if (trackManager.isPaused()) {
            trackManager.resume();
            cmd.getEvent().getChannel().sendMessage(MsgPresets.musicResumed()).queue(m -> m.delete().queueAfter(10, TimeUnit.SECONDS));
        } else {
            trackManager.pause();
            cmd.getEvent().getChannel().sendMessage(MsgPresets.musicPaused()).queue(m -> m.delete().queueAfter(10, TimeUnit.SECONDS));
        }
    }

    @Override
    public String syntax(String prefix) {
        return prefix + "pause";
    }

    @Override
    public String description() {
        return "Pausiert die Wiedergabe oder ";
    }

    @Override
    public String details() {
        return "Pausiert die Wiedergabe";
    }
}
