package tools;

import java.io.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Scanner;

public class Configuration extends LinkedHashMap <String, String> {

    File file;

    public Configuration(File file, ArrayList<String> defaults) {
        try {
            this.file = file;
            if(!file.exists()) {
                writeLines(defaults);
            }
            Reader reader = new FileReader(file);
            parse(reader);
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void parse(Reader reader) {
        this.clear();
        Scanner scanner = new Scanner(reader);
        while(scanner.hasNextLine()) {
            String line = scanner.nextLine();
            if(!line.startsWith("//") && line.length() > 3 && line.contains("=")) {
                int index = line.indexOf("=");
                String key = line.substring(0, index);
                String value = line.substring(index + 1, line.length());
                this.put(key, value);
            }
        }
        scanner.close();
    }

    private void writeLines(ArrayList<String> lines) {
        try {
            FileWriter writer = new FileWriter(file);
            for(String line : lines) {
                writer.write(line);
                writer.write(System.getProperty("line.separator"));
            }
            writer.close();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }
}
