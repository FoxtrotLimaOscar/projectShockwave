package commands.music.utils;

import com.sedmelluq.discord.lavaplayer.track.AudioTrackInfo;
import commands.CmdHandler;
import commands.ReactEvent;
import commands.ReactHandler;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.User;
import tools.MsgPresets;

public class QueuedMessage implements ReactHandler {
    private final User queueUser;
    private final AudioInfo audioInfo;
    public QueuedMessage(Message msg, AudioInfo info) {
        this.queueUser = msg.getAuthor();
        this.audioInfo = info;
        msg.addReaction("❌").queue();
        CmdHandler.reactionTickets.put(msg.getId(), this);
    }
    @Override
    public void emoteUpdate(ReactEvent reactEvent) {
        Message msg = reactEvent.getMessage();
        if (msg.getAuthor().equals(this.queueUser)) {
            AudioTrackInfo trackInfo = this.audioInfo.getTrack().getInfo();
            PlayerManager.getTrackManager(msg.getGuild()).dequeue(this.audioInfo);
            msg.clearReactions().queue();
            msg.editMessage(MsgPresets.musicDequeuedInfo(trackInfo.title, trackInfo.uri)).queue();
        }
    }
}
