package com.example.notes;

import android.content.res.Configuration;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class NoteFragment extends Fragment {

    static final String SELECTED_NOTE = "note";
    static int PDateTime;
    boolean title_was_changed;
    private Note note;


    public NoteFragment() {
        // Required empty public constructor
    }

    public static NoteFragment newInstance(Note note) {
        NoteFragment fragment = new NoteFragment();
        Bundle args = new Bundle();
        args.putParcelable(SELECTED_NOTE, note);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null)
            requireActivity().getSupportFragmentManager().popBackStack();
    }
    //Call onActivity Create method

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_note, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Bundle arguments = getArguments();
        TextView time_date_alarm = view.findViewById(R.id.time_date_alarm_view);
        TextView tvDescription = view.findViewById(R.id.tvDescription);
        title_was_changed = false;
        if (arguments != null) {
            note = arguments.getParcelable(SELECTED_NOTE);
            if (note == null)
                note = Note.getNote(0);
            TextView tvTitle = view.findViewById(R.id.tvTitle);
            tvTitle.setText(note.getTitle());
            tvTitle.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    note.setTitle(charSequence.toString());
                    title_was_changed = true;
                    TextView rt = getActivity().findViewById(note.getId());
                    if (rt == null) {
                        int k = 1;
                        rt = getActivity().findViewById(k);
                    }
                    if (PDateTime == 0)
                        rt.setText(note.getTitle());
                }

                @Override
                public void afterTextChanged(Editable editable) {
                }
            });


            time_date_alarm.setText(note.getTimeDateAlarm());
            tvDescription.setText(note.getDescription());
            tvDescription.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    note.setDescription(charSequence.toString());
                }

                @Override
                public void afterTextChanged(Editable editable) {
                }
            });
            time_date_alarm.setOnClickListener(view1 -> {
                showCorrectDateTime(note);
            });
            Button buttonBack = view.findViewById(R.id.btnBack);
            if (buttonBack != null)
                buttonBack.setOnClickListener(view1 -> {
                    if (title_was_changed) {
                        TextView rt = getActivity().findViewById(note.getId());
                        rt.setText(note.getTitle());
                    }
                    requireActivity().getSupportFragmentManager().popBackStack();
                });
        }
    }

    private void showCorrectDateTime(Note note) {
        this.note = note;
        if (getResources().getConfiguration().orientation
                == Configuration.ORIENTATION_LANDSCAPE) {
            showLandCorrectDateTime(note);
        } else {
            showPortCorrectDateTime(note);
        }
    }

    private void showPortCorrectDateTime(Note note) {
        ParentDateTimeFragment parentDateTimeFragment = ParentDateTimeFragment.newInstance(note);
        FragmentManager fragmentManager =
                requireActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction =
                fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.notes_container, parentDateTimeFragment);
        fragmentTransaction.addToBackStack("");
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        fragmentTransaction.commit();
    }

    private void showLandCorrectDateTime(Note note) {
        ParentDateTimeFragment parentDateTimeFragment = ParentDateTimeFragment.newInstance(note);
        FragmentManager fragmentManager =
                requireActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.note_container, parentDateTimeFragment); // замена  фрагмента
        fragmentTransaction.addToBackStack("");
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        fragmentTransaction.commit();
    }
}