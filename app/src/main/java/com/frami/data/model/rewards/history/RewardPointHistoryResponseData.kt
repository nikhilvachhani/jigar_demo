package com.frami.data.model.rewards.history

import com.frami.data.model.profile.UserProfileData
import com.google.gson.annotations.SerializedName

class RewardPointHistoryResponseData {
    @SerializedName("userPointsDtoWrappers")
    var rewardHistoryData: List<RewardPointHistoryData>? = null

    @SerializedName("userProfileInfo")
    var userProfileInfo: UserProfileData? = null
}