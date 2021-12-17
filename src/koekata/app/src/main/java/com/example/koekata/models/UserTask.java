package com.example.koekata.models;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class UserTask {
    public String title;
    public Long start;
    public Long repeat;
    public Long lastDone;

    public UserTask() { }

    public UserTask(String title, Long start, Long repeat, Long lastDone) {
        this.title = title;
        this.start = start;
        this.repeat = repeat;
        this.lastDone = lastDone;
    }

    public UserTask(UserTask other) {
        this.title = other.title;
        this.start = other.start;
        this.repeat = other.repeat;
        this.lastDone = other.lastDone;
    }
}
