package exam;

import question.Question;
import java.util.*;

/**
 * EvaluationEngine — stateless evaluation logic.
 * Compares student answers against correct answers and returns a score.
 */
public class EvaluationEngine {

    /**
     * Evaluates a set of answers against the given questions.
     *
     * @param questions     ordered list of questions in the exam
     * @param studentAnswers map of questionId → student's chosen option (A/B/C/D)
     * @return score (number of correct answers)
     */
    public static int evaluate(List<Question> questions, Map<String, String> studentAnswers) {
        int score = 0;
        for (Question q : questions) {
            String given = studentAnswers.get(q.getQuestionId());
            if (given != null && q.isCorrect(given)) {
                score++;
            }
        }
        return score;
    }

    /**
     * Produces a detailed per-question report string.
     */
    public static String generateReport(List<Question> questions, Map<String, String> studentAnswers) {
        StringBuilder sb = new StringBuilder();
        int idx = 1;
        for (Question q : questions) {
            String given   = studentAnswers.getOrDefault(q.getQuestionId(), "-");
            boolean correct = q.isCorrect(given);
            sb.append(String.format(
                "  Q%d: %-45s | Your: %s | Correct: %s | %s%n",
                idx++,
                truncate(q.getQuestionText(), 45),
                given.toUpperCase(),
                q.getCorrectAnswer(),
                correct ? "CORRECT" : "WRONG"));
        }
        return sb.toString();
    }

    private static String truncate(String s, int maxLen) {
        return s.length() <= maxLen ? s : s.substring(0, maxLen - 3) + "...";
    }
}
