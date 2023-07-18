package com.frami.data.model.rewards.request

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class RewardAddToFavouriteRequest(
    @field:SerializedName("rewardId")
    var rewardId: String = "",
    @field:SerializedName("isFavorite")
    var isFavorite: Boolean,
) : Serializable {

}