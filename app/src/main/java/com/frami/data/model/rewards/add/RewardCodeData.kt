package com.frami.data.model.rewards.add

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class RewardCodeData(
    @field:SerializedName("couponId")
    var couponId: String = "",
    @field:SerializedName("couponCode")
    var couponCode: String,
) : Serializable {
    constructor(couponCode: String) : this(couponId = "", couponCode = couponCode)
}