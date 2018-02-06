package commands.music;

import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackInfo;
import commands.CmdInterface;
import commands.Command;
import commands.ReactHandler;
import commands.music.utils.PlayerManager;
import commands.music.utils.SearchResultHandler;
import commands.music.utils.TrackManager;
import core.Permission;
import core.database.groups.GSettings;
import entities.ReactEvent;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import core.database.Database;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import tools.MsgPresets;

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
            if (!(identifier.startsWith("http://") || identifier.startsWith("https://"))) {
                identifier = "ytsearch:" + identifier;
            }
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
    public void searchResults(AudioPlaylist playlist) {
        TrackManager manager = PlayerManager.getTrackManager(this.member.getGuild());
        List<AudioTrack> tracks = playlist.getTracks();
        if (playlist.getTracks().size() == 1) {
            AudioTrack track = tracks.get(0);
            AudioTrackInfo info = track.getInfo();
            if (manager.getQueue().isEmpty()) {
                this.channel.sendMessage(MsgPresets.musicQueuedInfo(false, info.title, info.uri)).queue();
            }
            manager.queue(track, this.member);
        } else {
            if (!manager.getQueue().isEmpty()) {
                this.channel.sendMessage(MsgPresets.musicQueuedInfo(true, playlist.getName(), tracks.get(0).getInfo().uri)).queue();
            }
            for (AudioTrack track : tracks) {
                manager.queue(track, this.member);
            }
        }
    }
}
