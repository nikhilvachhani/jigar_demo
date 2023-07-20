package com.frami.data.model.lookup.user

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class UserOptionsResponseData(
    @field:SerializedName("sectionKey")
    var sectionKey: String = "",
    @field:SerializedName("sectionTitle")
    var sectionTitle: String = "",
    @field:SerializedName("sectionDecription")
    var sectionDecription: String = "",
    @field:SerializedName("subsectionList")
    var subsectionList: List<SubSectionData> = ArrayList(),
) : Serializable {
}

data class SubSectionData(
    @field:SerializedName("key")
    var key: String = "",
    @field:SerializedName("title")
    var title: String = "",
    @field:SerializedName("description")
    var description: String = "",
    @field:SerializedName("isSelected")
    var isSelected: Boolean = false,
) : Serializable {
    constructor() : this(
        key = "",
        title = "",
        description = "",
        isSelected = false,
    )
}