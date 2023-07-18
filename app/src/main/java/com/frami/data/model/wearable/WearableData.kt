package com.frami.data.model.wearable

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import java.io.Serializable

@Entity(tableName = "WearableData")
data class WearableData(
    @PrimaryKey
    @field:SerializedName("id")
    var id: Int,
    @field:SerializedName("name")
    var name: String = "",
    @field:SerializedName("icon")
    var icon: Int,
    @field:SerializedName("oauth_token")
    var oAuthToken: String = "",
    @field:SerializedName("oauth_token_secret")
    var oAuthTokenSecret: String = "",
    @field:SerializedName("oauth_verifier")
    var oAuthVerifier: String = "",
    @field:SerializedName("userAccessToken")
    var userAccessToken: String = "",
    @field:SerializedName("userAccessTokenSecret")
    var userAccessTokenSecret: String = "",
    @field:SerializedName("status")
    var isActive: Int = 1
) : Serializable {
    constructor() : this(id = 0, name = "", icon = 0)
}