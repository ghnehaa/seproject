package question;

import utils.FileHandler;
import java.util.*;

public class QuestionController {
    private static final String QUESTIONS_FILE = "data/questions.json";
    private List<Question> questions;

    public QuestionController() {
        questions = loadQuestions();
    }

    public boolean addQuestion(String text, String a, String b, String c, String d, String correct) {
        if (!correct.toUpperCase().matches("[ABCD]")) return false;
        String id = "Q" + String.format("%03d", questions.size() + 1);
        questions.add(new Question(id, text, a, b, c, d, correct));
        saveQuestions();
        return true;
    }

    public List<Question> getAllQuestions() {
        return Collections.unmodifiableList(questions);
    }

    public Question getById(String id) {
        return questions.stream().filter(q -> q.getQuestionId().equals(id)).findFirst().orElse(null);
    }

    public boolean deleteQuestion(String id) {
        boolean removed = questions.removeIf(q -> q.getQuestionId().equals(id));
        if (removed) saveQuestions();
        return removed;
    }

    // ── Persistence ───────────────────────────────────────────────────────
    private List<Question> loadQuestions() {
        List<Question> list = new ArrayList<>();
        String content = FileHandler.readFile(QUESTIONS_FILE);
        if (content == null || content.trim().isEmpty() || content.equals("[]")) return list;
        content = content.trim().replaceAll("^\\[|\\]$", "");
        String[] entries = content.split("\\},\\{");
        for (String entry : entries) {
            entry = entry.replaceAll("^[\\[{]+|[\\]}]+$", "");
            Map<String, String> map = FileHandler.parseJsonObject(entry);
            if (map.containsKey("questionId")) {
                list.add(new Question(
                    map.get("questionId"), map.get("questionText"),
                    map.get("optionA"),    map.get("optionB"),
                    map.get("optionC"),    map.get("optionD"),
                    map.get("correctAnswer")));
            }
        }
        return list;
    }

    private void saveQuestions() {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < questions.size(); i++) {
            sb.append(questions.get(i).toJson());
            if (i < questions.size() - 1) sb.append(",");
        }
        sb.append("]");
        FileHandler.writeFile(QUESTIONS_FILE, sb.toString());
    }
}
