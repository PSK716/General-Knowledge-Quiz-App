package com.example.quizzy.Model;

public class User {
    public String name;
    public User()
    {

    }
    public User(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}