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
import android.view.LayoutInflater
import android.view.ViewGroup
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
import android.view.MenuInflater
import android.content.DialogInterface
import com.example.notes.NotesFragment
import com.google.android.material.floatingactionbutton.FloatingActionButton
import android.view.ContextMenu
import android.view.ContextMenu.ContextMenuInfo
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.example.notes.DatePickerFragment
import com.example.notes.TimePickerFragment
import com.example.notes.Adapter.SectionPagerAdapter
import android.widget.TimePicker
import android.widget.TimePicker.OnTimeChangedListener
import java.text.SimpleDateFormat
import java.util.*

class Note : Parcelable {
    var date: IntArray?
    var time: IntArray?

    //  private int id;
    var title: String?
    var description: String?
    var creationDate: String?
    var pictureID: Int
    var like: Boolean

    constructor(
        title: String?, description: String?, creationDate: String?,
        date: IntArray?, time: IntArray?, picture_id: Int, like: Boolean
    ) {
        this.title = title
        this.description = description
        this.creationDate = creationDate
        this.date = date
        this.time = time
        pictureID = picture_id
        this.like = like
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    protected constructor(parcel: Parcel) {
        title = parcel.readString()
        description = parcel.readString()
        creationDate = parcel.readString()
        date = parcel.createIntArray()
        time = parcel.createIntArray()
        pictureID = parcel.readInt()
        like = parcel.readBoolean()
    }

    fun getId(note: Note?): Int {
        return notes!!.indexOf(note)
    }

    val timeDateAlarm: String
        get() {
            val date_show: String
            val time_show: String
            val result_time_date_show: String
            val add_zero: String
            add_zero = if (time!![1] < 10) "0" else ""
            time_show = time!![0].toString() + ":" + add_zero + time!![1]
            date_show = date!![0].toString() + "." + date!![1] + "." + date!![2]
            result_time_date_show = "Сигнал: Время $time_show Дата $date_show"
            return result_time_date_show
        }

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(parcel: Parcel, i: Int) {
        parcel.writeString(title)
        parcel.writeString(description)
        parcel.writeString(creationDate)
        parcel.writeIntArray(date)
        parcel.writeIntArray(time)
        parcel.writeInt(pictureID)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            parcel.writeBoolean(like)
        }
    }

    companion object {
        val CREATOR: Creator<Note> = object : Creator<Note?> {
            @RequiresApi(api = Build.VERSION_CODES.Q)
            override fun createFromParcel(parcel: Parcel): Note? {
                return Note(parcel)
            }

            override fun newArray(i: Int): Array<Note?> {
                return arrayOfNulls(i)
            }
        }
        protected val random = Random()
        val notes: ArrayList<Note?>? = null
        var LENGTH_BEGIN = 7
        private const val counter = 0

        init {
            notes = ArrayList()
            for (i in 0 until LENGTH_BEGIN) {
                notes.add(getNote(i))
            }
        }

        @SuppressLint("DefaultLocale")
        fun getNote(index: Int): Note {
            val title = String.format("Заметка %d", index)
            val description = String.format("Описание заметки %d", index)
            val sdf = SimpleDateFormat("'Дата\n'dd-MM-yyyy '\n\nи\n\nВремя\n'HH:mm:ss z")
            // on below line we are creating a variable
// for current date and time and calling a simple date format in it.
            val currentDateAndTime = sdf.format(Date())
            val date = intArrayOf(
                random.nextInt(31) + 1, random.nextInt(11) + 1, 2023 + random.nextInt(2)
            )
            val time = intArrayOf(random.nextInt(24) + 1, random.nextInt(60) + 1)
            return Note(title, description, currentDateAndTime, date, time, 0, false)
        }

        fun clearAll() {
            notes!!.clear()
        }
    }
}