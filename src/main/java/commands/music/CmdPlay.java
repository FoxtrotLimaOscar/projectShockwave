package commands.music;

import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackInfo;
import commands.*;
import commands.music.utils.*;
import core.Permission;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.TextChannel;
import core.database.Database;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import tools.MsgPresets;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class CmdPlay implements CmdInterface, SearchResultHandler, ReactHandler {

    private Member member;
    private TextChannel channel;

    @Override
    public Permission permission() {
        return Permission.NONE;
    }

    @Override
    public void run(Command cmd) {
        GuildMessageReceivedEvent event = cmd.getEvent();
        this.member = event.getMember();
        this.channel = event.getChannel();
        if (cmd.hasParam(1)) {
            String identifier = cmd.getRaw(1);
            identifier = makeSearchReady(identifier);
            PlayerManager.searchTrack(this.member.getGuild(), identifier, this);
        } else {
            this.channel.sendMessage(MsgPresets.musicNoSearchfactor(Database.getGuild(this.member.getGuild()).getPrefix())).queue();
        }
    }

    @Override
    public void emoteUpdate(ReactEvent reactEvent) {
        boolean sameUser = this.member.getUser().equals(reactEvent.getUser());
        if (sameUser && reactEvent.getEmote().equals("❌")) {
            reactEvent.getMessage().getTextChannel().sendMessage("COMING SOON").queue();
        }
    }

    @Override
    public String syntax(String prefix) {
        return prefix + "play < Suchschlüssel | Link >";
    }

    @Override
    public String description() {
        return "Spielt einen Song anhand eines Links oder eines Namens ab";
    }

    @Override
    public String details() {
        return description();
    }

    @Override
    public void searchResults(AudioPlaylist playlist) {
        handleSearchResults(playlist, this.channel, this.member);
    }

    static void handleSearchResults(AudioPlaylist playlist, TextChannel channel, Member member) {
        if (!member.getVoiceState().inVoiceChannel()) {
            channel.sendMessage(MsgPresets.musicNotConnected()).queue();
            return;
        }
        TrackManager manager = PlayerManager.getTrackManager(member.getGuild());
        List<AudioTrack> foundTracks = playlist.getTracks();
        LinkedList<AudioTrack> futureTracks = new LinkedList<>();
        if(playlist.isSearchResult()) {
            futureTracks.add(foundTracks.get(0));
        } else {
            futureTracks.addAll(foundTracks);
        }
        if (futureTracks.size() == 1 && !manager.getQueue().isEmpty()) {
            Message queuedMsg ;
            AudioTrack firstTrack = futureTracks.get(0);
            AudioTrackInfo firstTrackInfo = firstTrack.getInfo();
            AudioInfo audioInfo = new AudioInfo(firstTrack, member);
            queuedMsg = channel.sendMessage(MsgPresets.musicQueuedInfo(false, firstTrackInfo.title, firstTrackInfo.uri)).complete();
            new QueuedMessage(queuedMsg, audioInfo);
            manager.queue(audioInfo);
            return;
        } else if (futureTracks.size() > 1) {
            Collections.shuffle(futureTracks);
            channel.sendMessage(MsgPresets.musicQueuedInfo(true, playlist.getName(), futureTracks.get(0).getInfo().uri)).queue();
        }
        for (AudioTrack track : futureTracks) {
            manager.queue(track, member);
        }
    }

    static String makeSearchReady(String identifier) {
        if (!(identifier.startsWith("http://") || identifier.startsWith("https://"))) {
            identifier = "ytsearch:" + identifier;
        }
        return identifier;
    }
}
