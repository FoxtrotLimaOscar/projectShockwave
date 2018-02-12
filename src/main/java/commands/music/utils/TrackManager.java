package commands.music.utils;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.VoiceChannel;

import java.util.*;

public class TrackManager extends AudioEventAdapter {
    private final Guild GUILD;
    private final AudioPlayer PLAYER;
    private final LinkedList<QueueItem> queue;

    public TrackManager(AudioPlayer player, Guild guild) {
        this.GUILD = guild;
        this.PLAYER = player;
        this.queue = new LinkedList<>();
    }

    //Fügt Tracks der Queue hinzu und spielt den ersten ab wenn gerade kein Track gespielt wird
    public void queue(QueueItem queueItem) {
        queue.add(queueItem);
        if (PLAYER.getPlayingTrack() == null) {
            PLAYER.playTrack(receiveTrack());
        }
    }

    public Set<QueueItem> getQueue() {
        return new LinkedHashSet<>(queue);
    }

    public void purgeQueue() {
        queue.clear();
    }

    public void remove(QueueItem item) {
        this.queue.remove(item);
    }

    public void shuffleQueue() {
        List<QueueItem> cQueue = new ArrayList<>(getQueue());
        QueueItem current = cQueue.get(0);
        cQueue.remove(0);
        Collections.shuffle(cQueue);
        cQueue.add(0, current);
        purgeQueue();
        queue.addAll(cQueue);
    }


    @Override
    public void onTrackStart(AudioPlayer player, AudioTrack track) {
        Member member = queue.peekFirst().getMember();
        VoiceChannel vChan = member.getVoiceState().getChannel();
        if (vChan == null) {
            player.stopTrack();
        } else {
            member.getGuild().getAudioManager().openAudioConnection(vChan);
        }
    }
    //Löscht den ersten/aktuellen Track aus der queue,  und wenn es einen weiteren Track giebt wird dieser abgespielt
    @Override
    public void onTrackEnd(AudioPlayer player, AudioTrack track, AudioTrackEndReason endReason) {
        AudioTrack recievedTrack = receiveTrack();
        if (recievedTrack == null) {
            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    TrackManager.this.GUILD.getAudioManager().closeAudioConnection();
                }
            }, 0);
        } else {
            player.playTrack(recievedTrack);
        }
    }

    private AudioTrack receiveTrack() {
        if (queue.isEmpty()) {
            return null;
        }
        AudioTrack track = queue.peekFirst().receiveTrack();
        if (track == null) {
            queue.poll();
            track = queue.peekFirst().receiveTrack();
        }
        return track;
    }
}
