package com.example.notes

import android.content.res.Resources
import com.example.notes.CardsSource
import com.example.notes.CardSourceImpl
import com.example.notes.CardData
import android.content.res.TypedArray
import com.example.notes.R
import java.util.ArrayList

class CardSourceImpl(private val resources: Resources) : CardsSource {
    init {
        dataSource = ArrayList(Note.LENGTH_BEGIN)
        pictures_global = imageArray
    }

    private val imageArray: IntArray
        private get() {
            val pictures = resources.obtainTypedArray(R.array.pictures)
            val length = pictures.length()
            val answer = IntArray(length)
            for (i in 0 until length) {
                answer[i] = pictures.getResourceId(i, 0)
            }
            return answer
        }

    override fun getCardData(position: Int): CardData {
        return dataSource[position]
    }

    override fun size(): Int {
        return dataSource.size
    }

    override fun deleteCardData(position: Int) {
        dataSource.removeAt(position)
    }

    override fun updateCardData(position: Int, cardData: CardData) {
        dataSource[position] = cardData
    }

    override fun addCardData(cardData: CardData) {
        dataSource.add(cardData)
    }

    override fun clearCardData() {
        dataSource.clear()
    }

    override fun setNewData(dataSource: ArrayList<CardData>) {
        Companion.dataSource = dataSource
    }

    override fun getCardData(): ArrayList<CardData> {
        return dataSource
    }

    companion object {
        @JvmField
        var dataSource: ArrayList<CardData>
        @JvmField
        var pictures_global: IntArray
    }
}