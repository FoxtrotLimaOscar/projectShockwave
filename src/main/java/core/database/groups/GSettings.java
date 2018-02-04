package core.database.groups;

import com.toddway.shelf.ShelfItem;
import entities.DJRole;
import entities.MusicChannel;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.TextChannel;
import core.BotConfig;
import core.database.Database;

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


    public void setDJRole(DJRole djRole) {
        item("DJROLE").put(djRole);
    }
    public DJRole getDJRole() {
        if (item("DJROLE").exists()) {
            return item("DJROLE").get(DJRole.class);
        }
        return new DJRole();
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



    public void setMusicChannel(MusicChannel channel) {
        item("MUSICCHANNEL").put(channel);
    }
    public MusicChannel getMusicChannel() {
        if (item("MUSICCHANNEL").exists()) {
            return item("MUSICCHANNEL").get(MusicChannel.class);
        }
        return new MusicChannel(guild.getDefaultChannel());
    }
}
