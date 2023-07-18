package com.frami.data.model.explore

import com.frami.data.model.rewards.RewardsData
import com.google.gson.annotations.SerializedName

class ExploreResponseData {
    @SerializedName("employer")
    var employer: EmployerData? = null

    @SerializedName("challenges")
    var challenges: List<ChallengesData>? = ArrayList()

    @SerializedName("rewards")
    var rewards: List<RewardsData>? = ArrayList()

    @SerializedName("events")
    var events: List<EventsData>? = ArrayList()

    @SerializedName("communities")
    var communities: List<CommunityData>? = null
}