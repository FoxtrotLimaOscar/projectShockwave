package commands.music.utils;

import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;

public interface SearchResultHandler {
    void searchResults(AudioPlaylist playlist);
}
