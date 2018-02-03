package tools.SubsConfig;

import java.util.ArrayList;
import java.util.Arrays;

public class ConfigItem {
    private String key;
    private String value;

    public ConfigItem(String key, String value) {
        this.key = key;
        this.value = value;
    }
    public ConfigItem(String key, ArrayList<String> values) {
        this.key = key;
        this.value = "";
        for (String value : values) {
            this.value += value + ";";
        }
    }
    public ConfigItem(Enum key, String value) {
        this.key = key.toString();
        this.value = value;
    }
    public ConfigItem(Enum key, ArrayList<String> values) {
        this.key = key.toString();
        this.value = "";
        for (String value : values) {
            this.value += value + ";";
        }
    }
    public ConfigItem(String line) {
        int index = line.indexOf("=");
        this.key = line.substring(0, index);
        this.value = line.substring(index + 1, line.length());
    }

    public boolean isArray() {
        return value.contains(";");
    }
    public boolean hasArray() {
        return this.value != null && !this.value.equals("") && isArray();
    }
    public boolean hasSingle() {
        return this.value != null && !this.value.equals("") && !isArray();
    }
    public ArrayList<String> getArray() {
        if (isArray()) {
            return new ArrayList<>(Arrays.asList(this.value.split(";")));
        }
        return null;
    }
    public String getSingle() {
        if (!isArray()) {
            return this.value;
        }
        return null;
    }
    public String getKey() {
        return this.key;
    }
    public String getValue() {
        return this.value;
    }
    public String getLine() {
        return this.key + "=" + this.value;
    }
}
