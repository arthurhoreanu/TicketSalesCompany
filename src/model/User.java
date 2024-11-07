package model;

public abstract class User implements Identifiable {
    private int userId;
    protected String username;
    protected String password;
    private String email;

    public User(int userId, String username, String password, String email) {
        this.userId = userId;
        this.username = username;
        this.password = password;
        this.email = email;
    }

    @Override
    public Integer getID(){
        return this.userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public abstract String getAccessLevel();

    @Override
    public String toString() {
        return "User [userId=" + userId + ", username=" + username + ", password=" + password + ", email=" + email + "}" ;
    }
}

