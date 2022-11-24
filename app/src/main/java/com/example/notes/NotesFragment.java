package com.example.notes;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.floatingactionbutton.FloatingActionButton;


public class NotesFragment extends Fragment {


    static final String SELECTED_NOTE = "none";
    protected static int previous_position;
    private Note note;

    public NotesFragment() {
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putParcelable(SELECTED_NOTE, note);


        super.onSaveInstanceState(outState);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_notes, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (savedInstanceState != null) {
            note = savedInstanceState.getParcelable(SELECTED_NOTE);
        }

        initNotes(view.findViewById(R.id.data_container));

        if (isLandscape()) {
            showLandNoteDetails(note);
        }
        FloatingActionButton btnOne = view.findViewById(R.id.about);
        btnOne.setOnClickListener(v -> {
            createOneButtonAlertDialog();
        });
    }


    private boolean isLandscape() {
        return getResources().getConfiguration().orientation
                == Configuration.ORIENTATION_LANDSCAPE;
    }

    private void initNotes(View view) {
        LinearLayout layoutView = (LinearLayout) view;

        for (int i = 0; i < Note.getNotes().length; i++) {

            TextView tv = new TextView(getContext());
            tv.setText(Note.getNotes()[i].getTitle());

            tv.setTextSize(24);
            tv.setMovementMethod(new ScrollingMovementMethod());
            tv.setHorizontalScrollBarEnabled(true);
            tv.setHorizontallyScrolling(true);

            if (previous_position == (i + 1))
                tv.setBackgroundResource(R.color.set_text_block);
            else
                tv.setBackgroundResource(R.color.white);
            tv.setId(i + 1);
            layoutView.addView(tv);

            final int index = i;
            tv.setOnClickListener(v -> {
                if (previous_position != 0) {
                    TextView text_prev = layoutView.findViewById(previous_position);
                    text_prev.setBackgroundResource(R.color.white);
                    text_prev.invalidate();
                }
                previous_position = v.getId();
                v.setBackgroundResource(R.color.set_text_block);
                v.invalidate();
                showNoteDetails(Note.getNotes()[index]);
            });

            if ((previous_position == 0) && (isLandscape())) {

                previous_position = 1;
                TextView text_prev = layoutView.findViewById(previous_position);
                text_prev.setBackgroundResource(R.color.set_text_block);
                text_prev.invalidate();
                showNoteDetails(Note.getNotes()[0]);
            }

        }
    }

    private void showNoteDetails(Note note) {
        this.note = note;
        if (isLandscape()) {
            showLandNoteDetails(note);
        } else {
            showPortNoteDetails(note);
        }
    }

    private void showPortNoteDetails(Note note) {
        NoteFragment noteFragment = NoteFragment.newInstance(note);
        FragmentManager fragmentManager =
                requireActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction =
                fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.notes_container, noteFragment);
        fragmentTransaction.addToBackStack("");
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        fragmentTransaction.commit();
    }

    private void showLandNoteDetails(Note note) {
        NoteFragment noteFragment = NoteFragment.newInstance(note);
        FragmentManager fragmentManager =
                requireActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.note_container, noteFragment); // замена  фрагмента
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        fragmentTransaction.commit();
    }

    private void createOneButtonAlertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.Title)
                .setMessage(R.string.version)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
        Dialog d = builder.show();
        int textViewId = d.getContext().getResources().getIdentifier("android:id/alertTitle", null, null);
        TextView tv = d.findViewById(textViewId);
        tv.setTextColor(getResources().getColor(R.color.set_text_toast));
        tv.setBackgroundColor(getResources().getColor(R.color.teal_200));
        textViewId = d.getContext().getResources().getIdentifier("android:id/message", null, null);
        tv = d.findViewById(textViewId);
        tv.setTextColor(getResources().getColor(android.R.color.holo_red_dark));
        textViewId = d.getContext().getResources().getIdentifier("android:id/button1", null, null);
        Button b = d.findViewById(textViewId);
        b.setTextColor(getResources().getColor(android.R.color.holo_green_dark));
        b.setBackgroundColor(getResources().getColor(R.color.color_time_date));
    }
}