package com.example.notes;

import static com.example.notes.MainActivity.ARG_INDEX;
import static com.example.notes.MainActivity.CURRENT_NOTE;
import static com.example.notes.MainActivity.currentNote;
import static com.example.notes.MainActivity.tree_notes;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import java.util.Set;

public class NotesListFragment extends Fragment {
    protected static int previous_position, i;

    // При создании фрагмента укажем его макет
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_notes_portrait, container, false);
    }

    // Этот метод вызывается, когда макет экрана создан и готов к отображению информации. Создаем список заметок.
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Восстановление текущей позиции
        if (savedInstanceState != null) {
            currentNote = savedInstanceState.getString(CURRENT_NOTE, tree_notes.firstKey());
        }
        // инициализация списка
        initList(view);
        // отображения открытой ранее заметки
        if (isLandscape()) {
            showLandNote(currentNote);
        }
    }

    // создаём список заметок на экране из массива HashTree

    private void initList(View view) {
        LinearLayout layoutView = (LinearLayout) view;
        // В этом цикле создаём элемент TextView,
        // заполняем его значениями,
        // и добавляем на экран.
        Set<String> notes_inter = tree_notes.keySet();
        i = 1;
        for (String notes_c : notes_inter) {
            TextView tv = new TextView(getContext());
            tv.setText(notes_c);
            tv.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.text_size));
            tv.setHeight((int) ((TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 30, getResources().getDisplayMetrics())) * 1.3));
            tv.setMovementMethod(new ScrollingMovementMethod());
            tv.setHorizontalScrollBarEnabled(true);
            tv.setHorizontallyScrolling(true);
            if (previous_position == i)
                tv.setBackgroundResource(R.color.set_text_block);
            else
                tv.setBackgroundResource(R.color.white);

            tv.setId(i);
            layoutView.addView(tv);
            final String position = notes_c;
            tv.setOnClickListener(v -> {
                if (previous_position != 0) {
                    TextView text_prev = layoutView.findViewById(previous_position);
                    text_prev.setBackgroundResource(R.color.white);
                    text_prev.invalidate();
                }
                v.setBackgroundResource(R.color.set_text_block);
                v.invalidate();
                previous_position = v.getId();
                currentNote = position;
                showNote(position);
            });
            i++;
        }
    }

    private void showNote(String index) {
        if (isLandscape()) {
            showLandNote(index);
        } else {
            showPortNote(index);
        }
    }

    // Показываем заметку в портретной ориентации
    private void showPortNote(String index) {
        Activity activity = requireActivity();
        final Intent intent = new Intent(activity, NotesActivity.class);
        intent.putExtra(ARG_INDEX, index);
        activity.startActivity(intent);
    }

    // Показываем заметку в ландшафтной ориентации
    private void showLandNote(String index) {
        // Создаём новый фрагмент с текущей позицией для вывода заметки
        NotesFragment detail = NotesFragment.newInstance(index);
        // Выполняем транзакцию по замене фрагмента
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.notes_container, detail);  // замена фрагмента
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        fragmentTransaction.commit();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putString(CURRENT_NOTE, currentNote);
        super.onSaveInstanceState(outState);
    }

    private boolean isLandscape() {
        return getResources().getConfiguration().orientation
                == Configuration.ORIENTATION_LANDSCAPE;
    }
}