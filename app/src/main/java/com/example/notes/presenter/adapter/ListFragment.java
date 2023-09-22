package com.example.notes.presenter.adapter;


import static com.example.notes.model.CardSourceImpl.pictures_global;
import static com.example.notes.model.Note.random;
import static com.example.notes.view.NotesFragment.index;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.notes.presenter.OnItemClickListener;
import com.example.notes.view.NoteFragment;
import com.example.notes.R;
import com.example.notes.model.CardData;
import com.example.notes.model.CardSourceImpl;
import com.example.notes.model.CardsSource;
import com.example.notes.model.Note;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ListFragment extends Fragment {
    public static final String KEY = "KEY";
    static final String SELECTED_NOTE = "none";
    private static final int DURATION = 2000;
    public static SharedPreferences sharedPreferences;
    private Note note;
    private CardsSource data;
    private ListAdapter adapter;
    private RecyclerView recyclerView;
    private AlertDialog alert_dialog;
    private int position;
    private CardData cardData;
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private Map<String, Object> firebase_data = new HashMap<>();

    public ListFragment() {
        // Required empty public constructor
    }

    public static ListFragment newInstance() {
        return new ListFragment();
    }

    public CardsSource getData() {
        return data;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.cards_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        sharedPreferences = getActivity().getPreferences(Context.MODE_PRIVATE);
        int pic = pictures_global[random.nextInt(6) + 1];
        switch (item.getItemId()) {
            case R.id.action_add:


                SimpleDateFormat sdf = new SimpleDateFormat
                        ("'Дата\n'dd-MM-yyyy '\n\nи\n\nВремя\n'HH:mm:ss z");
// on below line we are creating a variable
// for current date and time and calling a simple date format in it.
                String currentDateAndTime = sdf.format(new Date());
                // добавление нового элемента
                data.addCardData(new CardData("Заметка " + data.size(),
                        String.format("Описание заметки %d", data.size()), currentDateAndTime,
                        pic, data.size(), false));

                Note notea = new Note("Заметка " + (data.size() - 1),
                        String.format("Описание заметки %d", data.size() - 1),
                        currentDateAndTime, new int[]{1, 1, 2023}, new int[]{8, 0}, pic, false);
                Note.getNotes().add(notea);
// нотификация добавления нового элемемента
                adapter_notify();
                return true;
            case R.id.action_clear:
                //чистка списка
                data.clearCardData();
                Note.clearAll();
                // нотификация, измернение элементов
                adapter.notifyDataSetChanged();
                String jsonCardDataAfterClear = new GsonBuilder().create().toJson(data.getCardData());
                sharedPreferences.edit().putString(KEY, jsonCardDataAfterClear).apply();
                return true;
            case R.id.send_to_firebase:
                firebase_data.clear();
                firebase_data.put("NOTESF", data.getCardData());
                db.collection("Notes")
                        .add(firebase_data)
                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                Toast toast = Toast.makeText(getActivity(),
                                        getResources().getString(R.string.success_send_to_firebase)
                                        , Toast.LENGTH_LONG);
                                toast.show();

                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast toast = Toast.makeText(getActivity(),
                                        getResources().getString(R.string.error_send_to_firebase)
                                        , Toast.LENGTH_LONG);
                                toast.show();
                            }
                        });

                return true;
            case R.id.obtain_from_firebase:
                firebase_data.clear();
                db.collection("users")
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    data.clearCardData();
                                    task.getResult().getDocuments();
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        firebase_data = document.getData();
                                        data.setNewData((ArrayList<CardData>) firebase_data
                                                .get("NOTESF"));
                                    }
                                        if (data.getCardData().size() !=0){
                                            adapter.setNewData(data.getCardData());
                                            // перлистываем список
                                            recyclerView.scrollToPosition(data.size() - 1);
                                          //  recyclerView.smoothScrollToPosition(data.size() - 1);
                                            String jsonCardDataAfterAdd = new GsonBuilder().create().toJson(data.getCardData());
                                            sharedPreferences.edit().putString(KEY, jsonCardDataAfterAdd).apply();
                                            Toast toast = Toast.makeText(getActivity(),
                                                    getResources()
                                                            .getString(R.string.obtain_from_Firebase)
                                                    , Toast.LENGTH_LONG);
                                            toast.show();
                                        }

                                } else {
                                    Toast toast = Toast.makeText(getActivity(),
                                            getResources()
                                                    .getString(R.string.error_obtain_from_Firebase),
                                            Toast.LENGTH_LONG);
                                    toast.show();
                                }
                            }
                        });

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
        data = new CardSourceImpl(getResources()); //new CardSourceImpl(getResources()).init();
        initRecycleView();
    }

    private void initRecycleView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        // ListAdapterV2 listAdapter = new ListAdapterV2(data);
        adapter = new ListAdapter(data, this);
        recyclerView.setAdapter(adapter);
        DividerItemDecoration itemDecoration = new
                DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL);
        itemDecoration.setDrawable(getResources().getDrawable(R.drawable.seporatpor, null));
        recyclerView.addItemDecoration(itemDecoration);
        DefaultItemAnimator defaultItemAnimator = new DefaultItemAnimator();
        defaultItemAnimator.setAddDuration(DURATION);
        defaultItemAnimator.setRemoveDuration(DURATION);
        recyclerView.setItemAnimator(defaultItemAnimator);
        sharedPreferences = getActivity().getPreferences(Context.MODE_PRIVATE);
        String savedData = sharedPreferences.getString(KEY, null);
        if (savedData == null) {
            Toast.makeText(getContext(), R.string.empty, Toast.LENGTH_LONG).show();
        } else {
            try {
                Type type = new TypeToken<List<CardData>>() {
                }.getType();
                ArrayList<CardData> data_source = new GsonBuilder().create()
                        .fromJson(savedData, type);
                adapter.setNewData(data_source);
                Note.clearAll();
                for (CardData from_database :
                        data_source) {

                    Note noteaa = new Note(from_database.getTitle(),
                            from_database.getDescription(),
                            from_database.getCurrentDateAndTime(),
                            new int[]{1, 1, 2023}, new int[]{8, 0}, from_database.getPicture(),
                            from_database.isLike());
                    Note.getNotes().add(noteaa);
                }

            } catch (Exception e) {
                Toast.makeText(getContext(), R.string.error, Toast.LENGTH_LONG).show();
            }
        }
        adapter.setItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                index = position;
                showNoteDetails(Note.getNotes().get(position));
            }
        });
    }

    @Override
    public void onCreateContextMenu(@NonNull ContextMenu menu, @NonNull View v,
                                    @Nullable ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater menuInflater = requireActivity().getMenuInflater();
        menuInflater.inflate(R.menu.card_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        //  getActivity().getSharedPreferences()
        sharedPreferences = getActivity().getPreferences(Context.MODE_PRIVATE);
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

                String jsonCardDataAfterDelete = new GsonBuilder()
                        .create().toJson(data.getCardData());
                sharedPreferences.edit().putString(KEY, jsonCardDataAfterDelete).apply();

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
        final View customView = getLayoutInflater()
                .inflate(R.layout.alert_dialog_correct_title, null);

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
                String strr = s.toString();
                textView.setText(strr);
                TitleChanging(strr);
            }
        });
    }

    private void TitleChanging(String str) {
        data.updateCardData(position, new CardData(str,
                cardData.getDescription(), cardData.getCurrentDateAndTime(),
                cardData.getPicture(), cardData.getId(), cardData.isLike()));
        adapter.notifyItemChanged(position);
        String jsonCardDataAfterUpdate = new GsonBuilder().create().toJson(data.getCardData());
        sharedPreferences.edit().putString(KEY, jsonCardDataAfterUpdate).apply();
        ArrayList<Note> notesd = Note.getNotes();
        Note noted = notesd.get(position);
        noted.title = str;
        Note.getNotes().set(position, noted);
    }
private void adapter_notify(){
    adapter.notifyItemInserted(data.size() - 1);
    // перлистываем список
    recyclerView.scrollToPosition(data.size() - 1);
    recyclerView.smoothScrollToPosition(data.size() - 1);
    String jsonCardDataAfterAdd = new GsonBuilder().create().toJson(data.getCardData());
    sharedPreferences.edit().putString(KEY, jsonCardDataAfterAdd).apply();
}

}