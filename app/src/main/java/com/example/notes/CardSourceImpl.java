package com.example.notes;

import android.content.res.Resources;
import android.content.res.TypedArray;

import java.util.ArrayList;
import java.util.List;

public class CardSourceImpl implements CardsSource {

    private final List<CardData> dataSource;
    private final Resources resources;

    public CardSourceImpl(Resources resources) {
        this.resources = resources;
        dataSource = new ArrayList<>(7);

    }

    public CardSourceImpl init() {
        int[] pictures = getImageArray();

        for (int i = 0; i < 7; i++) {
            dataSource.add(new CardData(Note.getNotes()[i].getTitle(), Note.getNotes()[i].getDescription(), pictures[i], Note.getNotes()[i].getId(), false));
        }

        return this;
    }


    private int[] getImageArray() {
        TypedArray pictures = resources.obtainTypedArray(R.array.pictures);
        int length = pictures.length();
        int[] answer = new int[length];
        for (int i = 0; i < length; i++) {
            answer[i] = pictures.getResourceId(i, 0);
        }
        return answer;
    }


    @Override
    public CardData getCardData(int position) {
        return dataSource.get(position);
    }

    @Override
    public int size() {
        return dataSource.size();
    }
}
