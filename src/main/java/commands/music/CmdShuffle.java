package commands.music;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import commands.CmdInterface;
import commands.Command;
import commands.music.utils.PlayerManager;
import core.Permission;
import entities.ReactEvent;
import net.dv8tion.jda.core.entities.Guild;

public class CmdShuffle implements CmdInterface {
    @Override
    public Permission permission() {
        return Permission.DJ;
    }

    @Override
    public void run(Command cmd) {
        Guild guild = cmd.getEvent().getGuild();
        if (PlayerManager.isIdle(guild)) return;
        PlayerManager.getTrackManager(guild).shuffleQueue();
    }

    @Override
    public void emoteUpdate(ReactEvent reactEvent) {

    }

    @Override
    public String syntax(String prefix) {
        return prefix + "shuffle";
    }

    @Override
    public String description() {
        return "Mischt die Queue";
    }
}
