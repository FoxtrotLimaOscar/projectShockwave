package entities;

import com.toddway.shelf.ShelfItem;
import net.dv8tion.jda.core.entities.Game;
import settings.Database;

public class BotSets {
    private String path = "BOT/";

    private ShelfItem item(String key) {
        return Database.shelf.item(path + key);
    }



    public Game getGame() {
        if (item("GAME").exists()) {
            return item("GAME").get(Game.class);
        }
        return Game.playing("HelloWorld!");
    }
}
