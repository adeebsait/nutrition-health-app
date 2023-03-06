package com.example.finalyear.common.data.firebasepojo;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class UserInfo {
    private String email;
    private String uuid;
    private String password;
    private UserActivityData activities;

    private Info info;

    public UserInfo() {
    }

    public UserInfo(String email, String uuid, String password) {
        this.email = email;
        this.uuid = uuid;
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public UserActivityData getActivities() {
        return activities;
    }

    public String getUuid() {
        return uuid;
    }

    public String getEmail() {
        return email;
    }

    public Info getInfo() {
        return info;
    }

    @Override
    public String toString() {
        return "UserInfo{" +
                "email='" + email + '\'' +
                ", uuid='" + uuid + '\'' +
                ", password='" + password + '\'' +
                ", activities=" + activities +
                ", info=" + info +
                '}';
    }
}