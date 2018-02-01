package settings;

import com.toddway.shelf.Shelf;
import core.Statics;
import entities.BotSets;
import entities.GuildSets;
import entities.UserSets;
import net.dv8tion.jda.core.entities.*;

import java.io.File;

public class Database {

    public static Shelf shelf = null;

    public static void load() {
        File file = new File("DATA");
        shelf = new Shelf(file);
    }

    public static GuildSets getGuildSets(Guild guild) {
        Statics.databaseAccesses++;
        return new GuildSets(guild, "GUILD-" + guild.getId() + "/");
    }

    public static UserSets getUserSets(User user) {
        Statics.databaseAccesses++;
        return new UserSets(user, "USER-" + user.getId() + "/");
    }

    public static BotSets getBot() {
        Statics.databaseAccesses++;
        return new BotSets();
    }
}
