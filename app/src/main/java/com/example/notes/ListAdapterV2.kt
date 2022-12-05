package com.example.notes

import com.example.notes.CardsSource.setNewData
import com.example.notes.CardsSource.getCardData
import com.example.notes.CardsSource.size
import com.example.notes.CardData.title
import com.example.notes.CardData.id
import com.example.notes.CardData.isLike
import com.example.notes.CardData.picture
import com.example.notes.CardsSource
import androidx.recyclerview.widget.RecyclerView
import com.example.notes.CardData
import android.view.ViewGroup
import android.view.LayoutInflater
import android.view.View
import com.example.notes.R
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView
import android.widget.CheckBox
import android.view.View.OnLongClickListener
import androidx.fragment.app.Fragment
import com.example.notes.MainActivity
import java.util.ArrayList

class ListAdapterV2(var dateSource: CardsSource, private val fragment: Fragment?) :
    RecyclerView.Adapter<ListAdapterV2.ViewHolder>() {
    private var itemClickListener: OnItemClickListener? = null
    var menuPosition = 0

    fun setNewData(dataSource: ArrayList<CardData?>?) {
        dateSource.setNewData(dataSource)
        notifyDataSetChanged()
    }

    private fun registerContextMenu(itemView: View) {
        fragment?.registerForContextMenu(itemView)
    }

    fun setItemClickListener(itemClickListener: OnItemClickListener?) {
        this.itemClickListener = itemClickListener
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.setData(dateSource.getCardData(position))
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.item_v2, viewGroup, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return dateSource.size()
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val title: TextView

        // private final TextView description;
        private val image: AppCompatImageView
        private val like: CheckBox

        init {
            title = itemView.findViewById(R.id.title)
            // description = itemView.findViewById(R.id.description);
            image = itemView.findViewById(R.id.imageView)
            like = itemView.findViewById(R.id.like)
            registerContextMenu(itemView)
            image.setOnClickListener { view ->
                val position = adapterPosition
                if (itemClickListener != null) itemClickListener!!.onItemClick(view, position)
            }
            image.setOnLongClickListener {
                menuPosition = layoutPosition
                itemView.showContextMenu(15f, 15f)
                true
            }
        }

        fun setData(cardData: CardData?) {
            title.text = cardData!!.title
            title.id = cardData.id
            if (MainActivity.notes_text_color == 0) MainActivity.notes_text_color =
                MainActivity.notes_text_color_first
            title.setTextColor(MainActivity.notes_text_color)
            //    description.setText(cardData.getDescription());
            like.isChecked = cardData.isLike
            image.setImageResource(cardData.picture)
        }
    }
}