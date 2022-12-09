package com.example.notes;

import static com.example.notes.Note.LENGTH_BEGIN;

import android.content.res.Resources;
import android.content.res.TypedArray;

import java.util.ArrayList;

public class CardSourceImpl implements CardsSource {

    public static ArrayList<CardData> dataSource;
    protected static int[] pictures_global;
    private final Resources resources;

    public CardSourceImpl(Resources resources) {
        this.resources = resources;
        dataSource = new ArrayList<>(LENGTH_BEGIN);
        pictures_global = getImageArray();

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

    @Override
    public void deleteCardData(int position) {
        dataSource.remove(position);
    }

    @Override
    public void updateCardData(int position, CardData cardData) {
        dataSource.set(position, cardData);
    }

    @Override
    public void addCardData(CardData cardData) {
        dataSource.add(cardData);
    }

    @Override
    public void clearCardData() {
        dataSource.clear();
    }

    @Override
    public void setNewData(ArrayList<CardData> dataSource) {
        CardSourceImpl.dataSource = dataSource;
    }

    @Override
    public ArrayList<CardData> getCardData() {
        return dataSource;
    }

}
