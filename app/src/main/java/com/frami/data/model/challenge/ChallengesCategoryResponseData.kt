package com.frami.data.model.challenge

import com.frami.data.model.explore.ChallengesData
import com.google.gson.annotations.SerializedName

class ChallengesCategoryResponseData {

    @SerializedName("myChallenges")
    var myChallenges: List<ChallengesData>? = ArrayList()

    @SerializedName("networkChallenges")
    var networkChallenges: List<ChallengesData>? = ArrayList()

    @SerializedName("recommededChallenges")
    var recommendedChallenges: List<ChallengesData>? = ArrayList()

}