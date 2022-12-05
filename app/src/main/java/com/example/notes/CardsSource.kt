package com.example.notes

import com.example.notes.CardData
import java.util.ArrayList

interface CardsSource {
    fun getCardData(position: Int): CardData?
    fun size(): Int
    fun deleteCardData(position: Int)
    fun updateCardData(position: Int, cardData: CardData?)
    fun addCardData(cardData: CardData?)
    fun clearCardData()
    fun setNewData(dataSource: ArrayList<CardData?>?)
    val cardData: ArrayList<CardData?>?
}