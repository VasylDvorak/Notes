package com.example.notes.view;

import static com.example.notes.view.NoteFragment.SELECTED_NOTE;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.notes.R;
import com.example.notes.model.Note;

import java.util.ArrayList;

public class TimePickerFragment extends Fragment {
    private View layout;
    private LayoutInflater inflator;

    public TimePickerFragment() {
        // Required empty public constructor
    }

    public static TimePickerFragment newInstance(Note note) {
        TimePickerFragment fragment = new TimePickerFragment();
        Bundle args = new Bundle();
        args.putParcelable(SELECTED_NOTE, note);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_time_picker, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Bundle arguments = getArguments();

        if (arguments != null) {
            Note note = arguments.getParcelable(SELECTED_NOTE);
            TimePicker timePicker = view.findViewById(R.id.timePicker);
            timePicker.setIs24HourView(true);
            timePicker.setHour(note.time[0]);
            timePicker.setMinute(note.time[1]);
            timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
                @Override
                public void onTimeChanged(TimePicker view_time, int hourOfDay, int minute) {
                    note.time[0] = hourOfDay;
                    note.time[1] = minute;
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
}
