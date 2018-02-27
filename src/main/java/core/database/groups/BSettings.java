package core.database.groups;

import com.toddway.shelf.ShelfItem;
import net.dv8tion.jda.core.entities.Game;
import core.database.Database;

import java.util.HashMap;
import java.util.Map;

public class BSettings {
    private String path;

    public BSettings(String path) {
        this.path = path;
    }
    private ShelfItem item(String key) {
        return Database.shelf.item(path + key);
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

    public void setGame(Game game) {
        item("GAME").put(game);
    }
    public Game getGame() {
        if (item("GAME").exists()) {
            return item("GAME").get(Game.class);
        }
        return Game.playing("HelloWorld!");
    }
}
