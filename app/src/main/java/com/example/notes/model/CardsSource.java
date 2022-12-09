package com.example.notes.model;

import java.util.ArrayList;

public interface CardsSource {
    CardData getCardData(int position);

    int size();

    void deleteCardData(int position);

    void updateCardData(int position, CardData cardData);

    void addCardData(CardData cardData);

    void clearCardData();

    void setNewData(ArrayList<CardData> dataSource);

    ArrayList<CardData> getCardData();
}
