package com.example.notes

import com.example.notes.ListFragmentV2.Companion.newInstance
import com.example.notes.DatePickerFragment.Companion.newInstance
import com.example.notes.Adapter.SectionPagerAdapter.addFragment
import android.os.Parcelable
import androidx.annotation.RequiresApi
import android.os.Build
import android.os.Parcel
import android.os.Parcelable.Creator
import android.annotation.SuppressLint
import android.os.Bundle
import com.example.notes.R
import android.widget.TextView
import com.example.notes.NoteFragment
import com.example.notes.MainActivity
import android.text.TextWatcher
import com.example.notes.CardSourceImpl
import com.example.notes.CardData
import com.google.gson.GsonBuilder
import com.example.notes.ListFragmentV2
import android.text.Editable
import android.view.View.OnLongClickListener
import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import com.example.notes.ParentDateTimeFragment
import android.content.DialogInterface
import android.content.res.Configuration
import android.view.*
import com.example.notes.NotesFragment
import com.google.android.material.floatingactionbutton.FloatingActionButton
import android.view.ContextMenu.ContextMenuInfo
import android.widget.Button
import android.widget.PopupMenu
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.example.notes.DatePickerFragment
import com.example.notes.TimePickerFragment
import com.example.notes.Adapter.SectionPagerAdapter
import android.widget.TimePicker
import android.widget.TimePicker.OnTimeChangedListener
import androidx.fragment.app.Fragment

class NoteFragment : Fragment() {
    //  public String mListener;
    var title_was_changed = false
    private val text_color = 0
    private var note: Note? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState != null) requireActivity().supportFragmentManager.popBackStack()
    }

    //Call onActivity Create method
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        return inflater.inflate(R.layout.fragment_note, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val arguments = arguments
        val time_date_alarm = view.findViewById<TextView>(R.id.time_date_alarm_view)
        val tvDescription = view.findViewById<TextView>(R.id.tvDescription)
        //  sharedPreferences = getActivity().getPreferences(Context.MODE_PRIVATE);
        title_was_changed = false
        if (arguments != null) {
            note = arguments.getParcelable(SELECTED_NOTE)
            if (note == null) note = Note.Companion.getNote(0)
            val tvTitle = view.findViewById<TextView>(R.id.tvTitle)
            tvTitle.text = note.getTitle()
            tvTitle.setTextColor(resources.getColor(MainActivity.note_text_color))
            tvTitle.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    charSequence: CharSequence,
                    i: Int,
                    i1: Int,
                    i2: Int
                ) {
                }

                override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
                    val new_title = charSequence.toString()
                    note.setTitle(new_title)
                    if (note!!.getId(note) >= 0) {
                        CardSourceImpl.dataSource[note!!.getId(note)] = CardData(
                            new_title,
                            note.getDescription(), note.getCreationDate(),
                            note.getPictureID(), note!!.getId(note),
                            note.getLike()
                        )
                        val jsonCardDataAfterUpdate = GsonBuilder().create()
                            .toJson(CardSourceImpl.dataSource)
                        ListFragmentV2.sharedPreferences!!.edit()
                            .putString(ListFragmentV2.KEY, jsonCardDataAfterUpdate).apply()
                    }
                    title_was_changed = true
                    var rt = activity!!.findViewById<TextView>(note!!.getId(note))
                    if (rt == null) {
                        val k = 1
                        rt = activity!!.findViewById(k)
                    }
                    if (PDateTime == 0 && rt != null && note != null) rt.text = note.getTitle()
                }

                override fun afterTextChanged(editable: Editable) {}
            })
            time_date_alarm.text = note.getTimeDateAlarm()
            tvDescription.text = note.getDescription()
            tvDescription.setTextColor(resources.getColor(MainActivity.note_text_color))
            tvDescription.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    charSequence: CharSequence,
                    i: Int,
                    i1: Int,
                    i2: Int
                ) {
                }

                override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
                    val new_description = charSequence.toString()
                    note.setDescription(new_description)
                    if (note!!.getId(note) >= 0) {
                        CardSourceImpl.dataSource[note!!.getId(note)] = CardData(
                            note.getTitle(),
                            new_description, note.getCreationDate(),
                            note.getPictureID(), note!!.getId(note),
                            note.getLike()
                        )
                        val jsonCardDataAfterUpdate = GsonBuilder()
                            .create().toJson(CardSourceImpl.dataSource)
                        ListFragmentV2.sharedPreferences!!.edit()
                            .putString(ListFragmentV2.KEY, jsonCardDataAfterUpdate).apply()
                    }
                }

                override fun afterTextChanged(editable: Editable) {}
            })
            time_date_alarm.setOnLongClickListener { view1: View? ->
                initDateTimePopupMenu(time_date_alarm)
                true
            }
            val buttonBack = view.findViewById<Button>(R.id.btnBack)
            buttonBack?.setOnClickListener { view1: View? ->
                if (title_was_changed) {
                    val rt = activity!!.findViewById<TextView>(note!!.getId(note))
                    rt.text = note.getTitle()
                }
                requireActivity().supportFragmentManager.popBackStack()
            }
        }
    }

    private fun initDateTimePopupMenu(view: TextView) {
        val activity: Activity = requireActivity()
        val popupMenu = PopupMenu(activity, view)
        activity.menuInflater.inflate(R.menu.popup_menu, popupMenu.menu)
        val item = popupMenu.menu.findItem(R.id.action_popup_delete)
        if (item != null) {
            item.isVisible = false
        }
        popupMenu.setOnMenuItemClickListener { menuItem -> // Слушатель диалога , сюда попадает управление
            if (menuItem.itemId == R.id.action_popup_correct) {
                val dialogDateTimeFragment: ParentDateTimeFragment =
                    ParentDateTimeFragment.Companion.newInstance(note)
                dialogDateTimeFragment.show(
                    requireActivity().supportFragmentManager,
                    "dialog_time_date_fragment"
                )
            }
            true
        }
        popupMenu.show()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        val CurrentFragment = requireActivity().supportFragmentManager
            .findFragmentByTag("NOTE_FRAGMENT")
        if (!isLandscape && menu != null &&
            CurrentFragment != null && CurrentFragment.isVisible
        ) {
            menu.clear()
            inflater.inflate(R.menu.note_fragment_menu, menu)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        when (id) {
            R.id.send_note -> {
                // Сделать поиск
                createOneButtonAlertDialog("Сделать отослать заметку")
                return true
            }
            R.id.add_photo -> {
                createOneButtonAlertDialog("Сделать добавить фото")
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private val isLandscape: Boolean
        private get() = (resources.configuration.orientation
                == Configuration.ORIENTATION_LANDSCAPE)

    private fun createOneButtonAlertDialog(title_window: String) {
        val builder = AlertDialog.Builder(activity)
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

    companion object {
        //  private OnDialogListener ColorListener;
        const val SELECTED_NOTE = "note"
        var PDateTime = 0
        fun newInstance(note: Note?): NoteFragment {
            val fragment = NoteFragment()
            val args = Bundle()
            args.putParcelable(SELECTED_NOTE, note)
            fragment.arguments = args
            return fragment
        }
    }
}