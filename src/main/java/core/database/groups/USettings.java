package core.database.groups;

import com.toddway.shelf.ShelfItem;
import net.dv8tion.jda.core.entities.User;
import core.database.Database;

import java.util.HashMap;
import java.util.Map;

public class USettings {
    private User user;
    private String path;

    public USettings(User user, String path) {
        this.user = user;
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
}
