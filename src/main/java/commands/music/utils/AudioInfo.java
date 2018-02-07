package commands.music.utils;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.dv8tion.jda.core.entities.Member;

import java.util.Random;

public class AudioInfo {
    private final int ID;
    private final AudioTrack TRACK;
    private final Member AUTHOR;

    public AudioInfo(AudioTrack track, Member author) {
        this.TRACK = track;
        this.AUTHOR = author;
        this.ID = new Random().nextInt();
    }

    public AudioTrack getTrack() {
        return TRACK;
    }

    public Member getAuthor() {
        return AUTHOR;
    }
}
