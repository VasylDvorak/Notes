package com.example.notes.view;

import static com.example.notes.presenter.ParentDateTimeFragment.SELECTED_NOTE;

import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.notes.R;
import com.example.notes.model.Note;

import java.util.ArrayList;


public class DatePickerFragment extends Fragment {
    private View layout;
    private LayoutInflater inflator;

    public DatePickerFragment() {
        // Required empty public constructor
    }

    public static DatePickerFragment newInstance(Note note) {
        DatePickerFragment fragment = new DatePickerFragment();
        Bundle args = new Bundle();
        args.putParcelable(SELECTED_NOTE, note);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_date_picker, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Bundle arguments = getArguments();
        // System.out.println(arguments + "date");
        if (arguments != null) {
            Note note = arguments.getParcelable(SELECTED_NOTE);
            DatePicker datePicker = view.findViewById(R.id.datePicker);
            if (!isLandscape()) {
                datePicker.setSpinnersShown(false);
                datePicker.setCalendarViewShown(true);
            }
            datePicker.init(note.date[2], note.date[1] - 1, note.date[0], new DatePicker.OnDateChangedListener() {

                @Override
                public void onDateChanged(DatePicker view_date, int year, int monthOfYear, int dayOfMonth) {
                    note.date[0] = view_date.getDayOfMonth();
                    note.date[1] = view_date.getMonth() + 1;
                    note.date[2] = view_date.getYear();
                    ArrayList<Note> notesd = Note.getNotes();
                    int indexd = notesd.indexOf(note);
                    Note.getNotes().set(indexd, note);

                    inflator = getLayoutInflater();
                    layout = inflator.inflate(R.layout.fragment_note,
                            getActivity().findViewById(R.id.linear_layout_note));
                    TextView time_date_alarm = layout.findViewById(R.id.time_date_alarm_view);
                    time_date_alarm.setText(note.getTimeDateAlarm());
                }
            });


        }
    }


    private boolean isLandscape() {
        return getResources().getConfiguration().orientation
                == Configuration.ORIENTATION_LANDSCAPE;
    }
}