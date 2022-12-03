package com.example.notes;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class DialogHeaderDrawerFragment extends DialogFragment {
    private OnDialogListener dialogListener;

    public static DialogHeaderDrawerFragment newInstance() {
        return new DialogHeaderDrawerFragment();
    }

    public void setOnDialogListener(OnDialogListener dialogListener) {
        this.dialogListener = dialogListener;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_drawer_header_text_dialog, container, false);
// Запретим пользователю выходить без выбора
        setCancelable(false);
        view.findViewById(R.id.btn_name).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
                if (dialogListener != null) dialogListener.onDialogName();
            }
        });
        view.findViewById(R.id.btn_profession).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dismiss();
                if (dialogListener != null) dialogListener.onDialogProfession();
            }
        });
        return view;
    }
}
