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
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.example.notes.DatePickerFragment
import com.example.notes.TimePickerFragment
import com.example.notes.Adapter.SectionPagerAdapter
import android.widget.TimePicker
import android.widget.TimePicker.OnTimeChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction

class NotesFragment : Fragment() {
    private var note: Note? = null
    override fun onSaveInstanceState(outState: Bundle) {
        outState.putParcelable(SELECTED_NOTE, note)
        super.onSaveInstanceState(outState)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_notes, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (savedInstanceState != null) {
            note = savedInstanceState.getParcelable(SELECTED_NOTE)
        }
        initNotes(view.findViewById(R.id.data_container))
        if (isLandscape && Note.Companion.getNotes().size != 0) showLandNoteDetails(
            Note.Companion.getNotes().get(
                index
            )
        )
        val btnOne = view.findViewById<FloatingActionButton>(R.id.about)
        btnOne.setOnClickListener { v: View? -> createOneButtonAlertDialog("О приложении") }
    }

    private fun showLandNoteDetails(note: Note?) {
        val noteFragment: NoteFragment = NoteFragment.Companion.newInstance(note)
        val fragmentManager = requireActivity().supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.note_container, noteFragment) // замена  фрагмента
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
        fragmentTransaction.commit()
    }

    private val isLandscape: Boolean
        private get() = (resources.configuration.orientation
                == Configuration.ORIENTATION_LANDSCAPE)

    private fun initNotes(view: View) {
        addFragment(newInstance())
    }

    override fun onCreateContextMenu(
        menu: ContextMenu, v: View,
        menuInfo: ContextMenuInfo?
    ) {
        super.onCreateContextMenu(menu, v, menuInfo)
        val inflater = activity!!.menuInflater
        inflater.inflate(R.menu.context_menu, menu)
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.add -> createOneButtonAlertDialog("Реализовать добавить")
            R.id.sort -> createOneButtonAlertDialog("Реализовать сортировать")
            else -> return super.onContextItemSelected(item)
        }
        return true
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.clear()
        inflater.inflate(R.menu.menu, menu)
    }

    private fun createOneButtonAlertDialog(title_window: String) {
        val builder = AlertDialog.Builder(activity)
        builder.setTitle(title_window)
            .setMessage(R.string.version)
            .setPositiveButton(R.string.ok) { dialog, which -> }
        val d: Dialog = builder.show()
        var textViewId = d.context.resources
            .getIdentifier("android:id/alertTitle", null, null)
        var tv = d.findViewById<TextView>(textViewId)
        tv.setTextColor(resources.getColor(R.color.set_text_toast))
        tv.setBackgroundColor(resources.getColor(R.color.teal_200))
        textViewId = d.context.resources
            .getIdentifier("android:id/message", null, null)
        tv = d.findViewById(textViewId)
        tv.setTextColor(resources.getColor(android.R.color.holo_red_dark))
        textViewId = d.context.resources
            .getIdentifier("android:id/button1", null, null)
        val b = d.findViewById<Button>(textViewId)
        b.setTextColor(resources.getColor(android.R.color.holo_green_dark))
        b.setBackgroundColor(resources.getColor(R.color.color_time_date))
    }

    private fun addFragment(fragment: Fragment) {
        requireActivity().supportFragmentManager
            .beginTransaction()
            .replace(R.id.data_container, fragment)
            .addToBackStack(null)
            .commit()
    }

    companion object {
        const val SELECTED_NOTE = "none"
        var index = 0
        protected var previous_position = 0
    }
}