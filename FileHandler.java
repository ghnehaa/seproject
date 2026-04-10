package utils;

import java.io.*;
import java.nio.file.*;
import java.util.*;

public class FileHandler {

    public static String readFile(String path) {
        try {
            File f = new File(path);
            if (!f.exists()) return "[]";
            return new String(Files.readAllBytes(f.toPath()));
        } catch (IOException e) {
            System.err.println("[FileHandler] Read error: " + e.getMessage());
            return "[]";
        }
    }

    public static void writeFile(String path, String content) {
        try {
            File f = new File(path);
            f.getParentFile().mkdirs();
            Files.write(f.toPath(), content.getBytes());
        } catch (IOException e) {
            System.err.println("[FileHandler] Write error: " + e.getMessage());
        }
    }

    /**
     * Minimal JSON object parser — handles flat {"key":"value",...} objects.
     * Does NOT handle nested objects or arrays as values.
     */
    public static Map<String, String> parseJsonObject(String json) {
        Map<String, String> map = new LinkedHashMap<>();
        // Match "key":"value" pairs (string values) and "key":number pairs
        java.util.regex.Matcher m =
            java.util.regex.Pattern.compile("\"([^\"]+)\":(?:\"([^\"]*)\"|(-?\\d+))")
                                   .matcher(json);
        while (m.find()) {
            String key   = m.group(1);
            String value = m.group(2) != null ? m.group(2) : m.group(3);
            map.put(key, value);
        }
        return map;
    }
}
