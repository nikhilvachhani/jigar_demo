package com.frami.data.model.challenge.leaderboard

import com.frami.data.model.BaseResponse
import com.frami.data.model.explore.LeaderboardData
import com.google.gson.annotations.SerializedName

class LeaderBoardResponse : BaseResponse() {
    @SerializedName("data")
    var data: List<LeaderboardData>? = ArrayList()
}