package core.database.groups;

import com.toddway.shelf.ShelfItem;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Role;
import net.dv8tion.jda.core.entities.TextChannel;
import core.BotConfig;
import core.database.Database;

import java.util.HashMap;
import java.util.Map;

public class GSettings {
    private Guild guild;
    private String path;

    public GSettings(Guild guild, String path) {
        this.guild = guild;
        this.path = path;
    }
    private ShelfItem item(String key) {
        return Database.shelf.item(path + key);
    }

    public void setPrefix(String prefix) {
        item("PREFIX").put(prefix);
    }
    public String getPrefix() {
        if (item("PREFIX").exists()) {
            return item("PREFIX").get(String.class);
        }
        return BotConfig.getDefaultPrefix();
    }

    public void setBotChannel(TextChannel channel) {
        item("BOTCHANNEL").put(channel);
    }
    public TextChannel getBotChannel() {
        if (item("BOTCHANNEL").exists()) {
            return item("BOTCHANNEL").get(TextChannel.class);
        }
        return guild.getDefaultChannel();
    }


    public void setBootMessage(boolean bootMessage) {
        item("BOOTMESSAGE").put(bootMessage);
    }
    public boolean getBootMessage() {
        if (item("BOOTMESSAGE").exists()) {
            return item("BOOTMESSAGE").get(boolean.class);
        }
        return true;
    }

    public void setDJRole(Role role) {
        item("DJROLE").put(role);
    }
    public Role getDJRole() {
        if (item("DJROLE").exists()) {
            return item("DJROLE").get(Role.class);
        }
        return null;
    }

    //Wert von 200 bis 0
    public void setVolume(int vol) {
        if (vol > 100) {
            vol = 100;
        } else if (vol < 10) {
            vol = 10;
        }
        item("VOLUME").put(vol);
    }
    public int getVolume() {
        if (item("VOLUME").exists()) {
            return item("VOLUME").get(int.class);
        } else {
            //TODO: Einstellbar machen
            return 50;
        }
    }

    public void setMusicChannel(TextChannel channel) {
        item("MUSICCHANNEL").put(channel);
    }
    public TextChannel getMusicChannel() {
        if (item("MUSICCHANNEL").exists()) {
            return item("MUSICCHANNEL").get(TextChannel.class);
        }
        return this.guild.getDefaultChannel();
    }

    public void setMap(Map<String, String> map) {
        item("MAP").put(map);
    }
    public Map<String, String> getMap() {
        if (item("MAP").exists()) {
            return (Map<String, String>) item("MAP").get(Map.class);
        }
        return new HashMap<>();
    }
}
