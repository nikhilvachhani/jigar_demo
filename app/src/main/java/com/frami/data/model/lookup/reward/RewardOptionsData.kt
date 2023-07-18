package com.frami.data.model.lookup.reward

import com.frami.data.model.BaseResponse
import com.frami.data.model.common.IdNameData
import com.frami.data.model.rewards.Organizer
import com.google.gson.annotations.SerializedName

class RewardOptionsData : BaseResponse() {
    @SerializedName("generateRewardCodes")
    var generateRewardCodes: List<IdNameData> = ArrayList()

    @SerializedName("addReward")
    var addReward: List<IdNameData> = ArrayList()

    @SerializedName("organizers")
    var organizers: List<Organizer> = ArrayList()
}