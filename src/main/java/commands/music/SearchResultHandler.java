package commands.music;

import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;

public interface SearchResultHandler {
    void searchResults(AudioPlaylist playlist);
}
