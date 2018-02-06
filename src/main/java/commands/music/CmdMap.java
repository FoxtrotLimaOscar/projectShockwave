package commands.music;

import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import commands.CmdInterface;
import commands.Command;
import commands.music.utils.PlayerManager;
import commands.music.utils.SearchResultHandler;
import core.Permission;
import core.database.Database;
import core.database.groups.USettings;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import tools.MsgPresets;

import java.util.Map;

public class CmdMap implements CmdInterface, SearchResultHandler {
    private Member member;
    private TextChannel channel;

    @Override
    public Permission permission() {
        return Permission.NONE;
    }

    @Override
    public void run(Command cmd) {
        GuildMessageReceivedEvent event = cmd.getEvent();
        this.channel = event.getChannel();
        this.member = event.getMember();
        if (!cmd.hasParam( 1)) {
            this.channel.sendMessage(MsgPresets.mapSaves(Database.getUser(event.getAuthor()).getMap())).queue();
        } else if (!cmd.hasParam(2)) {
            Map<String, String> map = Database.getUser(event.getAuthor()).getMap();
            String key = cmd.getSlice(1);
            if (map.containsKey(key)) {
                String identifier = map.get(key);
                identifier = CmdPlay.makeSearchReady(identifier);
                PlayerManager.searchTrack(event.getGuild(), identifier, this);
            } else {
                this.channel.sendMessage(MsgPresets.mapNoSuchKey(key)).queue();
            }
        } else if (cmd.getSlice(2).equals("delete")) {
            USettings uSettings = Database.getUser(event.getAuthor());
            Map<String, String> map = uSettings.getMap();
            String key = cmd.getSlice(1);
            if (map.containsKey(key)) {
                map.remove(key);
                uSettings.setMap(map);
                this.channel.sendMessage(MsgPresets.mapDeleted(key)).queue();
            } else {
                this.channel.sendMessage(MsgPresets.mapNoSuchKey(key)).queue();
            }
        } else {
            USettings uSettings = Database.getUser(event.getAuthor());
            Map<String, String> map = uSettings.getMap();
            String key = cmd.getSlice(1);
            String value = cmd.getRaw(2);
            map.put(key, value);
            uSettings.setMap(map);
            this.channel.sendMessage(MsgPresets.mapSaved(key, value)).queue();
        }
    }

    @Override
    public String syntax(String prefix) {
        return prefix + "map < name > < delete | Name oder Link >";
    }

    @Override
    public String description() {
        return "Lässt dich Songtitel und Links zu einem eigenen Namen abkürzen";
    }

    @Override
    public void searchResults(AudioPlaylist playlist) {
        CmdPlay.handleSearchResults(playlist, this.channel, this.member);
    }
}
