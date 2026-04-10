import admin.*;
import student.*;
import exam.*;
import question.*;
import result.*;
import utils.*;

import java.util.*;

/**
 * ─────────────────────────────────────────────────────────────────────────────
 * Online Examination & Evaluation System
 * Jaypee Institute of Information Technology, Noida
 * Group: Aneesha Siby | Ishita Saraswat | Neha Sharma
 * ─────────────────────────────────────────────────────────────────────────────
 */
public class Main {

    private static final Scanner sc = new Scanner(System.in);

    // Controllers (dependency injection style)
    private static AdminController    adminCtrl;
    private static StudentController  studentCtrl;
    private static QuestionController questionCtrl;
    private static ExamController     examCtrl;
    private static ResultController   resultCtrl;

    public static void main(String[] args) {
        initControllers();
        Display.printBanner("ONLINE EXAMINATION & EVALUATION SYSTEM");
        System.out.println("  Jaypee Institute of Information Technology, Noida");

        boolean running = true;
        while (running) {
            Display.printSectionHeader("MAIN MENU");
            System.out.println("  1. Admin Login");
            System.out.println("  2. Student Login");
            System.out.println("  3. Exit");
            System.out.print("\n  Enter choice: ");
            String choice = sc.nextLine().trim();

            switch (choice) {
                case "1" -> adminMenu();
                case "2" -> studentMenu();
                case "3" -> {
                    Display.printInfo("Thank you for using the system. Goodbye!");
                    running = false;
                }
                default  -> Display.printError("Invalid choice. Please enter 1, 2, or 3.");
            }
        }
    }

    // ═══════════════════════════════════════════════════════════════════
    //  INITIALISATION
    // ═══════════════════════════════════════════════════════════════════
    private static void initControllers() {
        adminCtrl    = new AdminController();
        studentCtrl  = new StudentController();
        questionCtrl = new QuestionController();
        examCtrl     = new ExamController();
        resultCtrl   = new ResultController();
    }

    // ═══════════════════════════════════════════════════════════════════
    //  ADMIN FLOW
    // ═══════════════════════════════════════════════════════════════════
    private static void adminMenu() {
        Display.printSectionHeader("ADMIN LOGIN");
        System.out.print("  Username: ");
        String u = sc.nextLine().trim();
        System.out.print("  Password: ");
        String p = sc.nextLine().trim();

        Admin admin = adminCtrl.login(u, p);
        if (admin == null) {
            Display.printError("Invalid credentials. Access denied.");
            return;
        }
        Display.printSuccess("Welcome, Admin " + admin.getUsername() + "!");

        boolean back = false;
        while (!back) {
            Display.printSectionHeader("ADMIN PANEL");
            System.out.println("  1. Add Student");
            System.out.println("  2. View All Students");
            System.out.println("  3. Add Question");
            System.out.println("  4. View All Questions");
            System.out.println("  5. Create Exam");
            System.out.println("  6. View All Exams");
            System.out.println("  7. View All Results");
            System.out.println("  8. Logout");
            System.out.print("\n  Enter choice: ");
            String choice = sc.nextLine().trim();

            switch (choice) {
                case "1" -> addStudent();
                case "2" -> viewStudents();
                case "3" -> addQuestion();
                case "4" -> viewQuestions();
                case "5" -> createExam();
                case "6" -> viewExams();
                case "7" -> viewAllResults();
                case "8" -> { back = true; Display.printInfo("Admin logged out."); }
                default  -> Display.printError("Invalid choice.");
            }
        }
    }

    private static void addStudent() {
        Display.printSectionHeader("ADD STUDENT");
        System.out.print("  Full Name : ");
        String name = sc.nextLine().trim();
        System.out.print("  Username  : ");
        String user = sc.nextLine().trim();
        System.out.print("  Password  : ");
        String pass = sc.nextLine().trim();

        if (studentCtrl.addStudent(name, user, pass)) {
            Display.printSuccess("Student '" + name + "' added successfully.");
        } else {
            Display.printError("Username '" + user + "' already exists.");
        }
    }

    private static void viewStudents() {
        Display.printSectionHeader("ALL STUDENTS");
        List<Student> list = studentCtrl.getAllStudents();
        if (list.isEmpty()) { Display.printInfo("No students registered yet."); return; }
        System.out.printf("  %-8s %-20s %-15s%n", "ID", "Name", "Username");
        Display.printDivider();
        list.forEach(s -> System.out.printf("  %-8s %-20s %-15s%n",
            s.getStudentId(), s.getName(), s.getUsername()));
    }

