package edu.icet.model;

public class User {

    private int user_id;
    private String username;
    private String password_hash;

    public User() {
    }

    public User(int user_id, String username, String password_hash) {
        this.user_id = user_id;
        this.username = username;
        this.password_hash = password_hash;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword_hash() {
        return password_hash;
    }

    public void setPassword_hash(String password_hash) {
        this.password_hash = password_hash;
    }

}
