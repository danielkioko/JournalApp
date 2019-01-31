package com.danielkioko.peachnotes;

public class Noter {

    private String title, notes, time;

    public Noter(String title, String notes, String date) {
    }

    public void Noter(String title, String notes, String time) {

        this.title = title;
        this.notes = notes;
        this.time = time;

    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
