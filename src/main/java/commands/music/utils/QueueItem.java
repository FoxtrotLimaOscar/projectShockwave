package commands.music.utils;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import commands.CmdHandler;
import commands.MsgLink;
import commands.ReactEvent;
import commands.ReactHandler;
import core.database.Database;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.TextChannel;
import tools.MsgPresets;

import java.util.LinkedList;
import java.util.concurrent.TimeUnit;

public class QueueItem implements ReactHandler {
    private String playlistTitle;
    private Member member;
    private MsgLink link;
    private LinkedList<AudioTrack> tracks = new LinkedList<>();

    public QueueItem(AudioTrack track, Member member) {
        this.tracks.add(track);
        this.member = member;
        this.playlistTitle = null;
        queued();
    }
    public QueueItem (LinkedList<AudioTrack> tracks, Member member, String playlistTitle) {
        this.tracks = tracks;
        this.member = member;
        if (tracks.size() > 1) {
            this.playlistTitle = playlistTitle;
        }
        queued();
    }

    public AudioTrack receiveTrack() {
        if (tracks.isEmpty()) {
            this.link.getMessage().delete().queue();
            return null;
        } else {
            playing();
            return tracks.poll();
        }
    }

    public AudioTrack currentTrack() {
        return this.tracks.peek();
    }

    public Member getMember() {
        return this.member;
    }

    private void queued() {
        TextChannel channel = Database.getGuild(this.member.getGuild()).getMusicChannel().getTextChannel();
        Message queuedMsg = channel.sendMessage(MsgPresets.musicQueuedInfo(playlistTitle, tracks.element().getInfo())).complete();
        queuedMsg.addReaction("❌").queue();
        this.link = new MsgLink(queuedMsg);
        CmdHandler.queueReactionTicket(this.link, this);
    }
    private void dequeued() {
        Message msg = this.link.getMessage();
        msg.editMessage(MsgPresets.musicDequeuedInfo(playlistTitle, tracks.element().getInfo())).queue(msgA -> msgA.delete().queueAfter(20, TimeUnit.SECONDS));
        msg.clearReactions().queue();
    }
    private void playing() {
        Message msg = this.link.getMessage();
        msg.editMessage(MsgPresets.musicPlayingInfo(playlistTitle, tracks.element().getInfo())).queue();
        msg.clearReactions().queue();
    }

    public void clear() {
        this.tracks.clear();
        this.link.getMessage().delete().queue();
    }

    @Override
    public void emoteUpdate(ReactEvent reactEvent) {
        boolean sameUser = this.member.getUser().getId().equals(reactEvent.getUser().getId());
        if (sameUser && reactEvent.getEmote().equals("❌")) {
            dequeued();
            PlayerManager.getTrackManager(this.member.getGuild()).remove(this);
        } else {
            CmdHandler.queueReactionTicket(this.link, this);
        }
    }
}
