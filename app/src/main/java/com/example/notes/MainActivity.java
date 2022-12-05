package com.example.notes;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    public static final String CHANNEL_ID = "CHANNEL_ID", CHOSEN_THEME = "chosen_theme";
    private static final int first_stile = 0;
    private static final int second_stile = 1;
    private static final int third_style = 2;
    protected static int note_text_color, notes_text_color, notes_text_color_first;
    private TextView headerTitleName, headerTitleProfession;
    private String UserName, UserProfession;
    private Snackbar snackbar;
    private AlertDialog alert_dialog, alert;
    private int chosen, previous_chosen;
    private LayoutInflater inflator;
    private View layout;
    private int first;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initToolBar();

        if (!isLandscape())
            initToolbarAndDrawer();
        else
            initToolBar();

        if (savedInstanceState != null) {
            previous_chosen = savedInstanceState.getInt(CHOSEN_THEME);
            setAppTheme(previous_chosen);
        } else {
            first = 1;
            setAppTheme(first_stile);
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.notes_container, new NotesFragment(), "Notes_Fragment")
                    .commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.clear();
        if (isLandscape()) {
            menu.add(Menu.NONE, R.id.show_current_data_time, Menu.NONE, R.string.Current_time_date)
                    .setIcon(android.R.drawable.ic_menu_recent_history).setShowAsActionFlags(1);
            menu.add(Menu.NONE, 6, Menu.NONE, R.string.Send_note);
            menu.add(Menu.NONE, 7, Menu.NONE, R.string.Add_photo);
        }
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.action_search:
                // Сделать поиск
                createOneButtonAlertDialog("Сделать поиск");
                return true;
            case R.id.action_sort:
                createOneButtonAlertDialog("Сделать сортировку");
                return true;

            case 6:
                createOneButtonAlertDialog("Сделать переслать заметку");
                return true;
            case 7:
                createOneButtonAlertDialog("Сделать добавить фото");
                return true;
            case R.id.show_current_data_time:
                showNotification();
                return true;
            case R.id.exit_from_notes:
                showAlertDialogWithCustomView();
                return true;
            case R.id.change_theme:

                ThemeChooser();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private Toolbar initToolBar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        return toolbar;
    }

    private void initToolbarAndDrawer() {
        Toolbar toolbar = initToolBar();
        initDrawer(toolbar);
    }

    private void initDrawer(Toolbar toolbar) {
        DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        NavigationView navigationView = findViewById(R.id.navigation_view);
        View headerLayout = navigationView.inflateHeaderView(R.layout.drawer_header);
        headerTitleName = headerLayout.findViewById(R.id.header_title_id);
        headerTitleName.setText(R.string.header_title);
        UserName = getResources().getString(R.string.header_title);
        ShapeableImageView avatar = headerLayout.findViewById(R.id.header_avatar);
        avatar.setImageResource(R.drawable.avatar);
        headerTitleProfession = headerLayout.findViewById(R.id.header_title_id2);
        headerTitleProfession.setText(R.string.status);
        UserProfession = getResources().getString(R.string.status);
        DilogOfHeaderCorrect(headerLayout.findViewById(R.id.correct_text_header));
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                switch (id) {
                    case R.id.action_about:
                        createOneButtonAlertDialog(getString(R.string.Notes_program));
                        return true;
                    case R.id.action_exit:

                        drawerLayout.close();
                        return true;
                    case R.id.settings: {
                        createOneButtonAlertDialog("Сделать настройки");
                    }
                    return true;
                }
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

    private void DilogOfHeaderCorrect(View CorrectTextHeader) {

        OnDialogListener dialogListener = new OnDialogListener() {

            @Override
            public void onDialogName() {
                UserName = CorrectText(headerTitleName);
            }

            @Override
            public void onDialogProfession() {

                UserProfession = CorrectText(headerTitleProfession);
            }

            @Override
            public String CorrectText(TextView textView) {

                return prepareCustomSnackbar(textView, findViewById(R.id.action_exit));
            }
        };

        CorrectTextHeader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogHeaderDrawerFragment dialogFragment =
                        DialogHeaderDrawerFragment.newInstance();
                dialogFragment.setOnDialogListener(dialogListener);
                dialogFragment.show(getSupportFragmentManager(),
                        "dialog_drawer_header_fragment");
            }
        });
    }


    private String prepareCustomSnackbar(TextView text_view, View anchor_view) {

        snackbar = Snackbar.make(anchor_view, "", Snackbar.LENGTH_INDEFINITE)
                .setAction(R.string.apply, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        InputMethodManager imm = (InputMethodManager)
                                getSystemService(Activity.INPUT_METHOD_SERVICE);
                        imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
                        snackbar.dismiss();
                    }
                });
        Snackbar.SnackbarLayout layout = (Snackbar.SnackbarLayout) snackbar.getView();
        snackbar.setActionTextColor(Color.RED);
        View sbView = snackbar.getView();
        TextView Text_view = sbView.findViewById(com.google.android.material.R.id.snackbar_text);
        Text_view.setVisibility(View.INVISIBLE);


        View snackView = getLayoutInflater().inflate(R.layout.my_snackbar_layout, null);
// Configure the view
        EditText correct_line = snackView.findViewById(R.id.correct_box);

        correct_line.setText(text_view.getText());
        correct_line.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                text_view.setText(charSequence.toString());

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


//If the view is not covering the whole snackbar layout, add this line
        layout.setPadding(0, 0, 0, 0);
// Add the view to the Snackbar's layout
        layout.addView(snackView, 0);
