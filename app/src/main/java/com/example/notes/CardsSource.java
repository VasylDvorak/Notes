package com.example.notes;

public interface CardsSource {
    CardData getCardData(int position);
    int size();
}
