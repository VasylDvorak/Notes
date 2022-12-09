package com.example.notes.presenter;

import android.widget.TextView;

public interface OnDialogListener {
    void onDialogName();

    void onDialogProfession();

    String CorrectText(TextView textView);
}
