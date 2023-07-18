package com.frami.data.model.appconfig

import android.os.Parcel
import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

@Entity
data class AppConfig(
    @PrimaryKey
    var appConfigId: Int = 25349,

    @SerializedName("adType")
    @Expose
    var adType: Int = 0,

    @SerializedName("androidVersion")
    @Expose
    var androidVersion: Int = 0,

    @SerializedName("privacyPolicy")
    @Expose
    var privacyPolicy: String = "",

    @SerializedName("isForcefullyAppUpdateDownload")
    @Expose
    var isForcefullyAppUpdateDownload: Int = 0,

    @SerializedName("forcefullyAppURL")
    @Expose
    var forcefullyAppURL: String? = null,

    @SerializedName("forcefullyAppUpdateDownloadMessage")
    @Expose
    var forcefullyAppUpdateDownloadMessage: String? = null,

    @SerializedName("shareAppContent")
    @Expose
    var shareAppContent: String = ""

) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readString()!!,
        parcel.readInt(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString()!!
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(appConfigId)
        parcel.writeInt(adType)
        parcel.writeInt(androidVersion)
        parcel.writeString(privacyPolicy)
        parcel.writeInt(isForcefullyAppUpdateDownload)
        parcel.writeString(forcefullyAppURL)
        parcel.writeString(forcefullyAppUpdateDownloadMessage)
        parcel.writeString(shareAppContent)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<AppConfig> {
        override fun createFromParcel(parcel: Parcel): AppConfig {
            return AppConfig(parcel)
        }

        override fun newArray(size: Int): Array<AppConfig?> {
            return arrayOfNulls(size)
        }
    }
}