package com.frami.utils

import androidx.room.TypeConverter
import com.frami.data.model.activity.comment.CommentData
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class DataTypeConverter {
    private val gson = Gson()


    @TypeConverter
    fun fieldsToStringList(data: String): List<String> {
        val listType = object : TypeToken<List<String>>() {}.type
        return gson.fromJson(data, listType)
    }

    @TypeConverter
    fun ListToStringFields(someObjects: List<String>): String {
        return gson.toJson(someObjects)
    }

    @TypeConverter
    fun stringToComment(data: String): CommentData {
        val listType = object : TypeToken<CommentData>() {}.type
        return gson.fromJson(data, listType)
    }

    @TypeConverter
    fun commentToStringFields(someObjects: CommentData): String {
        return gson.toJson(someObjects)
    }
}