package com.example.notes;

import android.annotation.SuppressLint;
import android.os.Parcel;
import android.os.Parcelable;

import java.time.LocalDateTime;
import java.util.Random;

public class Note implements Parcelable {

    public static final Creator<Note> CREATOR = new Creator<Note>() {
        @Override
        public Note createFromParcel(Parcel parcel) {
            return new Note(parcel);
        }

        @Override
        public Note[] newArray(int i) {
            return new Note[i];
        }
    };
    private static final Random random = new Random();
    private static final Note[] notes;
    private static int counter;

    static {
        notes = new Note[30];
        for (int i = 0; i < notes.length; i++) {
            notes[i] = Note.getNote(i);
        }
    }

    int[] date;
    int[] time;
    private int id;
    private String title;
    private String description;
    private LocalDateTime creationDate;

    {
        id = ++counter;
    }


    public Note(String title, String description, LocalDateTime creationDate, int[] date, int[] time) {
        this.title = title;
        this.description = description;
        this.creationDate = creationDate;
        this.date = date;
        this.time = time;

    }

    protected Note(Parcel parcel) {
        id = parcel.readInt();
        title = parcel.readString();
        description = parcel.readString();
        date = parcel.createIntArray();
        time = parcel.createIntArray();
    }

    public static Note[] getNotes() {
        return notes;
    }

    @SuppressLint("DefaultLocale")
    public static Note getNote(int index) {
        String title = String.format("Заметка %d", index);
        String description = String.format("Описание заметки %d", index);
        LocalDateTime creationDate = LocalDateTime.now().plusDays(-random.nextInt(5));
        int[] date = new int[]{random.nextInt(31) + 1, random.nextInt(11) + 1, 2023 + random.nextInt(2)};
        int[] time = new int[]{random.nextInt(24) + 1, random.nextInt(60) + 1};
        return new Note(title, description, creationDate, date, time);
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTimeDateAlarm() {
        String date_show, time_show, result_time_date_show, add_zero;
        add_zero = time[1] < 10 ? "0" : "";
        time_show = time[0] + ":" + add_zero + time[1];
        date_show = date[0] + "." + date[1] + "." + date[2];
        result_time_date_show = "Сигнал: Время " + time_show + " Дата " + date_show;
        return result_time_date_show;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(getId());
        parcel.writeString(getTitle());
        parcel.writeString(getDescription());
    }


}
