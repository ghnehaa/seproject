package exam;

import java.util.List;

public class Exam {
    private String examId;
    private String examTitle;
    private int    durationMinutes;
    private List<String> questionIds;   // ordered list of Question IDs

    public Exam(String examId, String examTitle, int durationMinutes, List<String> questionIds) {
        this.examId          = examId;
        this.examTitle       = examTitle;
        this.durationMinutes = durationMinutes;
        this.questionIds     = questionIds;
    }

    public String       getExamId()          { return examId; }
    public String       getExamTitle()       { return examTitle; }
    public int          getDurationMinutes() { return durationMinutes; }
    public List<String> getQuestionIds()     { return questionIds; }

    public String toJson() {
        StringBuilder ids = new StringBuilder("[");
        for (int i = 0; i < questionIds.size(); i++) {
            ids.append("\"").append(questionIds.get(i)).append("\"");
            if (i < questionIds.size() - 1) ids.append(",");
        }
        ids.append("]");
        return String.format(
            "{\"examId\":\"%s\",\"examTitle\":\"%s\",\"durationMinutes\":%d,\"questionIds\":%s}",
            examId, examTitle, durationMinutes, ids);
    }

    @Override
    public String toString() {
        return String.format("Exam{id='%s', title='%s', questions=%d, duration=%dmin}",
            examId, examTitle, questionIds.size(), durationMinutes);
    }
}
