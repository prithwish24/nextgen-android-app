package com.nextgen.carrental.app.model;

/**
 * Created by prith on 4/10/2018.
 */

public class User {
    private String userId;
    private String name;
    private String nickname;
    private String email;

    public User() {
    }

    public User(String userId, String name, String nickname, String email) {
        this.userId = userId;
        this.name = name;
        this.nickname = nickname;
        this.email = email;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
