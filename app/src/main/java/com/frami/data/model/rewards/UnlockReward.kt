package com.frami.data.model.rewards

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class UnlockReward(
    @field:SerializedName("rewardId")
    var rewardId: String,
    @field:SerializedName("rewardClaimId")
    var rewardClaimId: String,
    @field:SerializedName("userId")
    var userId: String,
    @field:SerializedName("couponId")
    var couponId: String? = null,
    @field:SerializedName("points")
    var points: Int,
    @field:SerializedName("status")
    var status: String,
    @field:SerializedName("isFavorite")
    var isFavorite: Boolean = false,
    @field:SerializedName("creationDate")
    var creationDate: String,
    @field:SerializedName("id")
    var id: String,
    @field:SerializedName("auditHistory")
    var auditHistory: List<AuditRewardHistory>,
) : Serializable {
}