// Show the Snackbar
        snackbar.setAnchorView(anchor_view)
                .setBackgroundTint(Color.CYAN)
                .setAnimationMode(BaseTransientBottomBar.ANIMATION_MODE_SLIDE)
                .show();

        return String.valueOf(text_view.getText());
    }

    private void showAlertDialogWithCustomView() {
        final View customView = getLayoutInflater()
                .inflate(R.layout.alert_dialog_custom_view, null);

        TextView title = new TextView(this);
// You Can Customise your Title here
        title.setText(getString(R.string.exit_alert_dialog));
        title.setBackgroundColor(Color.RED);
        title.setPadding(10, 10, 10, 10);
        title.setGravity(Gravity.CENTER);
        title.setTextColor(Color.YELLOW);
        title.setTextSize(20);

        alert_dialog = new AlertDialog.Builder(this)
                .setCustomTitle(title)
                .setView(customView)
                .setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(MainActivity.this,
                                getString(R.string.Notes_were_closed), Toast.LENGTH_LONG).show();
                        finishAndRemoveTask();
                    }
                })
                .setNegativeButton(getString(R.string.no), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        alert_dialog.dismiss();
                    }
                })
                .show();
    }

    private void showNotification() {
// Создаем NotificationChannel, но это делается только для API 26+
// Потому что NotificationChannel -- это новый класс и его нет в suppor library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel();
        }

        // on below line we are creating and initializing
// variable for simple date format.
        SimpleDateFormat sdf =
                new SimpleDateFormat("'Дата\n'dd-MM-yyyy '\n\nи\n\nВремя\n'HH:mm:ss z");
// on below line we are creating a variable
// for current date and time and calling a simple date format in it.
        String currentDateAndTime = sdf.format(new Date());

        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(this, CHANNEL_ID);
// Все цветные иконки отображаются только в оттенках серого
        builder.setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle(getString(R.string.Current_date_time))
                .setContentText(currentDateAndTime)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);
        NotificationManagerCompat.from(this).notify(42, builder.build());
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void createNotificationChannel() {
        String name = "Name";
        String descriptionText = "Description";
        int importance = NotificationManager.IMPORTANCE_DEFAULT;
        NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name,
                importance);
        channel.setDescription(descriptionText);
// Регистрируем канал в системе
        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.createNotificationChannel(channel);
    }


    private void ThemeChooser() {

        final String[] items = getResources().getStringArray(R.array.choose);
// Создаём билдер и передаём контекст приложения
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
// В билдере указываем заголовок окна . Можно указывать как ресурс ,
// так и строку
        inflator = getLayoutInflater();
        layout = inflator.inflate(R.layout.theme_chooser,
                findViewById(R.id.root_title_theme_chooser));
        builder.setCustomTitle(layout)
// Добавляем список элементов ; chosen -- выбранный элемент ,
                .setSingleChoiceItems(items, previous_chosen,
                        new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int item) {
                        chosen = item; // Обновляем выбранный элемент
                        setAppTheme(chosen);
                    }
                })
                .setNegativeButton(getString(R.string.cancel),
                        new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        chosen = previous_chosen;
                        setAppTheme(chosen);
                        alert.dismiss();
                    }
                })
                .setPositiveButton(getString(R.string.apply),
                        new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        previous_chosen = chosen;
                        alert.dismiss();
                    }
                });
        alert = builder.create();

        alert.show();
    }

    private void ChangeStyleTheme(int background_color_tool_bar,
                                  int text_title_color_tool_bar,
                                  int text_subtitle_color_tool_bar,
                                  int text_notes_list_color) {

        Toolbar changed_toolbar = MainActivity.this.findViewById(R.id.toolbar);
        changed_toolbar.setBackgroundColor(getResources().getColor(background_color_tool_bar));
        changed_toolbar.setTitleTextColor(getResources().getColor(text_title_color_tool_bar));
        changed_toolbar.setSubtitleTextColor(getResources().getColor(text_subtitle_color_tool_bar));
        inflator = getLayoutInflater();
        layout = inflator.inflate(R.layout.fragment_notes,
                findViewById(R.id.notes_container));
        TextView changed_color_text;
        notes_text_color = getResources().getColor(text_notes_list_color);
        inflator = getLayoutInflater();
        layout = inflator.inflate(R.layout.fragment_note,
                findViewById(R.id.linear_layout_note));
        note_text_color = text_notes_list_color;
        changed_color_text = layout.findViewById(R.id.tvTitle);
        changed_color_text.setTextColor(getResources().getColor(text_notes_list_color));
        changed_color_text = layout.findViewById(R.id.tvDescription);
        changed_color_text.setTextColor(getResources().getColor(text_notes_list_color));

        if (first != 0) {
            MainActivity.this.getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.data_container, ListFragmentV2.newInstance())
                    .addToBackStack(null)
                    .commit();
        }
    }

    private void setAppTheme(int codeStyle) {
        notes_text_color_first = getResources().getColor(R.color.teal_200);
        switch (codeStyle) {
            case first_stile:
                ChangeStyleTheme(R.color.set_text_toast,
                        R.color.color_time_date,
                        R.color.teal_200,
                        R.color.teal_200);
                return;
            case second_stile:
                ChangeStyleTheme(R.color.purple_500,
                        R.color.set_text_toast,
                        android.R.color.holo_red_light,
                        android.R.color.holo_red_light);
                return;
            case third_style:
                ChangeStyleTheme(R.color.color_1,
                        R.color.purple_700,
                        android.R.color.holo_purple,
                        android.R.color.holo_purple);
                return;
            default:
                ChangeStyleTheme(R.color.set_text_toast,
                        R.color.color_time_date,
                        R.color.teal_200,
                        R.color.teal_200);
                return;
        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putInt(CHOSEN_THEME, previous_chosen);
        super.onSaveInstanceState(outState);
    }
}




