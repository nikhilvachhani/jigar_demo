package com.frami.data.model.invite

import com.frami.data.local.db.AppDatabase
import com.frami.utils.AppConstants
import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class ParticipantData(
    @field:SerializedName("userId")
    var userId: String? = "",
    @field:SerializedName("userName")
    var userName: String = "",
    @field:SerializedName("participantStatus")
    var participantStatus: String?,
    @field:SerializedName("memberStatus")
    var memberStatus: String?,
    @field:SerializedName("profilePhotoUrl")
    var profilePhotoUrl: String?,
    var isSelected: Boolean = false,
    var isNotEditable: Boolean = false,
    var isInviteNotVisible: Boolean = false,
) : Serializable {
    constructor() : this(
        userId = "",
        userName = "",
        profilePhotoUrl = null,
        participantStatus = null,
        memberStatus = null,
    )

    fun isAlreadyParticipant() =
        (participantStatus == AppConstants.KEYS.Joined || participantStatus == AppConstants.KEYS.Accepted || participantStatus == AppConstants.KEYS.NoResponse) ||
                (memberStatus == AppConstants.KEYS.Joined || memberStatus == AppConstants.KEYS.NoResponse) //TODO This is not working properly

    fun isLoggedInUser() = AppDatabase.db.userDao().getById()?.userId == userId

    fun isPreSelectedForActivity() = participantStatus == AppConstants.KEYS.Joined
}