package commands.music;

import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackInfo;
import commands.*;
import commands.music.utils.PlayerManager;
import commands.music.utils.QueueItem;
import commands.music.utils.SearchResultHandler;
import commands.music.utils.TrackManager;
import core.Permission;
import core.database.Database;
import net.dv8tion.jda.core.entities.*;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import tools.MsgPresets;

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
        if (cmd.hasParam(1)) {
            PlayerManager.searchTrack(cmd.getEvent().getGuild(), "ytsearch:" +  cmd.getRaw(1), this);
        } else {
            this.channel.sendMessage(MsgPresets.musicNoSearchfactor(Database.getGuild(this.member.getGuild()).getPrefix())).queue();
        }

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
    public String details() {
        return description();
    }

    @Override
    public void emoteUpdate(ReactEvent reactEvent) {
        User user = reactEvent.getUser();
        Message msg = reactEvent.getMessage();
        boolean sameUser = this.member.getUser().getId().equals(user.getId());
        String emote = reactEvent.getEmote();
        if (sameUser && emote.equals("1⃣")) {
            queueFoundSong(this.playlist.getTracks().get(0) , this.member, msg);
        } else if (sameUser && emote.equals("2⃣")) {
            queueFoundSong(this.playlist.getTracks().get(1) , this.member, msg);
        } else if (sameUser && emote.equals("3⃣")) {
            queueFoundSong(this.playlist.getTracks().get(2) , this.member, msg);
        } else if (sameUser && emote.equals("4⃣")) {
            queueFoundSong(this.playlist.getTracks().get(3) , this.member, msg);
        } else if (sameUser && emote.equals("5⃣")) {
            queueFoundSong(this.playlist.getTracks().get(4) , this.member, msg);
        } else if (sameUser && emote.equals("❌")) {
            reactEvent.getMessage().delete().queue();
        } else {
            CmdHandler.queueReactionTicket(reactEvent.getMessageLink(), this);
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
            CmdHandler.queueReactionTicket(new MsgLink(msg), this);
        } else {
             this.channel.sendMessage(MsgPresets.musicNoResultsFound()).queue();
        }
    }

    private void queueFoundSong(AudioTrack track, Member member, Message msg) {
        Guild guild = member.getGuild();
        TrackManager manager = PlayerManager.getTrackManager(guild);
        msg.delete().queue();
        manager.queue(new QueueItem(track, member));
    }
}
