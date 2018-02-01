package commands.music.utils;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.TextChannel;
import settings.Database;
import tools.MsgPresets;
import tools.SubsToolkit;

import java.util.*;

public class PlayerManager {
    public static final AudioPlayerManager MANAGER = new DefaultAudioPlayerManager();
    private static final Map<Guild, Map.Entry<AudioPlayer, TrackManager>> PLAYERS = new HashMap<>();

    private static AudioPlayer createPlayer(Guild guild) {
        AudioSourceManagers.registerRemoteSources(MANAGER);
        AudioPlayer player = MANAGER.createPlayer();
        TrackManager manager = new TrackManager(player);
        player.addListener(manager);
        guild.getAudioManager().setSendingHandler(new PlayerSendHandler(player));
        PLAYERS.put(guild, new AbstractMap.SimpleEntry<>(player, manager));
        return player;
    }

    private static boolean hasPlayer(Guild guild) {
        return PLAYERS.containsKey(guild);
    }

    private static AudioPlayer getPlayer(Guild guild) {
        if (hasPlayer(guild)) {
            return PLAYERS.get(guild).getKey();
        } else {
            return createPlayer(guild);
        }
    }

    private static TrackManager getManager(Guild guild) {
        //TODO: Kontrollieren ob Manager Null returnen darf
        return PLAYERS.get(guild).getValue();
    }

    private boolean isIdle(Guild guild) {
        return !hasPlayer(guild) || getPlayer(guild).getPlayingTrack() == null;
    }

    public static void loadTrack(String identifier, int selection, boolean shuffle, Member member, TextChannel channel) {
        Guild guild = member.getGuild();
        AudioPlayer player = getPlayer(guild);
        ArrayList<AudioTrack> gonnaQueue = new ArrayList<>();
        boolean sendInfo = !getManager(guild).getQueue().isEmpty();


        //TODO Einstellbar machen
        player.setVolume(Database.getGuildSets(guild).getVolume());
        //TODO: Einstellbar machen
        MANAGER.setFrameBufferDuration(15000);
        MANAGER.loadItemOrdered(guild, identifier, new AudioLoadResultHandler() {

            @Override
            public void trackLoaded(AudioTrack track) {
                getManager(guild).queue(track, member);
            }

            @Override
            public void playlistLoaded(AudioPlaylist playlist) {
                if (playlist.isSearchResult()) {
                    gonnaQueue.add(playlist.getTracks().get(selection));
                } else {
                    gonnaQueue.addAll(playlist.getTracks());
                }
                if (shuffle) {
                    Collections.shuffle(gonnaQueue);
                }
                //TODO: Playlistlimit Einstellbar machen
                for (AudioTrack track : gonnaQueue.subList(0, SubsToolkit.lowerOf(gonnaQueue.size(), 100))) {
                    trackLoaded(track);
                }
            }

            @Override
            public void noMatches() {
                channel.sendMessage(MsgPresets.musicNoResultsFound()).queue();
            }

            @Override
            public void loadFailed(FriendlyException exception) {
                exception.printStackTrace();
            }
        });
    }
}
