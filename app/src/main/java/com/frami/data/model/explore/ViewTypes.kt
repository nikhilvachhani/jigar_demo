package com.frami.data.model.explore

import android.os.Parcel
import android.os.Parcelable
import com.frami.data.model.common.EmptyData
import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class ViewTypes(
    @field:SerializedName("name")
    var name: String? = "",
    @field:SerializedName("viewType")
    var viewType: Int = 0,
    @field:SerializedName("data")
    var data: ArrayList<Any> = ArrayList(),
    @field:SerializedName("isShowAll")
    var isShowAll: Boolean = true,
    @field:SerializedName("empty")
    var emptyData: EmptyData? = null,
) : Serializable, Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readInt(),
        parcel.readArrayList(Any::class.java.classLoader) as ArrayList<Any>,
        parcel.readByte() != 0.toByte(),
        parcel.readParcelable(EmptyData::class.java.classLoader),
    )

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(p0: Parcel, p1: Int) {

    }

    companion object CREATOR : Parcelable.Creator<ViewTypes> {
        override fun createFromParcel(parcel: Parcel): ViewTypes {
            return ViewTypes(parcel)
        }

        override fun newArray(size: Int): Array<ViewTypes?> {
            return arrayOfNulls(size)
        }
    }
}