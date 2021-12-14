package com.example.koekata.models;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class UserEvent {
    public String title;
    public String description;
    public Long date;

    public UserEvent() { }

    public UserEvent(String title, String description, Long date) {
        this.title = title;
        this.description = description;
        this.date = date;
    }
}
