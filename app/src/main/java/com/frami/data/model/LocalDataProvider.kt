package com.frami.data.model

import android.content.Context
import com.frami.R


data class TourDataModel(
    val title: String,
    val text: String,
    val icon: Int? = null
)

object LocalDataProvider {
    fun getTourDataList(context: Context): List<TourDataModel>{
        val list :MutableList<TourDataModel>  = arrayListOf()
        with(list) {
            add(TourDataModel(context.getString(R.string.tour_title_1),context.getString(R.string.tour_text_1)))
            add(TourDataModel(context.getString(R.string.tour_title_2),context.getString(R.string.tour_text_2)))
            add(TourDataModel(context.getString(R.string.tour_title_3),context.getString(R.string.tour_text_3)))
        }
        return list
    }
}