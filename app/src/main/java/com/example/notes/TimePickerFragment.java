package com.example.notes;

import static com.example.notes.NoteFragment.SELECTED_NOTE;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class TimePickerFragment extends Fragment {

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
                }
            });
        }
    }
}
