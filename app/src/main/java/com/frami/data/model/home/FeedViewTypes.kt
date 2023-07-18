package com.frami.data.model.home

import com.frami.data.model.explore.ChallengesData
import com.frami.data.model.explore.CommunityData
import com.frami.data.model.explore.EventsData
import com.frami.data.model.rewards.RewardsDetails
import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class FeedViewTypes(
    @field:SerializedName("startDateTime")
    var startDateTime: String = "",
    @field:SerializedName("screenType")
    var screenType: String = "",
    @field:SerializedName("activity")
    var activity: ActivityData? = null,
    @field:SerializedName("challenge")
    var challenge: ChallengesData? = null,
    @field:SerializedName("event")
    var event: EventsData? = null,
    @field:SerializedName("reward")
    var reward: RewardsDetails? = null,
    @field:SerializedName("community")
    var community: CommunityData? = null,
) : Serializable