    private static void addQuestion() {
        Display.printSectionHeader("ADD QUESTION");
        System.out.print("  Question Text : ");
        String text = sc.nextLine().trim();
        System.out.print("  Option A      : ");
        String a = sc.nextLine().trim();
        System.out.print("  Option B      : ");
        String b = sc.nextLine().trim();
        System.out.print("  Option C      : ");
        String c = sc.nextLine().trim();
        System.out.print("  Option D      : ");
        String d = sc.nextLine().trim();
        System.out.print("  Correct Answer (A/B/C/D): ");
        String ans = sc.nextLine().trim();

        if (questionCtrl.addQuestion(text, a, b, c, d, ans)) {
            Display.printSuccess("Question added successfully.");
        } else {
            Display.printError("Invalid correct-answer option. Must be A, B, C, or D.");
        }
    }

    private static void viewQuestions() {
        Display.printSectionHeader("QUESTION BANK");
        List<Question> list = questionCtrl.getAllQuestions();
        if (list.isEmpty()) { Display.printInfo("No questions in the bank yet."); return; }
        list.forEach(q -> {
            System.out.println("\n  " + q.getQuestionId() + ". " + q.getQuestionText());
            System.out.printf("     A) %-20s  B) %-20s%n", q.getOptionA(), q.getOptionB());
            System.out.printf("     C) %-20s  D) %-20s%n", q.getOptionC(), q.getOptionD());
            System.out.println("     Correct: " + q.getCorrectAnswer());
        });
    }

    private static void createExam() {
        Display.printSectionHeader("CREATE EXAM");
        List<Question> all = questionCtrl.getAllQuestions();
        if (all.isEmpty()) { Display.printError("No questions available. Add questions first."); return; }

        System.out.print("  Exam Title        : ");
        String title = sc.nextLine().trim();
        System.out.print("  Duration (minutes): ");
        int duration;
        try { duration = Integer.parseInt(sc.nextLine().trim()); }
        catch (NumberFormatException e) { Display.printError("Invalid duration."); return; }

        System.out.println("\n  Available Question IDs:");
        all.forEach(q -> System.out.println("    " + q.getQuestionId() + " - " + truncate(q.getQuestionText(), 50)));
        System.out.print("\n  Enter question IDs separated by commas: ");
        String[] ids = sc.nextLine().trim().split(",");
        List<String> selected = new ArrayList<>();
        for (String id : ids) {
            String trimmed = id.trim().toUpperCase();
            if (questionCtrl.getById(trimmed) != null) {
                selected.add(trimmed);
            } else {
                Display.printError("Question ID not found: " + trimmed);
            }
        }
        if (selected.isEmpty()) { Display.printError("No valid questions selected."); return; }

        Exam exam = examCtrl.createExam(title, duration, selected);
        Display.printSuccess("Exam '" + title + "' created with ID: " + exam.getExamId());
    }

    private static void viewExams() {
        Display.printSectionHeader("ALL EXAMS");
        List<Exam> list = examCtrl.getAllExams();
        if (list.isEmpty()) { Display.printInfo("No exams created yet."); return; }
        System.out.printf("  %-8s %-30s %-10s %-10s%n", "ID", "Title", "Questions", "Duration");
        Display.printDivider();
        list.forEach(e -> System.out.printf("  %-8s %-30s %-10d %-10d min%n",
            e.getExamId(), e.getExamTitle(), e.getQuestionIds().size(), e.getDurationMinutes()));
    }

    private static void viewAllResults() {
        Display.printSectionHeader("ALL STUDENT RESULTS");
        List<Result> list = resultCtrl.getAllResults();
        if (list.isEmpty()) { Display.printInfo("No results recorded yet."); return; }
        System.out.printf("  %-8s %-20s %-25s %-10s %-6s%n", "ID", "Student", "Exam", "Score", "Grade");
        Display.printDivider();
        list.forEach(r -> System.out.printf("  %-8s %-20s %-25s %-10s %-6s%n",
            r.getResultId(), r.getStudentName(), r.getExamTitle(),
            r.getScore() + "/" + r.getTotalQuestions(), r.getGrade()));
    }

