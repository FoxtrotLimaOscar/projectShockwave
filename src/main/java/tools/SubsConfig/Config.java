package tools.SubsConfig;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Config {
    private File file;
    private Map<String, ConfigItem> items = new HashMap<>();
    private Defaults defaults;

    public Config(File file, Defaults defaults) {
        this.file = file;
        this.defaults = defaults;
        if (!this.file.exists()) {
            writeDefaults();
        }
        load();
    }



    public ConfigItem item(String key) {
        if (this.items.containsKey(key)) {
            return this.items.get(key);
        }
        return null;
    }
    public ConfigItem item(Enum key) {
        if (this.items.containsKey(key.toString())) {
            return this.items.get(key.toString());
        }
        return null;
    }
    public boolean checkArray(String key) {
        return this.items.containsKey(key) && this.items.get(key).hasArray();
    }
    public boolean checkArray(Enum key) {
        return this.items.containsKey(key.toString()) && this.items.get(key.toString()).hasArray();
    }
    public boolean checkSingle(String key) {
        return this.items.containsKey(key) && this.items.get(key).hasSingle();
    }
    public boolean checkSingle(Enum key) {
        return this.items.containsKey(key.toString()) && this.items.get(key.toString()).hasSingle();
    }



    private void writeDefaults() {
        try {
            Writer writer = new FileWriter(this.file);
            String n = "";
            String seperator = System.getProperty("line.separator");
            for (String line : this.defaults.asLines()) {
                writer.write(n + line);
                n = seperator;
            }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void load() {
        try {
            this.items.clear();
            Reader reader = new FileReader(this.file);
            Scanner scanner = new Scanner(reader);
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                if (!line.startsWith("#")) {
                    ConfigItem item = new ConfigItem(line);
                    this.items.put(item.getKey(), item);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
