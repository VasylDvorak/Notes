package com.example.notes

import android.app.AlertDialog
import android.content.Context
import com.example.notes.CardsSource.addCardData
import com.example.notes.CardsSource.size
import com.example.notes.CardsSource.cardData
import com.example.notes.CardsSource.clearCardData
import com.example.notes.ListAdapterV2.setNewData
import com.example.notes.CardData.title
import com.example.notes.CardData.description
import com.example.notes.CardData.currentDateAndTime
import com.example.notes.CardData.picture
import com.example.notes.CardData.isLike
import com.example.notes.ListAdapterV2.setItemClickListener
import com.example.notes.ListAdapterV2.menuPosition
import com.example.notes.CardsSource.getCardData
import com.example.notes.CardData.id
import com.example.notes.CardsSource.deleteCardData
import com.example.notes.CardsSource.updateCardData
import com.example.notes.CardsSource
import com.example.notes.ListAdapterV2
import androidx.recyclerview.widget.RecyclerView
import com.example.notes.CardData
import com.example.notes.R
import com.example.notes.ListFragmentV2
import com.example.notes.CardSourceImpl
import com.google.gson.GsonBuilder
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.DefaultItemAnimator
import android.widget.Toast
import com.google.gson.reflect.TypeToken
import com.example.notes.NotesFragment
import android.view.ContextMenu.ContextMenuInfo
import android.widget.TextView
import com.example.notes.NoteFragment
import android.content.DialogInterface
import android.widget.EditText
import android.text.TextWatcher
import android.text.Editable
import android.content.SharedPreferences
import android.content.res.Configuration
import android.graphics.Color
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*

class ListFragmentV2 : Fragment() {
    private var note: Note? = null
    var data: CardsSource? = null
        private set
    private var adapter: ListAdapterV2? = null
    private var recyclerView: RecyclerView? = null
    private var alert_dialog: AlertDialog? = null
    private var position = 0
    private var cardData: CardData? = null
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.cards_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        sharedPreferences = activity!!.getPreferences(Context.MODE_PRIVATE)
        val pic = CardSourceImpl.pictures_global[Note.random.nextInt(6) + 1]
        when (item.itemId) {
            R.id.action_add -> {
                val sdf = SimpleDateFormat("'Дата\n'dd-MM-yyyy '\n\nи\n\nВремя\n'HH:mm:ss z")
                // on below line we are creating a variable
// for current date and time and calling a simple date format in it.
                val currentDateAndTime = sdf.format(Date())
                // добавление нового элемента
                data!!.addCardData(
                    CardData(
                        "Заметка " + data!!.size(),
                        String.format("Описание заметки %d", data!!.size()),
                        currentDateAndTime,
                        pic,
                        data!!.size(),
                        false
                    )
                )
                val notea = Note(
                    "Заметка " + (data!!.size() - 1),
                    String.format("Описание заметки %d", data!!.size() - 1),
                    currentDateAndTime,
                    intArrayOf(1, 1, 2023),
                    intArrayOf(8, 0),
                    pic,
                    false
                )
                Note.notes.add(notea)
                // нотификация добавления нового элемемента
                adapter!!.notifyItemInserted(data!!.size() - 1)
                // перлистываем список
                recyclerView!!.scrollToPosition(data!!.size() - 1)
                recyclerView!!.smoothScrollToPosition(data!!.size() - 1)
                val jsonCardDataAfterAdd = GsonBuilder().create().toJson(data!!.cardData)
                sharedPreferences.edit().putString(KEY, jsonCardDataAfterAdd).apply()
                return true
            }
            R.id.action_clear -> {
                //чистка списка
                data!!.clearCardData()
                Note.clearAll()
                // нотификация, измернение элементов
                adapter!!.notifyDataSetChanged()
                val jsonCardDataAfterClear = GsonBuilder().create().toJson(data!!.cardData)
                sharedPreferences.edit().putString(KEY, jsonCardDataAfterClear).apply()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_list_v2, container, false)
        initView(view)
        setHasOptionsMenu(true)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (savedInstanceState != null) {
            note = savedInstanceState.getParcelable(SELECTED_NOTE)
        }
    }

    private fun initView(view: View) {
        recyclerView = view.findViewById(R.id.recycler_view_lines)
        data = CardSourceImpl(resources) //new CardSourceImpl(getResources()).init();
        initRecycleView()
    }

