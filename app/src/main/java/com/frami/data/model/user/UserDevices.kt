package com.frami.data.model.user

import com.google.gson.annotations.SerializedName
import java.io.Serializable

//@Entity(tableName = "userDevices")
data class UserDevices(
//    @PrimaryKey(autoGenerate = true)
//    var id: Int,
    @field:SerializedName("deviceType")
    var deviceType: String?,
    @field:SerializedName("deviceName")
    var deviceName: String?,
    @field:SerializedName("deviceId")
    var deviceId: String?,
    @field:SerializedName("userAccessToken")
    var userAccessToken: String? = "",
    @field:SerializedName("userAccessTokenSecret")
    var userAccessTokenSecret: String? = "",
    @field:SerializedName("refreshToken")
    var refreshToken: String? = "",
    @field:SerializedName("deviceUserId")
    var deviceUserId: String? = "",
    @field:SerializedName("isDeviceConnected")
    var isDeviceConnected: Boolean = false,
) : Serializable {

}