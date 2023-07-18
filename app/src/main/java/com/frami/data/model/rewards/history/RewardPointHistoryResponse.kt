package com.frami.data.model.rewards.history

import com.frami.data.model.BaseResponse
import com.frami.data.model.profile.UserProfileData
import com.frami.data.model.user.User
import com.google.gson.annotations.SerializedName

class RewardPointHistoryResponse : BaseResponse() {
    @SerializedName("data")
    var data: RewardPointHistoryResponseData? = null
}