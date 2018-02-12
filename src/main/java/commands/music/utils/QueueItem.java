package commands.music.utils;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackInfo;
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
    private String title;
    private String uri;
    private  boolean isPlaylist;
    private Member member;
    private MsgLink link;
    private TextChannel channel;
    private LinkedList<AudioTrack> tracks;
    public QueueItem(LinkedList<AudioTrack> tracks, Member member, TextChannel channel, String playlistTitle) {
        this.tracks = tracks;
        this.member = member;
        this.channel = channel;
        this.isPlaylist = isPlaylist();
        AudioTrackInfo firstTrackInfo = tracks.get(0).getInfo();
        if (this.isPlaylist) {
            this.title = playlistTitle;
        } else {
            this.title = firstTrackInfo.title;
        }
        this.uri = firstTrackInfo.uri;
        warnWrongChannel();
        sendQueuedMessage();
    }
    public QueueItem(AudioTrack track, Member member, TextChannel channel) {
        this.tracks.add(track);
        this.member = member;
        this.channel = channel;
        this.isPlaylist = false;
        AudioTrackInfo trackInfo = track.getInfo();
        this.title = trackInfo.title;
        this.uri = trackInfo.uri;
        warnWrongChannel();
        sendQueuedMessage();
    }

    public Member getMember() {
        return this.member;
    }

    private void updatePlaying() {
        Message msg = this.link.getMessage();
        msg.clearReactions().queue();
        msg.editMessage(MsgPresets.musicPlayingInfo(this.title, this.uri)).queue();
    }

    public AudioTrack getTrack() {
        AudioTrack track = this.tracks.peekFirst();
        AudioTrackInfo trackInfo = track.getInfo();
        this.title = trackInfo.title;
        this.uri = trackInfo.uri;
        updatePlaying();
        return track;
    }

    public boolean isEmpty() {
        return this.tracks.isEmpty();
    }

    public boolean finishedPlaying() {
        this.tracks.pollFirst();
        AudioTrack newFirstTrack = tracks.peekFirst();
        if (newFirstTrack == null) {
            this.link.getMessage().delete().queue();
            return true;
        } else {
            AudioTrackInfo trackInfo = newFirstTrack.getInfo();
            this.title = trackInfo.title;
            this.uri = trackInfo.uri;
            updatePlaying();
        }
        return false;
    }

    private void sendQueuedMessage() {
        Message msg = this.channel.sendMessage(MsgPresets.musicQueuedInfo(this.isPlaylist, this.title, this.uri)).complete();
        msg.addReaction("❌").queue();
        this.link = new MsgLink(msg);
        CmdHandler.queueReactionTicket(this.link, this);
    }

    private boolean isPlaylist() {
        return tracks.size() > 1;
    }

    private void warnWrongChannel() {
        TextChannel musicChannel = Database.getGuild(this.member.getGuild()).getMusicChannel().getTextChannel();
        if (!this.channel.getId().equals(musicChannel.getId())) {
            this.channel.sendMessage(MsgPresets.musicWrongTextChannel(musicChannel)).queue();
            this.channel = musicChannel;
        }
    }

    @Override
    public void emoteUpdate(ReactEvent reactEvent) {
        if (reactEvent.getUser().getId().equals(this.member.getUser().getId()) && reactEvent.getEmote().equals("❌")) {
            this.tracks.clear();
            Message msg = reactEvent.getMessage();
            msg.editMessage(MsgPresets.musicDequeuedInfo(this.title, this.uri)).queue(msgA -> msgA.delete().queueAfter(10, TimeUnit.SECONDS));
            msg.clearReactions().queue();
        } else {
            CmdHandler.queueReactionTicket(reactEvent.getMessageLink(), this);
        }
    }
}
