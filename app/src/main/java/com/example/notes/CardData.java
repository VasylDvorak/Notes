package com.example.notes;

public class CardData {
private String title;
private  String description;
private int picture;
private boolean like;
private int id;

    public CardData(String title, String description, int picture,int id, boolean like) {
        this.title = title;
        this.description = description;
        this.picture = picture;
        this.like = like;
        this.id=id;
    }

    public String getTitle() {
        return title;
    }
    public String getDescription() {
        return description;
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
