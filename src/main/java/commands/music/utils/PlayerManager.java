package commands.music.utils;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import core.database.Database;
import net.dv8tion.jda.core.entities.Guild;

import java.util.*;

public class PlayerManager {
    public static final AudioPlayerManager MANAGER = new DefaultAudioPlayerManager();
    private static final Map<Guild, Map.Entry<AudioPlayer, TrackManager>> PLAYERS = new HashMap<>();

    private static AudioPlayer createPlayer(Guild guild) {
        AudioSourceManagers.registerRemoteSources(MANAGER);
        AudioPlayer player = MANAGER.createPlayer();
        TrackManager manager = new TrackManager(player, guild);
        player.addListener(manager);
        player.setVolume(Database.getGuild(guild).getVolume());
        guild.getAudioManager().setSendingHandler(new PlayerSendHandler(player));
        PLAYERS.put(guild, new AbstractMap.SimpleEntry<>(player, manager));
        return player;
    }

    private static boolean hasPlayer(Guild guild) {
        return PLAYERS.containsKey(guild);
    }

    public static AudioPlayer getPlayer(Guild guild) {
        if (hasPlayer(guild)) {
            return PLAYERS.get(guild).getKey();
        } else {
            return createPlayer(guild);
        }
    }

    public static TrackManager getTrackManager(Guild guild) {
        //TODO: Kontrollieren ob Manager Null returnen darf
        if (!PLAYERS.containsKey(guild)) {
            createPlayer(guild);
        }
        return PLAYERS.get(guild).getValue();
    }

    public static boolean isIdle(Guild guild) {
        return !hasPlayer(guild) || getPlayer(guild).getPlayingTrack() == null;
    }

    public static void searchTrack(Guild guild, String identifier, SearchResultHandler handler) {
        getPlayer(guild);
        MANAGER.loadItemOrdered(guild, identifier, new AudioLoadResultHandler() {
            @Override
            public void trackLoaded(AudioTrack track) {}
            @Override
            public void playlistLoaded(AudioPlaylist playlist) {
                handler.searchResults(playlist);
            }
            @Override
            public void noMatches() {
                handler.searchResults(null);
            }
            @Override
            public void loadFailed(FriendlyException exception) {}
        });
    }

    public static void skip(Guild guild) {
        getPlayer(guild).stopTrack();
    }
    public static void skipPlaylist(Guild guild) {
        getTrackManager(guild).clearCurrentItem();
    }
}
