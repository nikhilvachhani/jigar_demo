package com.frami.data.model.home

import com.frami.data.model.BaseResponse
import com.frami.data.model.explore.ChallengesData
import com.frami.data.model.profile.UserProfileData
import com.frami.data.model.user.User
import com.google.gson.annotations.SerializedName

class HomeFeedNewResponse : BaseResponse() {
    @SerializedName("data")
    var data: List<FeedDataNew>? = ArrayList()
}

class FeedDataNew {
    @field:SerializedName("userProfileData")
    var userProfileData: User? = null
    @field:SerializedName("activity")
    var activity: ActivityData? = null
    @SerializedName("challenge")
    var challenge: List<ChallengesData>? = null
}

