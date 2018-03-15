package com.linkedin.replica.signUpInOut.models;


public class User {
    private String id;
    private String email;
    private String password;


    public String getPassword() {
        return password;
    }

    public String getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public User(String id, String email, String password) {
        this.id = id;
        this.email = email;
        this.password = password;
    }

    public User(String email, String password) {
        this.email = email;
        this.password = password;
    }
}
