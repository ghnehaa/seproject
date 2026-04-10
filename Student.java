package student;

public class Student {
    private String studentId;
    private String username;
    private String password;
    private String name;

    public Student(String studentId, String username, String password, String name) {
        this.studentId = studentId;
        this.username  = username;
        this.password  = password;
        this.name      = name;
    }

    public String getStudentId() { return studentId; }
    public String getUsername()  { return username; }
    public String getPassword()  { return password; }
    public String getName()      { return name; }

    public String toJson() {
        return String.format(
            "{\"studentId\":\"%s\",\"username\":\"%s\",\"password\":\"%s\",\"name\":\"%s\"}",
            studentId, username, password, name);
    }

    @Override
    public String toString() {
        return String.format("Student{id='%s', name='%s', username='%s'}", studentId, name, username);
    }
}
