package question;

public class Question {
    private String questionId;
    private String questionText;
    private String optionA;
    private String optionB;
    private String optionC;
    private String optionD;
    private String correctAnswer; // "A","B","C" or "D"

    public Question(String questionId, String questionText,
                    String optionA, String optionB, String optionC, String optionD,
                    String correctAnswer) {
        this.questionId     = questionId;
        this.questionText   = questionText;
        this.optionA        = optionA;
        this.optionB        = optionB;
        this.optionC        = optionC;
        this.optionD        = optionD;
        this.correctAnswer  = correctAnswer.toUpperCase();
    }

    // Getters
    public String getQuestionId()   { return questionId; }
    public String getQuestionText() { return questionText; }
    public String getOptionA()      { return optionA; }
    public String getOptionB()      { return optionB; }
    public String getOptionC()      { return optionC; }
    public String getOptionD()      { return optionD; }
    public String getCorrectAnswer(){ return correctAnswer; }

    public boolean isCorrect(String answer) {
        return correctAnswer.equalsIgnoreCase(answer.trim());
    }

    public String toJson() {
        return String.format(
            "{\"questionId\":\"%s\",\"questionText\":\"%s\"," +
            "\"optionA\":\"%s\",\"optionB\":\"%s\"," +
            "\"optionC\":\"%s\",\"optionD\":\"%s\"," +
            "\"correctAnswer\":\"%s\"}",
            questionId, escapeJson(questionText),
            escapeJson(optionA), escapeJson(optionB),
            escapeJson(optionC), escapeJson(optionD),
            correctAnswer);
    }

    private String escapeJson(String s) {
        return s.replace("\\", "\\\\").replace("\"", "\\\"");
    }

    @Override
    public String toString() {
        return String.format(
            "[%s] %s\n  A) %s  B) %s  C) %s  D) %s",
            questionId, questionText, optionA, optionB, optionC, optionD);
    }
}
