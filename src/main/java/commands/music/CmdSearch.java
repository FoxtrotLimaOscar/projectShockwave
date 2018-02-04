package commands.music;

import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import commands.CmdHandler;
import commands.CmdInterface;
import commands.Command;
import commands.ReactHandler;
import commands.music.utils.PlayerManager;
import core.Permission;
import entities.ReactEvent;
import net.dv8tion.jda.core.entities.*;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import tools.MsgPresets;
import tools.SubsToolkit;

public class CmdSearch implements CmdInterface, SearchResultHandler, ReactHandler{
    private AudioPlaylist playlist;
    private TextChannel channel;
    private Member member;
    @Override
    public Permission permission() {
        return Permission.DJ;
    }

    @Override
    public void run(Command cmd) {
        GuildMessageReceivedEvent event = cmd.getEvent();
        this.channel = event.getChannel();
        this.member = event.getMember();
        PlayerManager.searchTrack(cmd.getEvent().getGuild(), "ytsearch:" +  cmd.getRaw(1), this);
    }

    @Override
    public String syntax(String prefix) {
        return prefix + "search < Suchschlüssel >";
    }

    @Override
    public String description() {
        return "Lässt dich eins von 5 Suchergebnissen auswählen";
    }

    @Override
    public void emoteUpdate(ReactEvent reactEvent) {
        User user = reactEvent.getUser();
        boolean sameUser = this.member.getUser().getId().equals(user.getId());
        String emote = reactEvent.getEmote();
        if (sameUser && emote.equals("1⃣")) {
            queueSong(this.playlist.getTracks().get(0) , this.member);
            reactEvent.getMessage().delete().queue();
        } else if (sameUser && emote.equals("2⃣")) {
            queueSong(this.playlist.getTracks().get(1) , this.member);
            reactEvent.getMessage().delete().queue();
        } else if (sameUser && emote.equals("3⃣")) {
            queueSong(this.playlist.getTracks().get(2) , this.member);
            reactEvent.getMessage().delete().queue();
        } else if (sameUser && emote.equals("4⃣")) {
            queueSong(this.playlist.getTracks().get(3) , this.member);
            reactEvent.getMessage().delete().queue();
        } else if (sameUser && emote.equals("5⃣")) {
            queueSong(this.playlist.getTracks().get(4) , this.member);
            reactEvent.getMessage().delete().queue();
        } else if (sameUser && emote.equals("❌")) {
            reactEvent.getMessage().delete().queue();
        } else {
            CmdHandler.reactionTickets.put(reactEvent.getMessageID(), this);
        }
    }

    @Override
    public void searchResults(AudioPlaylist playlist) {
        if (playlist != null) {
            this.playlist = playlist;
            Message msg = this.channel.sendMessage(MsgPresets.musicSearchResults(playlist)).complete();
            msg.addReaction("\u0031\u20E3").queue();
            msg.addReaction("\u0032\u20E3").queue();
            msg.addReaction("\u0033\u20E3").queue();
            msg.addReaction("\u0034\u20E3").queue();
            msg.addReaction("\u0035\u20E3").queue();
            msg.addReaction("❌").queue();
            CmdHandler.reactionTickets.put(msg.getId(), this);
        } else {
             this.channel.sendMessage(MsgPresets.musicNoResultsFound()).queue();
        }
    }

    private static void queueSong(AudioTrack track, Member member) {
        PlayerManager.getTrackManager(member.getGuild()).queue(track, member);
    }
}
