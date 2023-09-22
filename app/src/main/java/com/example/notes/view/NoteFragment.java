package com.example.notes.view;


import static com.example.notes.model.CardSourceImpl.dataSource;
import static com.example.notes.presenter.adapter.ListFragment.KEY;
import static com.example.notes.presenter.adapter.ListFragment.sharedPreferences;
import static com.example.notes.view.MainActivity.note_text_color;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.notes.R;
import com.example.notes.model.CardData;
import com.example.notes.model.Note;
import com.example.notes.presenter.ParentDateTimeFragment;
import com.google.gson.GsonBuilder;


public class NoteFragment extends Fragment {
    //  private OnDialogListener ColorListener;
    static final String SELECTED_NOTE = "note";
    public static int PDateTime;
    //  public String mListener;
    boolean title_was_changed;
    private int text_color;
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
        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.fragment_note, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Bundle arguments = getArguments();

        TextView time_date_alarm = view.findViewById(R.id.time_date_alarm_view);
        TextView tvDescription = view.findViewById(R.id.tvDescription);
        //  sharedPreferences = getActivity().getPreferences(Context.MODE_PRIVATE);
        title_was_changed = false;
        if (arguments != null) {
            note = arguments.getParcelable(SELECTED_NOTE);
            if (note == null)
                note = Note.getNote(0);
            TextView tvTitle = view.findViewById(R.id.tvTitle);
            tvTitle.setText(note.getTitle());
            tvTitle.setTextColor(getResources().getColor(note_text_color));
            tvTitle.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    String new_title = charSequence.toString();
                    note.setTitle(new_title);
                    if (note.getId(note) >= 0) {
                        dataSource.set(note.getId(note), new CardData(new_title,
                                note.getDescription(), note.getCreationDate(),
                                note.getPictureID(), note.getId(note),
                                note.getLike()));
                        String jsonCardDataAfterUpdate = new GsonBuilder().create()
                                .toJson(dataSource);
                        sharedPreferences.edit().putString(KEY, jsonCardDataAfterUpdate).apply();
                    }


                    title_was_changed = true;
                    TextView rt = getActivity().findViewById(note.getId(note));
                    if (rt == null) {
                        int k = 1;
                        rt = getActivity().findViewById(k);
                    }
                    if ((PDateTime == 0) && (rt != null) && (note != null))
                        rt.setText(note.getTitle());
                }

                @Override
                public void afterTextChanged(Editable editable) {
                }
            });


            time_date_alarm.setText(note.getTimeDateAlarm());
            tvDescription.setText(note.getDescription());
            tvDescription.setTextColor(getResources().getColor(note_text_color));
            tvDescription.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    String new_description = charSequence.toString();
                    note.setDescription(new_description);
                    if (note.getId(note) >= 0) {
                        dataSource.set(note.getId(note),
                                new CardData(note.getTitle(),
                                        new_description, note.getCreationDate(),
                                        note.getPictureID(), note.getId(note),
                                        note.getLike()));
                        String jsonCardDataAfterUpdate = new GsonBuilder()
                                .create().toJson(dataSource);
                        sharedPreferences.edit().putString(KEY, jsonCardDataAfterUpdate).apply();
                    }

                }

                @Override
                public void afterTextChanged(Editable editable) {
                }
            });
            time_date_alarm.setOnLongClickListener(view1 -> {
                initDateTimePopupMenu(time_date_alarm);

                return true;
            });
            Button buttonBack = view.findViewById(R.id.btnBack);


            if (buttonBack != null)
                buttonBack.setOnClickListener(view1 -> {
                    if (title_was_changed) {
                        TextView rt = getActivity().findViewById(note.getId(note));
                        rt.setText(note.getTitle());

                    }
                    requireActivity().getSupportFragmentManager().popBackStack();
                });
        }
    }

    private void initDateTimePopupMenu(TextView view) {
        Activity activity = requireActivity();
        PopupMenu popupMenu = new PopupMenu(activity, view);
        activity.getMenuInflater().inflate(R.menu.popup_menu, popupMenu.getMenu());
        MenuItem item = popupMenu.getMenu().findItem(R.id.action_popup_delete);
        if (item != null) {
            item.setVisible(false);
        }
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                // Слушатель диалога , сюда попадает управление

                if (menuItem.getItemId() == R.id.action_popup_correct) {
                    ParentDateTimeFragment dialogDateTimeFragment =
                            ParentDateTimeFragment.newInstance(note);
                    dialogDateTimeFragment.show(requireActivity().getSupportFragmentManager(),
                            "dialog_time_date_fragment");
                }
                return true;
            }
        });
        popupMenu.show();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, @NonNull MenuInflater inflater) {

        Fragment CurrentFragment = requireActivity().getSupportFragmentManager()
                .findFragmentByTag("NOTE_FRAGMENT");

        if ((!isLandscape()) && (menu != null) &&
                (CurrentFragment != null && CurrentFragment.isVisible())) {
            menu.clear();
            inflater.inflate(R.menu.note_fragment_menu, menu);
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.send_note:
                // Сделать поиск
                createOneButtonAlertDialog("Сделать отослать заметку");
                return true;
            case R.id.add_photo:
                createOneButtonAlertDialog("Сделать добавить фото");
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private boolean isLandscape() {
        return getResources().getConfiguration().orientation
                == Configuration.ORIENTATION_LANDSCAPE;
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
}