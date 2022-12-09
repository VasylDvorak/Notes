package com.example.notes.model;

public class CardData {
    private final String title;
    private final String description;
    private final String currentDateAndTime;
    private final int picture;
    private final boolean like;
    private final int id;

    public CardData(String title, String description, String currentDateAndTime,
                    int picture, int id, boolean like) {
        this.title = title;
        this.description = description;
        this.picture = picture;
        this.like = like;
        this.id = id;
        this.currentDateAndTime = currentDateAndTime;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getCurrentDateAndTime() {
        return currentDateAndTime;
    }

    public int getPicture() {
        return picture;
    }

    public int getId() {
        return id;
    }

    public boolean isLike() {
        return like;
    }
}
