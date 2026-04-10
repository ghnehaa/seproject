package admin;

import utils.FileHandler;
import java.util.*;

public class AdminController {
    private static final String ADMINS_FILE = "data/admins.json";
    private List<Admin> admins;

    public AdminController() {
        admins = loadAdmins();
        if (admins.isEmpty()) {
            // Seed default admin
            admins.add(new Admin("A001", "admin", "admin123"));
            saveAdmins();
        }
    }

    public Admin login(String username, String password) {
        for (Admin a : admins) {
            if (a.getUsername().equals(username) && a.getPassword().equals(password)) {
                return a;
            }
        }
        return null;
    }

    private List<Admin> loadAdmins() {
        List<Admin> list = new ArrayList<>();
        String content = FileHandler.readFile(ADMINS_FILE);
        if (content == null || content.trim().isEmpty() || content.equals("[]")) return list;
        // Simple JSON array parse
        content = content.trim().replaceAll("^\\[|\\]$", "");
        String[] entries = content.split("\\},\\{");
        for (String entry : entries) {
            entry = entry.replaceAll("[\\[\\]{}]", "");
            Map<String, String> map = FileHandler.parseJsonObject(entry);
            if (map.containsKey("adminId")) {
                list.add(new Admin(map.get("adminId"), map.get("username"), map.get("password")));
            }
        }
        return list;
    }

    private void saveAdmins() {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < admins.size(); i++) {
            sb.append(admins.get(i).toJson());
            if (i < admins.size() - 1) sb.append(",");
        }
        sb.append("]");
        FileHandler.writeFile(ADMINS_FILE, sb.toString());
    }
}
