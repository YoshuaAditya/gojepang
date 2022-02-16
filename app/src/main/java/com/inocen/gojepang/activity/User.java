package com.inocen.gojepang.activity;

public class User {

    public String username;
    public String email;
    public String phone;
    public int age;
    public int lastChapter;
    public int lastKanji;
    public int lastKanjiTest;
    public int lastJlpt;

    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public User(String username, String email, String phone, int age) {
        this.username = username;
        this.email = email;
        this.phone = phone;
        this.age = age;
        this.lastChapter=0;
        this.lastKanji=0;
        this.lastKanjiTest=0;
        this.lastJlpt=0;
    }

}