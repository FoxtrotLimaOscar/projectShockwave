package core.database;

import com.toddway.shelf.Shelf;
import core.Statics;
import core.database.groups.BSettings;
import core.database.groups.GSettings;
import core.database.groups.USettings;
import net.dv8tion.jda.core.entities.*;

import java.io.File;

public class Database {

    public static Shelf shelf = null;

    public static void load() {
        File file = new File("DATA");
        shelf = new Shelf(file);
    }

    public static GSettings getGuild(Guild guild) {
        Statics.databaseAccesses++;
        return new GSettings(guild, "GUILDS-" + guild.getId() + "-");
    }

    public static USettings getUser(User user) {
        Statics.databaseAccesses++;
        return new USettings(user, "USERS-" + user.getId() + "-");
    }

    public static BSettings getBot() {
        Statics.databaseAccesses++;
        return new BSettings();
    }

    public static boolean check() {
        String key = "putput";
        String item = "DIKKA";
        shelf.item(key).put(item);
        String rec = shelf.item(key).get(String.class);
        return item.equals(rec);
    }
}
