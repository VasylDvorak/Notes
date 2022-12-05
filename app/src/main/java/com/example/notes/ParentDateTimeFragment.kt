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
import android.content.res.Configuration
import android.view.*
import com.example.notes.NotesFragment
import com.google.android.material.floatingactionbutton.FloatingActionButton
import android.view.ContextMenu.ContextMenuInfo
import android.widget.Button
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.example.notes.DatePickerFragment
import com.example.notes.TimePickerFragment
import com.example.notes.Adapter.SectionPagerAdapter
import android.widget.TimePicker
import android.widget.TimePicker.OnTimeChangedListener
import androidx.fragment.app.DialogFragment

class ParentDateTimeFragment : DialogFragment() {
    var myFragment: View? = null
    var viewPager: ViewPager? = null
    var tabLayout: TabLayout? = null
    private var datePickerFragment: DatePickerFragment? = null
    private var timePickerFragment: TimePickerFragment? = null
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
        myFragment = inflater.inflate(
            R.layout.fragment_parent_date_time,
            container, false
        )
        viewPager = myFragment.findViewById(R.id.viewPager)
        tabLayout = myFragment.findViewById(R.id.tabLayout)
        return myFragment
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setUpViewPager(viewPager)
        tabLayout!!.setupWithViewPager(viewPager)
        tabLayout!!.setOnClickListener { }
    }

    private fun setUpViewPager(viewPager: ViewPager?) {
        val adapter = SectionPagerAdapter(childFragmentManager)
        datePickerFragment = DatePickerFragment.newInstance(note)
        timePickerFragment = TimePickerFragment.Companion.newInstance(note)
        adapter.addFragment(datePickerFragment!!, "Сменить дату")
        adapter.addFragment(timePickerFragment!!, "Сменить время")
        viewPager!!.adapter = adapter
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val arguments = arguments
        if (arguments != null) {
            note = arguments.getParcelable(SELECTED_NOTE)
        }
        NoteFragment.Companion.PDateTime = 1
        val Parent_button_back = view.findViewById<Button>(R.id.parent_button_back)
        Parent_button_back?.setOnClickListener { view1: View? ->
            NoteFragment.Companion.PDateTime = 0
            dismiss()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        val CurrentFragment =
            requireActivity().supportFragmentManager.findFragmentByTag("PARENT_FRAGMENT")
        if (!isLandscape && menu != null &&
            CurrentFragment != null && CurrentFragment.isVisible
        ) {
            menu.clear()
            inflater.inflate(R.menu.parent_data_time_menu, menu)
        }
    }

    private val isLandscape: Boolean
        private get() = (resources.configuration.orientation
                == Configuration.ORIENTATION_LANDSCAPE)

    companion object {
        const val SELECTED_NOTE = "note"
        fun newInstance(note: Note?): ParentDateTimeFragment {
            val fragment = ParentDateTimeFragment()
            val args = Bundle()
            args.putParcelable(SELECTED_NOTE, note)
            fragment.arguments = args
            return fragment
        }
    }
}