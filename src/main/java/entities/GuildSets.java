package entities;

import com.toddway.shelf.ShelfItem;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.TextChannel;
import settings.BotConfig;
import settings.Database;

public class GuildSets {
    private Guild guild;
    private String path;

    public GuildSets(Guild guild, String path) {
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
}
