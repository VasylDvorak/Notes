package com.example.notes;

public class NotesKeepingClass {
    String name;
    String description;
    int[] date;
    int[] time;

    protected NotesKeepingClass(String name, String description, int[] date, int[] time) {
        this.name = name;
        this.description = description;
        this.date = date;
        this.time = time;
    }
}
