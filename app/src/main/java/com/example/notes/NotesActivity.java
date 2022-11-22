package com.example.notes;

import static com.example.notes.MainActivity.ARG_INDEX;

import android.content.res.Configuration;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class NotesActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes);

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            // Если устройство перевернули в альбомную ориентацию, то надо эту activity закрыть
            finish();
            return;
        }

        // Если эта activity запускается первый раз (с каждым новым описанием первый раз),
        // то перенаправим параметр фрагменту и запустим фрагмент
        if (savedInstanceState == null) {

            String index = getIntent().getExtras().getString(ARG_INDEX);

            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.notes_name_fragment_container, NotesFragment.newInstance(index))
                    .commit();
        }
    }
}