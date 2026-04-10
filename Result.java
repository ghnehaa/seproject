package result;

public class Result {
    private String resultId;
    private String studentId;
    private String studentName;
    private String examId;
    private String examTitle;
    private int    score;
    private int    totalQuestions;
    private String grade;

    public Result(String resultId, String studentId, String studentName,
                  String examId, String examTitle, int score, int totalQuestions) {
        this.resultId       = resultId;
        this.studentId      = studentId;
        this.studentName    = studentName;
        this.examId         = examId;
        this.examTitle      = examTitle;
        this.score          = score;
        this.totalQuestions = totalQuestions;
        this.grade          = calculateGrade(score, totalQuestions);
    }

    private String calculateGrade(int score, int total) {
        if (total == 0) return "N/A";
        double pct = (double) score / total * 100;
        if (pct >= 90) return "A+";
        if (pct >= 80) return "A";
        if (pct >= 70) return "B";
        if (pct >= 60) return "C";
        if (pct >= 50) return "D";
        return "F";
    }

    // Getters
    public String getResultId()      { return resultId; }
    public String getStudentId()     { return studentId; }
    public String getStudentName()   { return studentName; }
    public String getExamId()        { return examId; }
    public String getExamTitle()     { return examTitle; }
    public int    getScore()         { return score; }
    public int    getTotalQuestions(){ return totalQuestions; }
    public String getGrade()         { return grade; }
    public double getPercentage()    { return totalQuestions > 0 ? (double) score / totalQuestions * 100 : 0; }

    public String toJson() {
        return String.format(
            "{\"resultId\":\"%s\",\"studentId\":\"%s\",\"studentName\":\"%s\"," +
            "\"examId\":\"%s\",\"examTitle\":\"%s\",\"score\":%d,\"totalQuestions\":%d,\"grade\":\"%s\"}",
            resultId, studentId, studentName, examId, examTitle, score, totalQuestions, grade);
    }

    @Override
    public String toString() {
        return String.format(
            "Result{student='%s', exam='%s', score=%d/%d (%.1f%%), grade='%s'}",
            studentName, examTitle, score, totalQuestions, getPercentage(), grade);
    }
}
