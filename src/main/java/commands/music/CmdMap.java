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
        USettings uSettings = Database.getUser(event.getAuthor());
        if (!cmd.hasParam(1)) { //Wenn Eingabe nur den invoke (param0) enthält -> Die Map anzeigen
            this.channel.sendMessage(MsgPresets.mapSaves(Database.getUser(event.getAuthor()).getMap())).queue();
        } else if (!cmd.hasParam(2)) { //Wenn kein Wert (param2) zum Schlüssel (param1) eingegeben wurde -> Den Wert des Schlüssels bekommen und wiedergeben
            Map<String, String> map = Database.getUser(event.getAuthor()).getMap();
            String key = cmd.getSlice(1);
            if (map.containsKey(key)) {
                String identifier = map.get(key);
                identifier = CmdPlay.makeSearchReady(identifier);
                PlayerManager.searchTrack(event.getGuild(), identifier, this);
            } else {
                this.channel.sendMessage(MsgPresets.mapNoSuchKey(key)).queue();
            }
        } else if (cmd.getSlice(2).equals("delete")) { //Wenn der Wert (param2) "delete" ist -> Den Schlüssel mit Wert löschen
            Map<String, String> map = Database.getUser(event.getAuthor()).getMap();
            String key = cmd.getSlice(1);
            if (map.containsKey(key)) {
                map.remove(key);
                uSettings.setMap(map);
                this.channel.sendMessage(MsgPresets.mapDeleted(key)).queue();
            } else {
                this.channel.sendMessage(MsgPresets.mapNoSuchKey(key)).queue();
            }
        } else { //Wenn der Schlüssel (param1) und der Wert (param2) vorhanden sind -> Einen Schlüssel mit dem angegebenen Wert speichern
            Map<String, String> map = Database.getUser(event.getAuthor()).getMap();
            String key = cmd.getSlice(1);
            String value = cmd.getRaw(2);
            map.put(key, value);
            uSettings.setMap(map);
            this.channel.sendMessage(MsgPresets.mapSaved(key, value)).queue();
        }
    }

    @Override
    public String syntax(String prefix) {
        return prefix + "map < Name > < delete | Name oder Link >";
    }

    @Override
    public String description() {
        return "Lässt dich Songtitel und Links zu einem eigenen Namen abkürzen";
    }

    @Override
    public String details() {
        return description();
    }

    @Override
    public void searchResults(AudioPlaylist playlist) {
        CmdPlay.handleSearchResults(playlist, this.channel, this.member);
    }
}
