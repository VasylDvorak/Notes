package com.example.notes

import android.content.res.Configuration
import android.view.LayoutInflater
import android.view.ViewGroup
import android.os.Bundle
import android.view.View
import com.example.notes.R
import com.example.notes.ParentDateTimeFragment
import android.widget.DatePicker
import android.widget.DatePicker.OnDateChangedListener
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.notes.DatePickerFragment

class DatePickerFragment : Fragment() {
    private var layout: View? = null
    private var inflator: LayoutInflater? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_date_picker, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val arguments = arguments
        // System.out.println(arguments + "date");
        if (arguments != null) {
            val note = arguments.getParcelable<Note>(ParentDateTimeFragment.SELECTED_NOTE)
            val datePicker = view.findViewById<DatePicker>(R.id.datePicker)
            if (!isLandscape) {
                datePicker.spinnersShown = false
                datePicker.calendarViewShown = true
            }
            datePicker.init(
                note!!.date[2],
                note.date[1] - 1,
                note.date[0]
            ) { view_date, year, monthOfYear, dayOfMonth ->
                note.date[0] = view_date.dayOfMonth
                note.date[1] = view_date.month + 1
                note.date[2] = view_date.year
                val notesd = Note.notes
                val indexd = notesd.indexOf(note)
                Note.notes[indexd] = note
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

    private val isLandscape: Boolean
        private get() = (resources.configuration.orientation
                == Configuration.ORIENTATION_LANDSCAPE)

    companion object {
        @JvmStatic
        fun newInstance(note: Note?): DatePickerFragment {
            val fragment = DatePickerFragment()
            val args = Bundle()
            args.putParcelable(ParentDateTimeFragment.SELECTED_NOTE, note)
            fragment.arguments = args
            return fragment
        }
    }
}