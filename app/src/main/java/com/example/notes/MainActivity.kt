package com.example.notes

import android.app.*
import com.example.notes.DialogHeaderDrawerFragment.Companion.newInstance
import com.example.notes.DialogHeaderDrawerFragment.setOnDialogListener
import com.example.notes.ListFragmentV2.Companion.newInstance
import androidx.appcompat.app.AppCompatActivity
import android.widget.TextView
import com.google.android.material.snackbar.Snackbar
import android.os.Bundle
import com.example.notes.R
import com.example.notes.MainActivity
import com.example.notes.NotesFragment
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.ActionBarDrawerToggle
import com.google.android.material.navigation.NavigationView
import com.google.android.material.imageview.ShapeableImageView
import android.content.DialogInterface
import android.content.res.Configuration
import android.graphics.Color
import com.example.notes.OnDialogListener
import com.example.notes.DialogHeaderDrawerFragment
import com.google.android.material.snackbar.Snackbar.SnackbarLayout
import android.widget.EditText
import android.text.TextWatcher
import android.text.Editable
import com.google.android.material.snackbar.BaseTransientBottomBar
import android.widget.Toast
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.annotation.RequiresApi
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import androidx.appcompat.widget.Toolbar
import com.example.notes.ListFragmentV2
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {
    private var headerTitleName: TextView? = null
    private var headerTitleProfession: TextView? = null
    private var UserName: String? = null
    private var UserProfession: String? = null
    private var snackbar: Snackbar? = null
    private var alert_dialog: AlertDialog? = null
    private var alert: AlertDialog? = null
    private var chosen = 0
    private var previous_chosen = 0
    private var inflator: LayoutInflater? = null
    private var layout: View? = null
    private var first = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initToolBar()
        if (!isLandscape) initToolbarAndDrawer() else initToolBar()
        if (savedInstanceState != null) {
            previous_chosen = savedInstanceState.getInt(CHOSEN_THEME)
            setAppTheme(previous_chosen)
        } else {
            first = 1
            setAppTheme(first_stile)
            supportFragmentManager
                .beginTransaction()
                .add(R.id.notes_container, NotesFragment(), "Notes_Fragment")
                .commit()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menu.clear()
        if (isLandscape) {
            menu.add(Menu.NONE, R.id.show_current_data_time, Menu.NONE, R.string.Current_time_date)
                .setIcon(android.R.drawable.ic_menu_recent_history).setShowAsActionFlags(1)
            menu.add(Menu.NONE, 6, Menu.NONE, R.string.Send_note)
            menu.add(Menu.NONE, 7, Menu.NONE, R.string.Add_photo)
        }
        menuInflater.inflate(R.menu.menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        when (id) {
            R.id.action_search -> {
                // Сделать поиск
                createOneButtonAlertDialog("Сделать поиск")
                return true
            }
            R.id.action_sort -> {
                createOneButtonAlertDialog("Сделать сортировку")
                return true
            }
            6 -> {
                createOneButtonAlertDialog("Сделать переслать заметку")
                return true
            }
            7 -> {
                createOneButtonAlertDialog("Сделать добавить фото")
                return true
            }
            R.id.show_current_data_time -> {
                showNotification()
                return true
            }
            R.id.exit_from_notes -> {
                showAlertDialogWithCustomView()
                return true
            }
            R.id.change_theme -> {
                ThemeChooser()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun initToolBar(): Toolbar {
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        return toolbar
    }

    private fun initToolbarAndDrawer() {
        val toolbar = initToolBar()
        initDrawer(toolbar)
    }

    private fun initDrawer(toolbar: Toolbar) {
        val drawerLayout = findViewById<DrawerLayout>(R.id.drawer_layout)
        val actionBarDrawerToggle = ActionBarDrawerToggle(
            this, drawerLayout, toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        drawerLayout.addDrawerListener(actionBarDrawerToggle)
        actionBarDrawerToggle.syncState()
        val navigationView = findViewById<NavigationView>(R.id.navigation_view)
        val headerLayout = navigationView.inflateHeaderView(R.layout.drawer_header)
        headerTitleName = headerLayout.findViewById(R.id.header_title_id)
        headerTitleName.setText(R.string.header_title)
        UserName = resources.getString(R.string.header_title)
        val avatar = headerLayout.findViewById<ShapeableImageView>(R.id.header_avatar)
        avatar.setImageResource(R.drawable.avatar)
        headerTitleProfession = headerLayout.findViewById(R.id.header_title_id2)
        headerTitleProfession.setText(R.string.status)
        UserProfession = resources.getString(R.string.status)
        DilogOfHeaderCorrect(headerLayout.findViewById(R.id.correct_text_header))
        navigationView.setNavigationItemSelectedListener(object :
            NavigationView.OnNavigationItemSelectedListener {
            override fun onNavigationItemSelected(item: MenuItem): Boolean {
                val id = item.itemId
                when (id) {
                    R.id.action_about -> {
                        createOneButtonAlertDialog(getString(R.string.Notes_program))
                        return true
                    }
                    R.id.action_exit -> {
                        drawerLayout.close()
                        return true
                    }
                    R.id.settings -> {
                        run { createOneButtonAlertDialog("Сделать настройки") }
                        return true
                    }
                }
                return false
            }
        })
    }

    private fun createOneButtonAlertDialog(title_window: String) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(title_window)
            .setMessage(R.string.version)
            .setPositiveButton(R.string.ok) { dialog, which -> }
        val d: Dialog = builder.show()
        var textViewId = d.context.resources.getIdentifier("android:id/alertTitle", null, null)
        var tv = d.findViewById<TextView>(textViewId)
        tv.setTextColor(resources.getColor(R.color.set_text_toast))
        tv.setBackgroundColor(resources.getColor(R.color.teal_200))
        textViewId = d.context.resources.getIdentifier("android:id/message", null, null)
        tv = d.findViewById(textViewId)
        tv.setTextColor(resources.getColor(android.R.color.holo_red_dark))
        textViewId = d.context.resources.getIdentifier("android:id/button1", null, null)
        val b = d.findViewById<Button>(textViewId)
        b.setTextColor(resources.getColor(android.R.color.holo_green_dark))
        b.setBackgroundColor(resources.getColor(R.color.color_time_date))
    }

    private val isLandscape: Boolean
        private get() = (resources.configuration.orientation
                == Configuration.ORIENTATION_LANDSCAPE)

    private fun DilogOfHeaderCorrect(CorrectTextHeader: View) {
        val dialogListener: OnDialogListener = object : OnDialogListener {
            override fun onDialogName() {
                UserName = CorrectText(headerTitleName!!)
            }

            override fun onDialogProfession() {
                UserProfession = CorrectText(headerTitleProfession!!)
            }

            override fun CorrectText(textView: TextView): String {
                return prepareCustomSnackbar(textView, findViewById(R.id.action_exit))
            }
        }
        CorrectTextHeader.setOnClickListener {
            val dialogFragment = DialogHeaderDrawerFragment.newInstance()
            dialogFragment.setOnDialogListener(dialogListener)
            dialogFragment.show(
                supportFragmentManager,
                "dialog_drawer_header_fragment"
            )
        }
    }

    private fun prepareCustomSnackbar(text_view: TextView, anchor_view: View): String {
        snackbar = Snackbar.make(anchor_view, "", Snackbar.LENGTH_INDEFINITE)
            .setAction(R.string.apply) {
                val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0)
                snackbar!!.dismiss()
            }
        val layout = snackbar!!.view as SnackbarLayout
        snackbar!!.setActionTextColor(Color.RED)
        val sbView = snackbar!!.view
        val Text_view =
            sbView.findViewById<TextView>(com.google.android.material.R.id.snackbar_text)
        Text_view.visibility = View.INVISIBLE
        val snackView = layoutInflater.inflate(R.layout.my_snackbar_layout, null)
        // Configure the view
        val correct_line = snackView.findViewById<EditText>(R.id.correct_box)
        correct_line.setText(text_view.text)
        correct_line.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
                text_view.text = charSequence.toString()
            }

            override fun afterTextChanged(editable: Editable) {}
        })


//If the view is not covering the whole snackbar layout, add this line
        layout.setPadding(0, 0, 0, 0)
        // Add the view to the Snackbar's layout
        layout.addView(snackView, 0)
        // Show the Snackbar
        snackbar!!.setAnchorView(anchor_view)
            .setBackgroundTint(Color.CYAN)
            .setAnimationMode(BaseTransientBottomBar.ANIMATION_MODE_SLIDE)
            .show()
        return text_view.text.toString()
    }

    private fun showAlertDialogWithCustomView() {
        val customView = layoutInflater.inflate(R.layout.alert_dialog_custom_view, null)
        val title = TextView(this)
        // You Can Customise your Title here
        title.text = getString(R.string.exit_alert_dialog)
        title.setBackgroundColor(Color.RED)
        title.setPadding(10, 10, 10, 10)
        title.gravity = Gravity.CENTER
        title.setTextColor(Color.YELLOW)
        title.textSize = 20f
        alert_dialog = AlertDialog.Builder(this)
            .setCustomTitle(title)
            .setView(customView)
            .setPositiveButton(getString(R.string.yes)) { dialogInterface, i ->
                Toast.makeText(
                    this@MainActivity,
                    getString(R.string.Notes_were_closed),
                    Toast.LENGTH_LONG
                ).show()
                finishAndRemoveTask()
            }
            .setNegativeButton(getString(R.string.no)) { dialogInterface, i -> alert_dialog!!.dismiss() }
            .show()
    }

    private fun showNotification() {
// Создаем NotificationChannel, но это делается только для API 26+
// Потому что NotificationChannel -- это новый класс и его нет в suppor library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel()
        }

        // on below line we are creating and initializing
// variable for simple date format.
        val sdf = SimpleDateFormat("'Дата\n'dd-MM-yyyy '\n\nи\n\nВремя\n'HH:mm:ss z")
        // on below line we are creating a variable
// for current date and time and calling a simple date format in it.
        val currentDateAndTime = sdf.format(Date())
        val builder = NotificationCompat.Builder(this, CHANNEL_ID)
        // Все цветные иконки отображаются только в оттенках серого
        builder.setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle(getString(R.string.Current_date_time))
            .setContentText(currentDateAndTime).priority = NotificationCompat.PRIORITY_DEFAULT
        NotificationManagerCompat.from(this).notify(42, builder.build())
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private fun createNotificationChannel() {
        val name = "Name"
        val descriptionText = "Description"
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel(
            CHANNEL_ID, name,
            importance
        )
        channel.description = descriptionText
        // Регистрируем канал в системе
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }

    private fun ThemeChooser() {
        val items = resources.getStringArray(R.array.choose)
        // Создаём билдер и передаём контекст приложения
        val builder = AlertDialog.Builder(this@MainActivity)
        // В билдере указываем заголовок окна . Можно указывать как ресурс ,
// так и строку
        inflator = layoutInflater
        layout =
            inflator!!.inflate(R.layout.theme_chooser, findViewById(R.id.root_title_theme_chooser))
        builder.setCustomTitle(layout) // Добавляем список элементов ; chosen -- выбранный элемент ,
            .setSingleChoiceItems(items, previous_chosen) { dialogInterface, item ->
                chosen = item // Обновляем выбранный элемент
                setAppTheme(chosen)
            }
            .setNegativeButton(getString(R.string.cancel)) { dialogInterface, i ->
                chosen = previous_chosen
                setAppTheme(chosen)
                alert!!.dismiss()
            }
            .setPositiveButton(getString(R.string.apply)) { dialogInterface, i ->
                previous_chosen = chosen
                alert!!.dismiss()
            }
        alert = builder.create()
        alert.show()
    }

    private fun ChangeStyleTheme(
        background_color_tool_bar: Int,
        text_title_color_tool_bar: Int,
        text_subtitle_color_tool_bar: Int,
        text_notes_list_color: Int
    ) {
        val changed_toolbar = findViewById<Toolbar>(R.id.toolbar)
        changed_toolbar.setBackgroundColor(resources.getColor(background_color_tool_bar))
        changed_toolbar.setTitleTextColor(resources.getColor(text_title_color_tool_bar))
        changed_toolbar.setSubtitleTextColor(resources.getColor(text_subtitle_color_tool_bar))
        inflator = layoutInflater
        layout = inflator!!.inflate(
            R.layout.fragment_notes,
            findViewById(R.id.notes_container)
        )
        var changed_color_text: TextView
        notes_text_color = resources.getColor(text_notes_list_color)
        inflator = layoutInflater
        layout = inflator!!.inflate(
            R.layout.fragment_note,
            findViewById(R.id.linear_layout_note)
        )
        note_text_color = text_notes_list_color
        changed_color_text = layout.findViewById(R.id.tvTitle)
        changed_color_text.setTextColor(resources.getColor(text_notes_list_color))
        changed_color_text = layout.findViewById(R.id.tvDescription)
        changed_color_text.setTextColor(resources.getColor(text_notes_list_color))
        if (first != 0) {
            this@MainActivity.supportFragmentManager
                .beginTransaction()
                .replace(R.id.data_container, ListFragmentV2.newInstance())
                .addToBackStack(null)
                .commit()
        }
    }

    private fun setAppTheme(codeStyle: Int) {
        notes_text_color_first = resources.getColor(R.color.teal_200)
        when (codeStyle) {
            first_stile -> {
                ChangeStyleTheme(
                    R.color.set_text_toast,
                    R.color.color_time_date,
                    R.color.teal_200,
                    R.color.teal_200
                )
                return
            }
            second_stile -> {
                ChangeStyleTheme(
                    R.color.purple_500,
                    R.color.set_text_toast,
                    android.R.color.holo_red_light,
                    android.R.color.holo_red_light
                )
                return
            }
            third_style -> {
                ChangeStyleTheme(
                    R.color.color_1,
                    R.color.purple_700,
                    android.R.color.holo_purple,
                    android.R.color.holo_purple
                )
                return
            }
            else -> {
                ChangeStyleTheme(
                    R.color.set_text_toast,
                    R.color.color_time_date,
                    R.color.teal_200,
                    R.color.teal_200
                )
                return
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putInt(CHOSEN_THEME, previous_chosen)
        super.onSaveInstanceState(outState)
    }

    companion object {
        const val CHANNEL_ID = "CHANNEL_ID"
        const val CHOSEN_THEME = "chosen_theme"
        private const val first_stile = 0
        private const val second_stile = 1
        private const val third_style = 2
        @JvmField
        var note_text_color = 0
        protected var notes_text_color = 0
        protected var notes_text_color_first = 0
    }
}