    // ═══════════════════════════════════════════════════════════════════
    //  STUDENT FLOW
    // ═══════════════════════════════════════════════════════════════════
    private static void studentMenu() {
        Display.printSectionHeader("STUDENT LOGIN");
        System.out.print("  Username: ");
        String u = sc.nextLine().trim();
        System.out.print("  Password: ");
        String p = sc.nextLine().trim();

        Student student = studentCtrl.login(u, p);
        if (student == null) {
            Display.printError("Invalid credentials. Access denied.");
            return;
        }
        Display.printSuccess("Welcome, " + student.getName() + "!");

        boolean back = false;
        while (!back) {
            Display.printSectionHeader("STUDENT MENU");
            System.out.println("  1. View Available Exams");
            System.out.println("  2. Attempt Exam");
            System.out.println("  3. View My Results");
            System.out.println("  4. Logout");
            System.out.print("\n  Enter choice: ");
            String choice = sc.nextLine().trim();

            switch (choice) {
                case "1" -> viewExams();
                case "2" -> attemptExam(student);
                case "3" -> viewMyResults(student);
                case "4" -> { back = true; Display.printInfo("Logged out successfully."); }
                default  -> Display.printError("Invalid choice.");
            }
        }
    }

    private static void attemptExam(Student student) {
        Display.printSectionHeader("ATTEMPT EXAM");
        List<Exam> exams = examCtrl.getAllExams();
        if (exams.isEmpty()) { Display.printInfo("No exams available at this time."); return; }

        System.out.println("  Available Exams:");
        exams.forEach(e -> System.out.printf("  %-8s %s (%d min)%n",
            e.getExamId(), e.getExamTitle(), e.getDurationMinutes()));
        System.out.print("\n  Enter Exam ID: ");
        String examId = sc.nextLine().trim().toUpperCase();

        Exam exam = examCtrl.getById(examId);
        if (exam == null) { Display.printError("Exam not found."); return; }

        List<Question> questions = new ArrayList<>();
        for (String qid : exam.getQuestionIds()) {
            Question q = questionCtrl.getById(qid);
            if (q != null) questions.add(q);
        }
        if (questions.isEmpty()) { Display.printError("This exam has no valid questions."); return; }

        Display.printBanner("EXAM: " + exam.getExamTitle());
        System.out.printf("  Duration: %d minutes | Questions: %d%n%n",
            exam.getDurationMinutes(), questions.size());

        Map<String, String> answers = new LinkedHashMap<>();
        int idx = 1;
        for (Question q : questions) {
            System.out.println("  Q" + idx++ + ". " + q.getQuestionText());
            System.out.printf("     A) %-20s  B) %-20s%n", q.getOptionA(), q.getOptionB());
            System.out.printf("     C) %-20s  D) %-20s%n", q.getOptionC(), q.getOptionD());
            String ans = "";
            while (!ans.matches("[AaBbCcDd]")) {
                System.out.print("  Your Answer (A/B/C/D): ");
                ans = sc.nextLine().trim();
                if (!ans.matches("[AaBbCcDd]"))
                    Display.printError("Please enter A, B, C, or D.");
            }
            answers.put(q.getQuestionId(), ans.toUpperCase());
            System.out.println();
        }

        // ── Evaluate ──
        int score = EvaluationEngine.evaluate(questions, answers);
        Result result = resultCtrl.saveResult(
            student.getStudentId(), student.getName(),
            exam.getExamId(), exam.getExamTitle(),
            score, questions.size());

        // ── Show result ──
        Display.printBanner("EXAM RESULT");
        System.out.println("  Student : " + student.getName());
        System.out.println("  Exam    : " + exam.getExamTitle());
        System.out.printf("  Score   : %d / %d (%.1f%%)%n", score, questions.size(), result.getPercentage());
        System.out.println("  Grade   : " + result.getGrade());
        Display.printDivider();
        System.out.println("\n  Detailed Report:");
        System.out.print(EvaluationEngine.generateReport(questions, answers));
    }

    private static void viewMyResults(Student student) {
        Display.printSectionHeader("MY RESULTS");
        List<Result> list = resultCtrl.getResultsByStudent(student.getStudentId());
        if (list.isEmpty()) { Display.printInfo("You have not attempted any exams yet."); return; }
        System.out.printf("  %-8s %-25s %-10s %-8s %-6s%n", "ID", "Exam", "Score", "Percent", "Grade");
        Display.printDivider();
        list.forEach(r -> System.out.printf("  %-8s %-25s %-10s %-8s %-6s%n",
            r.getResultId(), r.getExamTitle(),
            r.getScore() + "/" + r.getTotalQuestions(),
            String.format("%.1f%%", r.getPercentage()),
            r.getGrade()));
    }

    private static String truncate(String s, int max) {
        return s.length() <= max ? s : s.substring(0, max - 3) + "...";
    }
}
