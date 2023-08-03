package com.frami.data.local.db

import androidx.room.TypeConverter
import com.frami.data.model.explore.EmployerData
import com.frami.data.model.lookup.CountryData
import com.frami.data.model.user.UserDevices
import com.frami.data.model.user.UserRoles
import com.google.gson.Gson
import java.util.*

object Converters {
    @JvmStatic
    @TypeConverter
    fun longToDate(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @JvmStatic
    @TypeConverter
    fun dateToLong(date: Date?): Long? {
        return date?.time
    }

    @JvmStatic
    @TypeConverter
    fun stringArrayToJson(stringArray: List<String?>): String = Gson().toJson(stringArray)

    @JvmStatic
    @TypeConverter
    fun jsonToStringArray(json: String?) =
        Gson().fromJson(json, Array<String?>::class.java).toList()

    @JvmStatic
    @TypeConverter
    fun countryDataToJson(data: CountryData?): String = Gson().toJson(data)

    @JvmStatic
    @TypeConverter
    fun jsonToCountryData(json: String?) = Gson().fromJson(json, CountryData::class.java)

    @JvmStatic
    @TypeConverter
    fun employerDataToJson(data: EmployerData?): String = Gson().toJson(data)

    @JvmStatic
    @TypeConverter
    fun jsonToEmployerData(json: String?) = Gson().fromJson(json, EmployerData::class.java)

    @JvmStatic
    @TypeConverter
    fun userDevicesDataToJson(data: List<UserDevices>?): String? =
        if (data.isNullOrEmpty()) Gson().toJson(ArrayList<UserDevices>()) else Gson().toJson(data)

    @JvmStatic
    @TypeConverter
    fun jsonToUserDevices(json: String?) =
        if (json.isNullOrEmpty()) ArrayList<UserDevices>() else
            Gson().fromJson(json, Array<UserDevices>::class.java).toList()

    @JvmStatic
    @TypeConverter
    fun userRolesDataToJson(data: List<UserRoles>?): String? =
        if (data.isNullOrEmpty()) Gson().toJson(ArrayList<UserRoles>()) else Gson().toJson(data)

    @JvmStatic
    @TypeConverter
    fun jsonToUserRoles(json: String?) =
        if (json.isNullOrEmpty()) ArrayList<UserRoles>() else
            Gson().fromJson(json, Array<UserRoles>::class.java).toList()
}
