package com.example.notes;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;



import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity {




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
                initToolBar();


        if(!isLandscape())
            initToolbarAndDrawer();
        else
            initToolBar();

        if (savedInstanceState == null) getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.notes_container, new NotesFragment())
                .commit();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.clear();
            getMenuInflater().inflate(R.menu.menu, menu);
            if (isLandscape()){
                menu.add(Menu.NONE, 6, Menu.NONE, R.string.Send_note);
                menu.add(Menu.NONE, 7, Menu.NONE, R.string.Add_photo);

            }
        return super .onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected( @NonNull MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.action_search:
                // Сделать поиск
                createOneButtonAlertDialog("Сделать поиск");
                return true ;
            case R.id.action_sort:
                createOneButtonAlertDialog("Сделать сортировку");
                return true ;
            case 6:
                createOneButtonAlertDialog("Сделать переслать заметку");
                return true ;
            case 7:
                createOneButtonAlertDialog("Сделать длобавить фото");
                return true ;

        }
        return super .onOptionsItemSelected(item);
    }

    private Toolbar initToolBar(){
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        return toolbar;
    }

    private void initToolbarAndDrawer(){
        Toolbar toolbar = initToolBar();
        initDrawer(toolbar);
    }

    private void initDrawer(Toolbar toolbar){
        DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        NavigationView navigationView = findViewById(R.id.navigation_view);





        View headerLayout = navigationView.inflateHeaderView(R.layout.drawer_header);
        TextView headerTitle = (TextView)headerLayout.findViewById(R.id.header_title_id);
        headerTitle.setText(R.string.header_title);
        ShapeableImageView avatar= (ShapeableImageView)headerLayout.findViewById(R.id.header_avatar);
        avatar.setImageResource(R.drawable.avatar);
        headerTitle = (TextView)headerLayout.findViewById(R.id.header_title_id2);
        headerTitle.setText(R.string.status);



        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                switch (id){
                    case R.id.action_about:
                        createOneButtonAlertDialog("Программа блокнот");
                        return true;
                    case R.id.action_exit:

                        drawerLayout.close();
                        return true;
                    case R.id.settings:
                        createOneButtonAlertDialog("Сделать настройки");

                        return true;}
                return false;
            }
        });



    }






    private void createOneButtonAlertDialog(String title_window) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
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
    private boolean isLandscape() {
        return getResources().getConfiguration().orientation
                == Configuration.ORIENTATION_LANDSCAPE;
    }

    }


