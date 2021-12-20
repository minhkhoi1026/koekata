package com.example.koekata.models;

import com.google.firebase.database.IgnoreExtraProperties;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

@IgnoreExtraProperties
public class UserEvent {
    public String title;
    public String description;
    public Long date;
    private SimpleDateFormat fmt = new SimpleDateFormat("yyyyMMdd", Locale.getDefault());

    public UserEvent() { }

    public UserEvent(String title, String description, Long date) {
        this.title = title;
        this.description = description;
        this.date = date;
    }

    public boolean equals(UserEvent other) {
        Date thisDate = new Date(this.date);
        Date otherDate = new Date(other.date);
        return other.title.equals(title)
                && other.description.equals(description)
                && fmt.format(thisDate).equals(fmt.format(otherDate));
    }
}
