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
import com.example.notes.ParentDateTimeFragment
import android.content.DialogInterface
import android.view.*
import com.example.notes.NotesFragment
import com.google.android.material.floatingactionbutton.FloatingActionButton
import android.view.ContextMenu.ContextMenuInfo
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.example.notes.DatePickerFragment
import com.example.notes.TimePickerFragment
import com.example.notes.Adapter.SectionPagerAdapter
import android.widget.TimePicker
import android.widget.TimePicker.OnTimeChangedListener
import androidx.fragment.app.Fragment
import java.util.ArrayList

class TimePickerFragment : Fragment() {
    private var layout: View? = null
    private var inflator: LayoutInflater? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_time_picker, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val arguments = arguments
        if (arguments != null) {
            val note = arguments.getParcelable<Note>(NoteFragment.Companion.SELECTED_NOTE)
            val timePicker = view.findViewById<TimePicker>(R.id.timePicker)
            timePicker.setIs24HourView(true)
            timePicker.hour = note!!.time!![0]
            timePicker.minute = note.time!![1]
            timePicker.setOnTimeChangedListener { view_time, hourOfDay, minute ->
                note.time!![0] = hourOfDay
                note.time!![1] = minute
                val notesd: ArrayList<Note?> = Note.Companion.getNotes()
                val indexd = notesd.indexOf(note)
                Note.Companion.getNotes().set(indexd, note)
                inflator = layoutInflater
                layout = inflator!!.inflate(
                    R.layout.fragment_note,
                    activity!!.findViewById(R.id.linear_layout_note)
                )
                val time_date_alarm = layout.findViewById<TextView>(R.id.time_date_alarm_view)
                time_date_alarm.text = note.timeDateAlarm
            }
        }
    }

    companion object {
        fun newInstance(note: Note?): TimePickerFragment {
            val fragment = TimePickerFragment()
            val args = Bundle()
            args.putParcelable(NoteFragment.Companion.SELECTED_NOTE, note)
            fragment.arguments = args
            return fragment
        }
    }
}