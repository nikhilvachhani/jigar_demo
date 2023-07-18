package com.frami.data.model.rewards

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class OrganizerParentView(
    @field:SerializedName("name")
    var name: String?,
    @field:SerializedName("organizer")
    var organizer: List<Organizer> = ArrayList(),
) : Serializable {
}