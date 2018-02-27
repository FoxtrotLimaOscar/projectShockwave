package commands.music;

import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackInfo;
import commands.*;
import commands.music.utils.*;
import core.Permission;
import net.dv8tion.jda.core.entities.*;
import core.database.Database;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import tools.MsgPresets;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class CmdPlay implements CmdInterface, SearchResultHandler {

    private Member member;
    private TextChannel channel;
    private String playlistLink;

    @Override
    public Permission permission() {
        return Permission.DJ;
    }

    @Override
    public void run(Command cmd) {
        GuildMessageReceivedEvent event = cmd.getEvent();
        Guild guild = event.getGuild();
        User user = event.getAuthor();
        TrackManager trackManager = PlayerManager.getTrackManager(guild);
        this.member = event.getMember();
        this.channel = event.getChannel();
        if (trackManager.isPaused()) {
            trackManager.resume();
            this.channel.sendMessage(MsgPresets.musicResumed()).queue(m -> m.delete().queueAfter(10, TimeUnit.SECONDS));
            return;
        }
        if (!cmd.hasParam(1)) {
            this.channel.sendMessage(MsgPresets.musicNoSearchfactor(Database.getGuild(guild).getPrefix())).queue(m -> m.delete().queueAfter(10, TimeUnit.SECONDS));
            return;
        }
        String raw = cmd.getRaw(1);
        String identifier = getMapSong(raw, guild, user);
        if (identifier == null) {
            identifier = raw;
        }
        identifier = makeSearchReady(identifier);
        PlayerManager.searchTrack(guild, identifier, this);
        this.playlistLink = identifier;
    }

    @Override
    public String syntax(String prefix) {
        return prefix + "play < Map-Schlüssel | Suchbegriff | Link >";
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
        handleSearchResults(playlist, this.playlistLink, this.channel, this.member);
    }

    static void handleSearchResults(AudioPlaylist playlist, String playlistLink, TextChannel channel, Member member) {
        System.out.println("cmdPlay.handleSearchResults()");
        if (!member.getVoiceState().inVoiceChannel()) {
            channel.sendMessage(MsgPresets.musicNotConnected()).queue();
            return;
        }
        TrackManager manager = PlayerManager.getTrackManager(member.getGuild());
        List<AudioTrack> foundTracks = playlist.getTracks();
        LinkedList<AudioTrack> stackTracks = new LinkedList<>();
        if(playlist.isSearchResult()) {
            stackTracks.add(foundTracks.get(0));
        } else {
            stackTracks.addAll(foundTracks);
        }
        System.out.println("cmdPlay.handleSearchResults()=" + stackTracks.get(0).getInfo().title);
        manager.queue(new QueueItem(stackTracks, member, playlist.getName(), playlistLink));
    }

    static String makeSearchReady(String identifier) {
        if (!(identifier.startsWith("http://") || identifier.startsWith("https://"))) {
            identifier = "ytsearch:" + identifier;
        }
        return identifier;
    }

    private static String getMapSong(String key, Guild guild, User user) {
        Map<String, String > userMap = Database.getUser(user).getMap();
        Map<String, String > guildMap = Database.getGuild(guild).getMap();
        Map<String, String > botMap = Database.getBot().getMap();
        return userMap.getOrDefault(key, guildMap.getOrDefault(key, botMap.getOrDefault(key, null)));
    }
}
