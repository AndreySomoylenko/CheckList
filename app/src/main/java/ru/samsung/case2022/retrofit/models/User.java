package ru.samsung.case2022.retrofit.models;

import com.google.gson.annotations.SerializedName;

public class User {
    @SerializedName("name")
    String name;

    @SerializedName("login")
    String login;

    @SerializedName("password")
    String password;

    public User(String nm, String lg, String pw) {
        this.name = nm;
        this.login = lg;
        this.password = pw;
    }
}
