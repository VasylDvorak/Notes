package com.example.notes;

import static com.example.notes.MainActivity.ARG_INDEX;
import static com.example.notes.MainActivity.tree_notes;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class NotesFragment extends Fragment {


    // Фабричный метод создания фрагмента
    // Фрагменты рекомендуется создавать через фабричные методы
    public static NotesFragment newInstance(String index) {
        // Создание фрагмента
        NotesFragment fragment = new NotesFragment();
        // Передача параметра
        Bundle args = new Bundle();
        args.putString(ARG_INDEX, index);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_notes, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Bundle arguments = getArguments();
        // Аргументы могут быть null (как в случае с методом Activity getIntent())
        // поэтому обязательно проверяем на null
        if (arguments != null) {
            String index = arguments.getString(ARG_INDEX, tree_notes.firstKey());
            DatePicker datePicker = view.findViewById(R.id.datePicker);
            TimePicker timePicker = view.findViewById(R.id.timePicker);
            TextView text_note = view.findViewById(R.id.notes_date_landscape);


            text_note.setText(tree_notes.get(index).date[0] + "." + tree_notes.get(index).date[1] + "." + tree_notes.get(index).date[2]);
            text_note.setOnClickListener(v -> {
                LinearLayout date_time_changer = view.findViewById(R.id.change_date_time);
                date_time_changer.setVisibility(LinearLayout.VISIBLE);
                timePicker.setVisibility(TimePicker.GONE);
                datePicker.setVisibility(DatePicker.VISIBLE);
                datePicker.init(tree_notes.get(index).date[2], tree_notes.get(index).date[1], tree_notes.get(index).date[0], new DatePicker.OnDateChangedListener() {

                    @Override
                    public void onDateChanged(DatePicker view_date, int year, int monthOfYear, int dayOfMonth) {
                        tree_notes.get(index).date[0] = view_date.getDayOfMonth();
                        tree_notes.get(index).date[1] = view_date.getMonth() + 1;
                        tree_notes.get(index).date[2] = view_date.getYear();
                        TextView text_date_change = view.findViewById(R.id.notes_date_landscape);
                        text_date_change.setText(tree_notes.get(index).date[0] + "." + tree_notes.get(index).date[1] + "." + tree_notes.get(index).date[2]);
                    }
                });
                Button button_ok = view.findViewById(R.id.date_time_Button);
                button_ok.setOnClickListener(v1 -> {
                    datePicker.setVisibility(DatePicker.GONE);
                    date_time_changer.setVisibility(LinearLayout.GONE);
                });
            });

            text_note = view.findViewById(R.id.notes_time_landscape);
            String add_zero = tree_notes.get(index).time[1] < 10 ? "0" : "";
            text_note.setText(tree_notes.get(index).time[0] + ":" + add_zero + tree_notes.get(index).time[1]);
            text_note.setOnClickListener(v -> {
                LinearLayout date_time_changer = view.findViewById(R.id.change_date_time);
                date_time_changer.setVisibility(LinearLayout.VISIBLE);
                timePicker.setIs24HourView(true);
                timePicker.setHour(tree_notes.get(index).time[0]);
                timePicker.setMinute(tree_notes.get(index).time[1]);
                datePicker.setVisibility(DatePicker.GONE);
                timePicker.setVisibility(TimePicker.VISIBLE);
                timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
                    @Override
                    public void onTimeChanged(TimePicker view_time, int hourOfDay, int minute) {
                        tree_notes.get(index).time[0] = hourOfDay;
                        tree_notes.get(index).time[1] = minute;
                        TextView text_time_change = view.findViewById(R.id.notes_time_landscape);
                        String add_zero_2 = tree_notes.get(index).time[1] < 10 ? "0" : "";
                        text_time_change.setText(tree_notes.get(index).time[0] + ":" + add_zero_2 + tree_notes.get(index).time[1]);
                    }
                });
                Button button_ok = view.findViewById(R.id.date_time_Button);
                button_ok.setOnClickListener(v1 -> {
                    timePicker.setVisibility(TimePicker.GONE);
                    date_time_changer.setVisibility(LinearLayout.GONE);
                });
            });

            text_note = view.findViewById(R.id.notes_name_landscape);
            text_note.setText(index);
            text_note = view.findViewById(R.id.notes_description_landscape);
            text_note.setText(tree_notes.get(index).description);
        }
    }
}
