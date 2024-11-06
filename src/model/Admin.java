package model;

public class Admin extends User {
    private String role;

    public Admin(int userId, String username, String password, String email, String role) {
        super(userId, username, password, email);
        this.role = role;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    @Override
    public String getAccessLevel() {
        return "Admin";
    }

}
