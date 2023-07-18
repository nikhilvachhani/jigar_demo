package com.frami.ui.common.videopicker

import android.os.Parcel
import android.os.Parcelable


class Dir : Parcelable {
    var id: Long = 0
    var name: String? = null
    var preview: String? = null
    var count = 0

    constructor() {}
    protected constructor(`in`: Parcel) {
        id = `in`.readLong()
        name = `in`.readString()
        preview = `in`.readString()
        count = `in`.readInt()
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeLong(id)
        dest.writeString(name)
        dest.writeString(preview)
        dest.writeInt(count)
    }

    companion object {
        val CREATOR: Parcelable.Creator<Dir> = object : Parcelable.Creator<Dir> {
            override fun createFromParcel(`in`: Parcel): Dir {
                return Dir(`in`)
            }

            override fun newArray(size: Int): Array<Dir?> {
                return arrayOfNulls(size)
            }
        }
    }
}