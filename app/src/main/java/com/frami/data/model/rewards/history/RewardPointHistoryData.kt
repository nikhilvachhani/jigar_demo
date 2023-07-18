package com.frami.data.model.rewards.history

import com.google.gson.annotations.SerializedName

class RewardPointHistoryData {
    @SerializedName("key")
    var key: String = ""

    @SerializedName("value")
    var data: List<RewardPointHistory>? = null
}