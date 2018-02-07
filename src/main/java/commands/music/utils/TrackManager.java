package commands.music.utils;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackInfo;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.VoiceChannel;
import core.database.Database;
import tools.MsgPresets;

import java.util.*;
import java.util.concurrent.LinkedBlockingQueue;

public class TrackManager extends AudioEventAdapter {
    private final Guild GUILD;
    private final AudioPlayer PLAYER;
    private final LinkedList<AudioInfo> queue;

    public TrackManager(AudioPlayer player, Guild guild) {
        this.GUILD = guild;
        this.PLAYER = player;
        this.queue = new LinkedList<>();
    }

    public AudioInfo queue(AudioTrack track, Member author) {
        return this.queue(new AudioInfo(track, author));
    }

    public AudioInfo queue(AudioInfo audioInfo) {
        queue.add(audioInfo);
        if (PLAYER.getPlayingTrack() == null) {
            PLAYER.playTrack(audioInfo.getTrack());
        }
        return audioInfo;
    }

    public boolean dequeue(AudioInfo audioInfo) {
        if (audioInfo.equals(queue.element())) {
            return false;
        } else {
            queue.removeLastOccurrence(audioInfo);
            return true;
        }
    }

    public Set<AudioInfo> getQueue() {
        return new LinkedHashSet<>(queue);
    }

    public AudioInfo getInfo(AudioTrack track) {
        return queue.stream()
                .filter(info -> info.getTrack().equals(track))
                .findFirst().orElse(null);
    }

    public void purgeQueue() {
        queue.clear();
    }

    public void shuffleQueue() {
        List<AudioInfo> cQueue = new ArrayList<>(getQueue());
        AudioInfo current = cQueue.get(0);
        cQueue.remove(0);
        Collections.shuffle(cQueue);
        cQueue.add(0, current);
        purgeQueue();
        queue.addAll(cQueue);
    }



    @Override
    public void onTrackStart(AudioPlayer player, AudioTrack track) {
        AudioInfo info = queue.element();
        Guild guild = info.getAuthor().getGuild();
        VoiceChannel vChan = info.getAuthor().getVoiceState().getChannel();
        if (vChan == null) {
            player.stopTrack();
        } else {
            info.getAuthor().getGuild().getAudioManager().openAudioConnection(vChan);
            Database.getGuild(guild).getMusicChannel().getTextChannel().sendMessage(MsgPresets.musicPlayingInfo(track.getInfo().title, track.getInfo().uri)).queue();
        }
    }
    @Override
    public void onTrackEnd(AudioPlayer player, AudioTrack track, AudioTrackEndReason endReason) {
        queue.poll();
        if (queue.isEmpty()) {
            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    TrackManager.this.GUILD.getAudioManager().closeAudioConnection();
                }
            }, 0);
        } else {
            player.playTrack(queue.element().getTrack());
        }
    }
}