    private fun initRecycleView() {
        val linearLayoutManager = LinearLayoutManager(context)
        recyclerView!!.layoutManager = linearLayoutManager
        // ListAdapterV2 listAdapter = new ListAdapterV2(data);
        adapter = ListAdapterV2(data!!, this)
        recyclerView!!.adapter = adapter
        val itemDecoration = DividerItemDecoration(context, LinearLayoutManager.VERTICAL)
        itemDecoration.setDrawable(resources.getDrawable(R.drawable.seporatpor, null))
        recyclerView!!.addItemDecoration(itemDecoration)
        val defaultItemAnimator = DefaultItemAnimator()
        defaultItemAnimator.addDuration = DURATION.toLong()
        defaultItemAnimator.removeDuration = DURATION.toLong()
        recyclerView!!.itemAnimator = defaultItemAnimator
        sharedPreferences = activity!!.getPreferences(Context.MODE_PRIVATE)
        val savedData = sharedPreferences.getString(KEY, null)
        if (savedData == null) {
            Toast.makeText(context, R.string.empty, Toast.LENGTH_LONG).show()
        } else {
            try {
                val type = object : TypeToken<List<CardData?>?>() {}.type
                val data_source = GsonBuilder().create()
                    .fromJson<ArrayList<CardData?>>(savedData, type)
                adapter!!.setNewData(data_source)
                Note.clearAll()
                for (from_database in data_source) {
                    val noteaa = Note(
                        from_database!!.title,
                        from_database.description,
                        from_database.currentDateAndTime,
                        intArrayOf(1, 1, 2023),
                        intArrayOf(8, 0),
                        from_database.picture,
                        from_database.isLike
                    )
                    Note.notes.add(noteaa)
                }
            } catch (e: Exception) {
                Toast.makeText(context, R.string.error, Toast.LENGTH_LONG).show()
            }
        }
        adapter!!.setItemClickListener { view, position ->
            NotesFragment.index = position
            showNoteDetails(Note.notes[position])
        }
    }

    override fun onCreateContextMenu(
        menu: ContextMenu, v: View,
        menuInfo: ContextMenuInfo?
    ) {
        super.onCreateContextMenu(menu, v, menuInfo)
        val menuInflater = requireActivity().menuInflater
        menuInflater.inflate(R.menu.card_menu, menu)
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        //  getActivity().getSharedPreferences()
        sharedPreferences = activity!!.getPreferences(Context.MODE_PRIVATE)
        position = adapter!!.menuPosition
        when (item.itemId) {
            R.id.action_update -> {
                cardData = data!!.getCardData(position)
                val update_title = activity!!.findViewById<TextView>(cardData!!.id)
                showAlertDialogWithCustomView(update_title)
                return true
            }
            R.id.action_delete -> {
                data!!.deleteCardData(position)
                Note.notes.removeAt(position)
                adapter!!.notifyItemRemoved(position)
                val jsonCardDataAfterDelete = GsonBuilder()
                    .create().toJson(data!!.cardData)
                sharedPreferences.edit().putString(KEY, jsonCardDataAfterDelete).apply()
                return true
            }
        }
        return super.onContextItemSelected(item)
    }

    private fun showNoteDetails(note: Note) {
        if (isLandscape) {
            showLandNoteDetails(note)
        } else {
            showPortNoteDetails(note)
        }
    }

    private fun showPortNoteDetails(note: Note) {
        val noteFragment = NoteFragment.newInstance(note)
        val fragmentManager = requireActivity().supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.add(R.id.notes_container, noteFragment, "NOTE_FRAGMENT")
        fragmentTransaction.addToBackStack("")
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
        fragmentTransaction.commit()
    }

    private fun showLandNoteDetails(note: Note) {
        val noteFragment = NoteFragment.newInstance(note)
        val fragmentManager = requireActivity().supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.note_container, noteFragment) // замена  фрагмента
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
        fragmentTransaction.commit()
    }

    private val isLandscape: Boolean
        private get() = (resources.configuration.orientation
                == Configuration.ORIENTATION_LANDSCAPE)

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putParcelable(SELECTED_NOTE, note)
        super.onSaveInstanceState(outState)
    }

    private fun showAlertDialogWithCustomView(textView: TextView) {
        val customView = layoutInflater
            .inflate(R.layout.alert_dialog_correct_title, null)
        val title = TextView(context)
        // You Can Customise your Title here
        title.text = getString(R.string.correct_title)
        title.setBackgroundColor(Color.GRAY)
        title.setPadding(10, 10, 10, 10)
        title.gravity = Gravity.CENTER
        title.setTextColor(Color.YELLOW)
        title.textSize = 20f
        alert_dialog = AlertDialog.Builder(context)
            .setCustomTitle(title)
            .setView(customView)
            .setPositiveButton(getString(R.string.btnBack)) { dialogInterface, i -> alert_dialog!!.dismiss() }
            .show()
        val editText = customView.findViewById<EditText>(R.id.edit_title)
        editText.setText(textView.text)
        editText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {}
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                val strr = s.toString()
                textView.text = strr
                TitleChanging(strr)
            }
        })
    }

    private fun TitleChanging(str: String) {
        data!!.updateCardData(
            position, CardData(
                str,
                cardData!!.description, cardData!!.currentDateAndTime,
                cardData!!.picture, cardData!!.id, cardData!!.isLike
            )
        )
        adapter!!.notifyItemChanged(position)
        val jsonCardDataAfterUpdate = GsonBuilder().create().toJson(data!!.cardData)
        sharedPreferences!!.edit().putString(KEY, jsonCardDataAfterUpdate).apply()
        val notesd = Note.notes
        val noted = notesd[position]
        noted.title = str
        Note.notes[position] = noted
    }

    companion object {
        const val KEY = "KEY"
        const val SELECTED_NOTE = "none"
        private const val DURATION = 2000
        @JvmField
        var sharedPreferences: SharedPreferences? = null
        @JvmStatic
        fun newInstance(): ListFragmentV2 {
            return ListFragmentV2()
        }
    }
}