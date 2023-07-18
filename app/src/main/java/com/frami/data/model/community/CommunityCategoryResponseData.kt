package com.frami.data.model.community

import com.frami.data.model.explore.CommunityData
import com.frami.data.model.explore.EventsData
import com.google.gson.annotations.SerializedName

class CommunityCategoryResponseData {

    @SerializedName("communitiesIAmIn")
    var communitiesIAmIn: List<CommunityData>? = ArrayList()

    @SerializedName("networkCommunities")
    var networkCommunities: List<CommunityData>? = ArrayList()

    @SerializedName("recommendedCommunities")
    var recommendedCommunities: List<CommunityData>? = ArrayList()

}