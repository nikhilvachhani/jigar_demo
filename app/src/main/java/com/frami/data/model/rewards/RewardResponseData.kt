package com.frami.data.model.rewards

import com.frami.data.model.BaseResponse
import com.frami.data.model.profile.UserProfileData
import com.google.gson.annotations.SerializedName

class RewardResponseData : BaseResponse() {
    @SerializedName("rewardResponses")
    var data: List<RewardsData>? = null

    @SerializedName("userProfileInfo")
    var userProfileInfo: UserProfileData? = null
}