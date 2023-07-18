package com.frami.data.model.explore

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class ChallengeRewardData(
    @field:SerializedName("title")
    var title: String = "",
    @field:SerializedName("description")
    var description: String = "",
    @field:SerializedName("participantStatus")
    var participantStatus: String?,
    @field:SerializedName("rewardImagesUrl")
    var rewardImagesUrl: List<String> = ArrayList(),
    @field:SerializedName("points")
    var points: Int? = 0,
    @field:SerializedName("whoCanJoin")
    var whoCanJoin: String = "",
    @field:SerializedName("howToUnlock")
    var howToUnlock: String = "",
) : Serializable {
}