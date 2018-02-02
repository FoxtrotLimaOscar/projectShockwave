package tools.SubsConfig;

import java.util.ArrayList;

public class Defaults {
    private ArrayList<String> lines = new ArrayList<>();
    public Defaults add(ConfigItem item) {
        this.lines.add(item.getLine());
        return this;
    }
    public Defaults addDescription(String description) {
        this.lines.add("#" + description);
        return this;
    }
    public ArrayList<String> asLines() {
        return lines;
    }
}
