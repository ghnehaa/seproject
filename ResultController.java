package result;

import utils.FileHandler;
import java.util.*;

public class ResultController {
    private static final String RESULTS_FILE = "data/results.json";
    private List<Result> results;

    public ResultController() {
        results = loadResults();
    }

    public Result saveResult(String studentId, String studentName,
                             String examId, String examTitle,
                             int score, int total) {
        String id = "R" + String.format("%03d", results.size() + 1);
        Result r = new Result(id, studentId, studentName, examId, examTitle, score, total);
        results.add(r);
        saveResults();
        return r;
    }

    public List<Result> getResultsByStudent(String studentId) {
        List<Result> out = new ArrayList<>();
        for (Result r : results)
            if (r.getStudentId().equals(studentId)) out.add(r);
        return out;
    }

    public List<Result> getAllResults() {
        return Collections.unmodifiableList(results);
    }

    private List<Result> loadResults() {
        List<Result> list = new ArrayList<>();
        String content = FileHandler.readFile(RESULTS_FILE);
        if (content == null || content.trim().isEmpty() || content.equals("[]")) return list;
        content = content.trim().replaceAll("^\\[|\\]$", "");
        String[] entries = content.split("\\},\\{");
        for (String entry : entries) {
            entry = entry.replaceAll("[\\[\\]{}]", "");
            Map<String, String> map = FileHandler.parseJsonObject(entry);
            if (map.containsKey("resultId")) {
                list.add(new Result(
                    map.get("resultId"),  map.get("studentId"), map.get("studentName"),
                    map.get("examId"),    map.get("examTitle"),
                    Integer.parseInt(map.getOrDefault("score", "0")),
                    Integer.parseInt(map.getOrDefault("totalQuestions", "0"))));
            }
        }
        return list;
    }

    private void saveResults() {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < results.size(); i++) {
            sb.append(results.get(i).toJson());
            if (i < results.size() - 1) sb.append(",");
        }
        sb.append("]");
        FileHandler.writeFile(RESULTS_FILE, sb.toString());
    }
}
