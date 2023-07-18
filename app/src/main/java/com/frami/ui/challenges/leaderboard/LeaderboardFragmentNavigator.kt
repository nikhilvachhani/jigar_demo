package com.frami.ui.challenges.leaderboard

import com.frami.data.model.explore.LeaderboardCommunityData
import com.frami.data.model.explore.LeaderboardData
import com.frami.ui.base.BaseNavigator

interface LeaderboardFragmentNavigator :
    BaseNavigator {
    fun leaderBoardFetchSuccessfully(list: List<LeaderboardData>?)
    fun leaderBoardCommunityFetchSuccessfully(list: List<LeaderboardCommunityData>?)
}