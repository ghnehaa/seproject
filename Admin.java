package admin;

public class Admin {
    private String adminId;
    private String username;
    private String password;

    public Admin(String adminId, String username, String password) {
        this.adminId = adminId;
        this.username = username;
        this.password = password;
    }

    public String getAdminId()  { return adminId; }
    public String getUsername() { return username; }
    public String getPassword() { return password; }

    public String toJson() {
        return String.format("{\"adminId\":\"%s\",\"username\":\"%s\",\"password\":\"%s\"}",
                adminId, username, password);
    }

    @Override
    public String toString() {
        return "Admin{adminId='" + adminId + "', username='" + username + "'}";
    }
}
