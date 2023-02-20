package ru.samsung.case2022.retrofit.models;

import com.google.gson.annotations.SerializedName;

/**
 * The User class
 * @author Philipp Schepnov
 * This class is used to simply create, get and store login, name and password
 */

public class User {
    @SerializedName("name")
    String name;

    @SerializedName("login")
    String login;

    @SerializedName("password")
    String password;

    /**
     * Constructor of class
     * Is used to set all variables of this class
     * @param nm is the name of user
     * @param lg is the login of user
     * @param pw is the password of user
     */

    public User(String nm, String lg, String pw) {
        this.name = nm;
        this.login = lg;
        this.password = pw;
    }
}