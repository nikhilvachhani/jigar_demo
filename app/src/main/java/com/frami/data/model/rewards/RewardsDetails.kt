package com.frami.data.model.rewards

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class RewardsDetails(
    @field:SerializedName("couponCode")
    var couponCode: String,
    @field:SerializedName("unlimited")
    var unlimited: Boolean,
    @field:SerializedName("lotteryReward")
    var lotteryReward: Boolean,
    @field:SerializedName("challengeCompleted")
    var challengeCompleted: Boolean,
    @field:SerializedName("isChallengeLinked")
    var isChallengeLinked: Boolean,
    @field:SerializedName("offersRemaining")
    var offersRemaining: Int,
    @field:SerializedName("otherInfo")
    var otherInfo: String,
    @field:SerializedName("challengeId")
    var challengeId: String,
    @field:SerializedName("isEdit")
    var isEdit: Boolean? = false,
) : RewardsData(), Serializable {
}