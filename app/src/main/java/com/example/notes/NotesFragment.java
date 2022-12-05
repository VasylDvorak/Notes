package com.example.notes;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class NotesFragment extends Fragment {
    static final String SELECTED_NOTE = "none";
    public static int index;
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
        View view = inflater.inflate(R.layout.fragment_notes, container, false);


        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (savedInstanceState != null) {
            note = savedInstanceState.getParcelable(SELECTED_NOTE);
        }

        initNotes(view.findViewById(R.id.data_container));

        if (isLandscape() && (Note.getNotes().size() != 0))
            showLandNoteDetails(Note.getNotes().get(index));

        FloatingActionButton btnOne = view.findViewById(R.id.about);
        btnOne.setOnClickListener(v -> {
            createOneButtonAlertDialog("О приложении");
        });
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

    private boolean isLandscape() {
        return getResources().getConfiguration().orientation
                == Configuration.ORIENTATION_LANDSCAPE;
    }

    private void initNotes(View view) {

        addFragment(ListFragmentV2.newInstance());

    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        MenuInflater inflater = getActivity().getMenuInflater();
        inflater.inflate(R.menu.context_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add:
                createOneButtonAlertDialog("Реализовать добавить");
                break;
            case R.id.sort:
                createOneButtonAlertDialog("Реализовать сортировать");
                break;
            default:
                return super.onContextItemSelected(item);
        }
        return true;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, @NonNull MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.menu, menu);
    }

    private void createOneButtonAlertDialog(String title_window) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(title_window)
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

    private void addFragment(Fragment fragment) {
        requireActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.data_container, fragment)
                .addToBackStack(null)
                .commit();

    }
}