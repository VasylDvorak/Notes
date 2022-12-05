package com.example.notes;

import static com.example.notes.Note.LENGTH_BEGIN;

import android.content.res.Resources;
import android.content.res.TypedArray;

import java.util.ArrayList;
import java.util.List;

public class CardSourceImpl implements CardsSource {

    protected static int[] pictures_global;
    private final List<CardData> dataSource;
    private final Resources resources;

    public CardSourceImpl(Resources resources) {
        this.resources = resources;
        dataSource = new ArrayList<>(LENGTH_BEGIN);
    }

    public CardSourceImpl init() {
        pictures_global = getImageArray();
        if (Note.getNotes().size() != 0)
            for (int i = 0; i < Note.getNotes().size(); i++) {
                dataSource.add(new CardData(Note.getNotes().get(i).getTitle(),
                        Note.getNotes().get(i).getDescription(), pictures_global[i],
                        Note.getNotes().get(i).getId(Note.getNotes().get(i)), false));
                ArrayList<Note> notesd = Note.getNotes();
                Note notef = notesd.get(i);
                notef.picture_id = pictures_global[i];
                Note.getNotes().set(i, notef);
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
}
