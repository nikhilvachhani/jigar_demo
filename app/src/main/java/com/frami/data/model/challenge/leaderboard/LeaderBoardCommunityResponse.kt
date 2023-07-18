package com.frami.data.model.challenge.leaderboard

import com.frami.data.model.BaseResponse
import com.frami.data.model.explore.LeaderboardCommunityData
import com.frami.data.model.explore.LeaderboardData
import com.google.gson.annotations.SerializedName

class LeaderBoardCommunityResponse : BaseResponse() {
    @SerializedName("data")
    var data: List<LeaderboardCommunityData>? = ArrayList()
}