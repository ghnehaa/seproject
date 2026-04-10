package exam;

import utils.FileHandler;
import java.util.*;
import java.util.regex.*;

public class ExamController {
    private static final String EXAMS_FILE = "data/exams.json";
    private List<Exam> exams;

    public ExamController() {
        exams = loadExams();
    }

    public Exam createExam(String title, int duration, List<String> questionIds) {
        String id = "E" + String.format("%03d", exams.size() + 1);
        Exam exam = new Exam(id, title, duration, questionIds);
        exams.add(exam);
        saveExams();
        return exam;
    }

    public List<Exam> getAllExams() {
        return Collections.unmodifiableList(exams);
    }

    public Exam getById(String id) {
        return exams.stream().filter(e -> e.getExamId().equals(id)).findFirst().orElse(null);
    }

    // ── Persistence ───────────────────────────────────────────────────────
    private List<Exam> loadExams() {
        List<Exam> list = new ArrayList<>();
        String content = FileHandler.readFile(EXAMS_FILE);
        if (content == null || content.trim().isEmpty() || content.equals("[]")) return list;

        // Extract each top-level JSON object
        Matcher m = Pattern.compile("\\{[^{}]*\\}").matcher(content);
        while (m.find()) {
            String entry = m.group().replaceAll("[{}]", "");
            // Extract questionIds array separately before general parse
            List<String> qIds = new ArrayList<>();
            Matcher qm = Pattern.compile("\"questionIds\":\\[([^\\]]*)\\]").matcher(m.group());
            if (qm.find()) {
                String raw = qm.group(1);
                for (String q : raw.split(",")) {
                    qIds.add(q.replaceAll("\"", "").trim());
                }
            }
            Map<String, String> map = FileHandler.parseJsonObject(entry);
            if (map.containsKey("examId")) {
                int dur = Integer.parseInt(map.getOrDefault("durationMinutes", "30"));
                list.add(new Exam(map.get("examId"), map.get("examTitle"), dur, qIds));
            }
        }
        return list;
    }

    private void saveExams() {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < exams.size(); i++) {
            sb.append(exams.get(i).toJson());
            if (i < exams.size() - 1) sb.append(",");
        }
        sb.append("]");
        FileHandler.writeFile(EXAMS_FILE, sb.toString());
    }
}
