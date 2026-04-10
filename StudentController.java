package student;

import utils.FileHandler;
import java.util.*;

public class StudentController {
    private static final String STUDENTS_FILE = "data/students.json";
    private List<Student> students;

    public StudentController() {
        students = loadStudents();
    }

    // ── Authentication ──────────────────────────────────────────────────
    public Student login(String username, String password) {
        for (Student s : students) {
            if (s.getUsername().equals(username) && s.getPassword().equals(password)) {
                return s;
            }
        }
        return null;
    }

    // ── CRUD ─────────────────────────────────────────────────────────────
    public boolean addStudent(String name, String username, String password) {
        for (Student s : students) {
            if (s.getUsername().equals(username)) return false; // duplicate
        }
        String id = "S" + String.format("%03d", students.size() + 1);
        students.add(new Student(id, username, password, name));
        saveStudents();
        return true;
    }

    public List<Student> getAllStudents() {
        return Collections.unmodifiableList(students);
    }

    public boolean deleteStudent(String studentId) {
        boolean removed = students.removeIf(s -> s.getStudentId().equals(studentId));
        if (removed) saveStudents();
        return removed;
    }

    // ── Persistence ───────────────────────────────────────────────────────
    private List<Student> loadStudents() {
        List<Student> list = new ArrayList<>();
        String content = FileHandler.readFile(STUDENTS_FILE);
        if (content == null || content.trim().isEmpty() || content.equals("[]")) return list;
        content = content.trim().replaceAll("^\\[|\\]$", "");
        String[] entries = content.split("\\},\\{");
        for (String entry : entries) {
            entry = entry.replaceAll("[\\[\\]{}]", "");
            Map<String, String> map = FileHandler.parseJsonObject(entry);
            if (map.containsKey("studentId")) {
                list.add(new Student(
                    map.get("studentId"), map.get("username"),
                    map.get("password"),  map.getOrDefault("name", "Unknown")));
            }
        }
        return list;
    }

    private void saveStudents() {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < students.size(); i++) {
            sb.append(students.get(i).toJson());
            if (i < students.size() - 1) sb.append(",");
        }
        sb.append("]");
        FileHandler.writeFile(STUDENTS_FILE, sb.toString());
    }
}
