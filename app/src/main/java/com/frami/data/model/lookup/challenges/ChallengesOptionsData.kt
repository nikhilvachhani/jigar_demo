package com.frami.data.model.lookup.challenges

import com.frami.data.model.BaseResponse
import com.frami.data.model.common.IdNameData
import com.frami.data.model.rewards.Organizer
import com.google.gson.annotations.SerializedName

class ChallengesOptionsData : BaseResponse() {
    @SerializedName("challengeType")
    var challengeType: List<IdNameData> = ArrayList()

    @SerializedName("challengeTypeCategory")
    var challengeTypeCategory: List<IdNameData> = ArrayList()

    @SerializedName("privacy")
    var privacy: List<IdNameData> = ArrayList()

    @SerializedName("duration")
    var duration: List<IdNameData> = ArrayList()

    @SerializedName("minLevelCriteria")
    var minLevelCriteria: List<IdNameData> = ArrayList()

    @SerializedName("maxLevelCriteria")
    var maxLevelCriteria: List<IdNameData> = ArrayList()

    @SerializedName("inviteFrom")
    var inviteFrom: List<IdNameData> = ArrayList()

    @SerializedName("chooseFrom")
    var chooseFrom: List<IdNameData> = ArrayList()

    @SerializedName("addReward")
    var addReward: List<IdNameData> = ArrayList()

    @SerializedName("challengeCompetitor")
    var challengeCompetitor: List<IdNameData> = ArrayList()

//    @SerializedName("whoCanJoin")
//    var whoCanJoin: List<IdNameData> = ArrayList()

    @SerializedName("howToUnlock")
    var howToUnlock: List<IdNameData> = ArrayList()

    @SerializedName("challengeTypeTargetUnits")
    var challengeTypeTargetUnits: List<ChallengeTypeTargetUnit> = ArrayList()

    @SerializedName("organizers")
    var organizers: List<Organizer> = ArrayList()
}