package com.example.notes.model;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.RequiresApi;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

public class Note implements Parcelable {
    public static final Creator<Note> CREATOR = new Creator<Note>() {
        @RequiresApi(api = Build.VERSION_CODES.Q)
        @Override
        public Note createFromParcel(Parcel parcel) {
            return new Note(parcel);
        }

        @Override
        public Note[] newArray(int i) {
            return new Note[i];
        }
    };
    public static final Random random = new Random();
    private static final ArrayList<Note> notes;
    public static int LENGTH_BEGIN = 7;
    private static int counter;

    static {
        notes = new ArrayList<Note>();
        for (int i = 0; i < LENGTH_BEGIN; i++) {
            notes.add(Note.getNote(i));
        }
    }

    public int[] date;
    public int[] time;
    //  private int id;
    public String title;
    String description;
    String creationDate;
    int picture_id;
    boolean like;

    public Note(String title, String description, String creationDate,
                int[] date, int[] time, int picture_id, boolean like) {
        this.title = title;
        this.description = description;
        this.creationDate = creationDate;
        this.date = date;
        this.time = time;
        this.picture_id = picture_id;
        this.like = like;


    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    protected Note(Parcel parcel) {
        title = parcel.readString();
        description = parcel.readString();
        creationDate = parcel.readString();
        date = parcel.createIntArray();
        time = parcel.createIntArray();
        picture_id = parcel.readInt();
        like = parcel.readBoolean();
    }

    public static ArrayList<Note> getNotes() {
        return notes;
    }


    @SuppressLint("DefaultLocale")
    public static Note getNote(int index) {
        String title = String.format("Заметка %d", index);
        String description = String.format("Описание заметки %d", index);
        SimpleDateFormat sdf = new SimpleDateFormat("'Дата\n'dd-MM-yyyy '\n\nи\n\nВремя\n'HH:mm:ss z");
// on below line we are creating a variable
// for current date and time and calling a simple date format in it.

        String currentDateAndTime = sdf.format(new Date());
        int[] date = new int[]{random.nextInt(31) + 1, random.nextInt(11) + 1
                , 2023 + random.nextInt(2)};
        int[] time = new int[]{random.nextInt(24) + 1, random.nextInt(60) + 1};
        return new Note(title, description, currentDateAndTime, date, time, 0, false);
    }

    public static void clearAll() {
        notes.clear();
    }

    public int getId(Note note) {
        return notes.indexOf(note);
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

    public int[] getDate() {
        return date;
    }

    public void setDate(int[] date) {
        this.date = date;
    }

    public int[] getTime() {
        return time;
    }

    public void setTime(int[] time) {
        this.time = time;
    }

    public String getTimeDateAlarm() {
        String date_show, time_show, result_time_date_show, add_zero;
        add_zero = time[1] < 10 ? "0" : "";
        time_show = time[0] + ":" + add_zero + time[1];
        date_show = date[0] + "." + date[1] + "." + date[2];
        result_time_date_show = "Сигнал: Время " + time_show + " Дата " + date_show;
        return result_time_date_show;
    }

    public String getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
    }

    public int getPictureID() {
        return picture_id;
    }

    public void setPictureID(int picture_id) {
        this.picture_id = picture_id;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(getTitle());
        parcel.writeString(getDescription());
        parcel.writeString(getCreationDate());
        parcel.writeIntArray(getDate());
        parcel.writeIntArray(getTime());
        parcel.writeInt(getPictureID());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            parcel.writeBoolean(getLike());
        }
    }

    public boolean getLike() {
        return like;
    }


}
