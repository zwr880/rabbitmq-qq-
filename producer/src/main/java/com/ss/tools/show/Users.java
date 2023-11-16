package com.ss.tools.show;

public class Users {
    private String username;
    private String avatarUrl;

    public Users(String username, String avatarUrl) {
        this.username = username;
        this.avatarUrl = avatarUrl;
    }

    public String getUsername() {
        return username;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    @Override
    public String toString() {
        return username;
    }
}

