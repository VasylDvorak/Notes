package com.example.notes;


import static com.example.notes.CardSourceImpl.pictures_global;
import static com.example.notes.Note.random;
import static com.example.notes.NotesFragment.index;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.ContextMenu;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ListFragmentV2 extends Fragment {
    static final String SELECTED_NOTE = "none";
    private static final int DURATION = 2000;
    private Note note;
    private CardsSource data;
    private ListAdapterV2 adapter;
    private RecyclerView recyclerView;
    private AlertDialog alert_dialog;
    private int position;
    private CardData cardData;

    public ListFragmentV2() {
        // Required empty public constructor
    }

    public static ListFragmentV2 newInstance() {
        return new ListFragmentV2();
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.cards_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int pic = pictures_global[random.nextInt(6) + 1];
        switch (item.getItemId()) {
            case R.id.action_add:
                // добавление нового элемента
                data.addCardData(new CardData("Заметка " + data.size(),
                        String.format("Описание заметки %d", data.size()),
                        pic, data.size(), false));

                SimpleDateFormat sdf = new SimpleDateFormat("'Дата\n'dd-MM-yyyy '\n\nи\n\nВремя\n'HH:mm:ss z");
// on below line we are creating a variable
// for current date and time and calling a simple date format in it.
                String currentDateAndTime = sdf.format(new Date());
                Note notea = new Note("Заметка " + (data.size() - 1), String.format("Описание заметки %d", data.size() - 1),
                        currentDateAndTime, new int[]{1, 1, 2023}, new int[]{8, 0}, pic);
                Note.getNotes().add(notea);
// нотификация добавления нового элемемента
                adapter.notifyItemInserted(data.size() - 1);
                // перлистываем список
                recyclerView.scrollToPosition(data.size() - 1);
                recyclerView.smoothScrollToPosition(data.size() - 1);
                return true;
            case R.id.action_clear:
                //чистка списка
                data.clearCardData();
                Note.clearAll();
                // нотификация, измернение элементов
                adapter.notifyDataSetChanged();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_list_v2, container, false);
        initView(view);
        setHasOptionsMenu(true);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (savedInstanceState != null) {
            note = savedInstanceState.getParcelable(SELECTED_NOTE);
        }

    }

    private void initView(View view) {
        recyclerView = view.findViewById(R.id.recycler_view_lines);
        data = new CardSourceImpl(getResources()).init();
        initRecycleView();
    }

    private void initRecycleView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        // ListAdapterV2 listAdapter = new ListAdapterV2(data);
        adapter = new ListAdapterV2(data, this);
        recyclerView.setAdapter(adapter);
        DividerItemDecoration itemDecoration = new
                DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL);
        itemDecoration.setDrawable(getResources().getDrawable(R.drawable.seporatpor, null));
        recyclerView.addItemDecoration(itemDecoration);
        DefaultItemAnimator defaultItemAnimator = new DefaultItemAnimator();
        defaultItemAnimator.setAddDuration(DURATION);
        defaultItemAnimator.setRemoveDuration(DURATION);
        recyclerView.setItemAnimator(defaultItemAnimator);

        adapter.setItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                index = position;
                showNoteDetails(Note.getNotes().get(position));
            }
        });
    }

    @Override
    public void onCreateContextMenu(@NonNull ContextMenu menu, @NonNull View v, @Nullable ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater menuInflater = requireActivity().getMenuInflater();
        menuInflater.inflate(R.menu.card_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        position = adapter.getMenuPosition();

        switch (item.getItemId()) {
            case R.id.action_update:
                cardData = data.getCardData(position);
                TextView update_title = getActivity().findViewById(cardData.getId());
                showAlertDialogWithCustomView(update_title);
                return true;
            case R.id.action_delete:
                data.deleteCardData(position);
                Note.getNotes().remove(position);
                adapter.notifyItemRemoved(position);
                return true;
        }
        return super.onContextItemSelected(item);
    }

    private void showNoteDetails(Note note) {
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
        fragmentTransaction.add(R.id.notes_container, noteFragment, "NOTE_FRAGMENT");
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

    private boolean isLandscape() {
        return getResources().getConfiguration().orientation
                == Configuration.ORIENTATION_LANDSCAPE;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putParcelable(SELECTED_NOTE, note);
        super.onSaveInstanceState(outState);
    }

    private void showAlertDialogWithCustomView(TextView textView) {
        final View customView = getLayoutInflater().inflate(R.layout.alert_dialog_correct_title, null);

        TextView title = new TextView(getContext());
// You Can Customise your Title here
        title.setText(getString(R.string.correct_title));
        title.setBackgroundColor(Color.GRAY);
        title.setPadding(10, 10, 10, 10);
        title.setGravity(Gravity.CENTER);
        title.setTextColor(Color.YELLOW);
        title.setTextSize(20);
        alert_dialog = new AlertDialog.Builder(getContext())
                .setCustomTitle(title)
                .setView(customView)
                .setPositiveButton(getString(R.string.btnBack), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        alert_dialog.dismiss();
                    }
                })
                .show();
        EditText editText = customView.findViewById(R.id.edit_title);
        editText.setText(textView.getText());
        editText.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                textView.setText(s.toString());
                TitleChanging(s.toString());
            }
        });
    }

    private void TitleChanging(String str) {
        data.updateCardData(position, new CardData(str,
                cardData.getDescription(),
                cardData.getPicture(), cardData.getId(), cardData.isLike()));
        ArrayList<Note> notesd = Note.getNotes();
        Note noted = notesd.get(position);
        noted.title = str;
        Note.getNotes().set(position, noted);
    }
}