package com.frami.data.model.lookup.community

import com.frami.data.model.BaseResponse
import com.frami.data.model.common.IdNameData
import com.google.gson.annotations.SerializedName

class CommunityOptionsData : BaseResponse() {

    @SerializedName("privacy")
    var privacy: List<IdNameData> = ArrayList()

    @SerializedName("inviteFrom")
    var inviteFrom: List<IdNameData> = ArrayList()

    @SerializedName("chooseFrom")
    var chooseFrom: List<IdNameData> = ArrayList()

    @SerializedName("category")
    var category: List<IdNameData> = ArrayList()
}