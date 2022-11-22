package com.example.notes;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.TreeMap;

public class MainActivity extends AppCompatActivity {

    protected static final String CURRENT_NOTE = "current_note";
    // Текущая позиция (выбранная заметка)
    protected static final String ARG_INDEX = "index";
    private static final String FRAGMENT_TAG = "NotesListFragment";
    protected static TreeMap<String, NotesKeepingClass> tree_notes = new TreeMap<>();
    protected static String currentNote;

    @Override
    @SuppressLint("ResourceType")
    protected void onCreate(Bundle savedInstanceState) {

        // Инициализация TreeMap будет впоследствии, когда данные будут сохранятся в файл.
        if (savedInstanceState == null) {
            String question = "Ответ на вопрос: Почитайте документацию методов requireActivity(), requireContext(), getActivity(), getContext() и объясните разницу между ними.";
            String answer = "Метод getActivity() возвращает FragmentActivity, с которым он в данный момент связан. Может возвращать значение null, если вместо этого фрагмент связан с контекстом.\n" +
                    "Метод requireActivity() возвращает FragmentActivity, с которым он в данный момент связан.\n" +
                    "Отличия двух методов getActivity() и requireActivity():\n" +
                    "requireActivity() выдает исключение IllegalStateException и сообщение об исключении, если Activity равно null.  Однако getActivity() возвращает значение null, когда этот фрагмент не присоединен к Activity. \n" +
                    "getActivity() выдаст null, если Activity равно null. getActivity() не выдаёт в этом случае исключение.\n" +
                    "\n" +
                    "Метод getContext() возвращает Context, с которым он в данный момент связан. \n" +
                    "Метод requireContext() возвращает Context, с которым он в данный момент связан.\n" +
                    "Отличия двух методов getContext() и requireContext():\n" +
                    "requireContext() выдает исключение IllegalStateException и сообщение об исключении, если Context равно null.\n" +
                    "getContext() выдаст null, если Context равно null. getContext() не выдаёт в этом случае исключение.\n";

            tree_notes.put("Breakfast", new NotesKeepingClass("Breakfast", "Time for Breakfast", new int[]{12, 11, 2022}, new int[]{9, 30}));
            tree_notes.put("Lunch", new NotesKeepingClass("Lunch", "Time for lunch", new int[]{12, 11, 2022}, new int[]{12, 9}));
            tree_notes.put("Dinner", new NotesKeepingClass("Dinner", "Time for Dinner", new int[]{12, 11, 2022}, new int[]{18, 30}));
            tree_notes.put(question, new NotesKeepingClass(question, answer, new int[]{16, 11, 2022}, new int[]{12, 0}));
            tree_notes.put("Note_1", new NotesKeepingClass("Note_1", "Some information", new int[]{26, 14, 2023}, new int[]{15, 45}));
            tree_notes.put("Note_2", new NotesKeepingClass("Note_2", "Some information", new int[]{26, 14, 2023}, new int[]{15, 45}));
            tree_notes.put("Note_3", new NotesKeepingClass("Note_3", "Some information", new int[]{26, 14, 2023}, new int[]{15, 45}));
            tree_notes.put("Note_4", new NotesKeepingClass("Note_4", "Some information", new int[]{26, 14, 2023}, new int[]{15, 45}));
            tree_notes.put("Note_5", new NotesKeepingClass("Note_5", "Some information", new int[]{26, 14, 2023}, new int[]{15, 45}));
            tree_notes.put("Note_6", new NotesKeepingClass("Note_6", "Some information", new int[]{26, 14, 2023}, new int[]{15, 45}));
            tree_notes.put("Note_7", new NotesKeepingClass("Note_7", "Some information", new int[]{26, 14, 2023}, new int[]{15, 45}));
            tree_notes.put("Note_8", new NotesKeepingClass("Note_8", "Some information", new int[]{26, 14, 2023}, new int[]{15, 45}));
            tree_notes.put("Note_9", new NotesKeepingClass("Note_9", "Some information", new int[]{26, 14, 2023}, new int[]{15, 45}));
            tree_notes.put("Note_10", new NotesKeepingClass("Note_10", "Some information", new int[]{26, 14, 2023}, new int[]{15, 45}));
            tree_notes.put("Note_11", new NotesKeepingClass("Note_11", "Some information", new int[]{26, 14, 2023}, new int[]{15, 45}));
            tree_notes.put("Note_12", new NotesKeepingClass("Note_12", "Some information", new int[]{26, 14, 2023}, new int[]{15, 45}));
            tree_notes.put("Note_13", new NotesKeepingClass("Note_13", "Some information", new int[]{26, 14, 2023}, new int[]{15, 45}));
            tree_notes.put("Note_14", new NotesKeepingClass("Note_14", "Some information", new int[]{26, 14, 2023}, new int[]{15, 45}));
            tree_notes.put("Note_15", new NotesKeepingClass("Note_15", "Some information", new int[]{26, 14, 2023}, new int[]{15, 45}));
            tree_notes.put("Note_16", new NotesKeepingClass("Note_16", "Some information", new int[]{26, 14, 2023}, new int[]{15, 45}));
            tree_notes.put("Note_17", new NotesKeepingClass("Note_17", "Some information", new int[]{26, 14, 2023}, new int[]{15, 45}));
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //создаём и отображаем текстовое уведомление
        String mess = "Ответ на второй вопрос ДЗ вконце списка. Пролистайте пожалуйста.";
        Toast toast = new Toast(getApplicationContext());
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.setDuration(Toast.LENGTH_LONG);
        TextView text_toast = new TextView(MainActivity.this);
        text_toast.setTextColor(Color.YELLOW);
        text_toast.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.text_size));
        text_toast.setBackgroundColor(R.color.set_text_toast);
        text_toast.setText(mess);
        toast.setView(text_toast);
        toast.show();


        if (savedInstanceState == null) getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.fragment_container, new NotesListFragment()).commit();
        NotesListFragment notesListFragment = (NotesListFragment) getSupportFragmentManager().findFragmentByTag(FRAGMENT_TAG);
        if (notesListFragment == null) {
            notesListFragment = new NotesListFragment();
        }

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, notesListFragment, FRAGMENT_TAG).commit();

    }
}