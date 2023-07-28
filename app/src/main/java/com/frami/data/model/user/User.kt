package com.frami.data.model.user

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.frami.data.model.lookup.CountryData
import com.frami.utils.AppConstants
import com.google.gson.annotations.SerializedName
import java.io.Serializable

@Entity(tableName = "User")
data class User(
    @PrimaryKey
    @field:SerializedName("id")
    var id: Int = 40304,
    @field:SerializedName("userId")
    var userId: String,
    @field:SerializedName("userName")
    var userName: String,
    @field:SerializedName("profilePhoto")
    var profilePhoto: String? = "",
    @field:SerializedName("profilePhotoUrl")
    var profilePhotoUrl: String? = "",
    @field:SerializedName("firstName")
    var firstName: String = "",
    @field:SerializedName("lastName")
    var lastName: String = "",
    @field:SerializedName("emailAddress")
    var emailAddress: String? = "",
    @field:SerializedName("workEmailAddress")
    var workEmailAddress: String? = "",
    @field:SerializedName("birthDate")
    var birthDate: String? = "",
    @field:SerializedName("gender")
    var gender: String? = AppConstants.GENDER.MALE.type,
    @field:SerializedName("mobileNumber")
    var mobileNumber: String? = "",
    @field:SerializedName("nationality")
    var nationality: CountryData? = null,
    @field:SerializedName("identityProvider")
    var identityProvider: String? = "",
    @field:SerializedName("b2CFlow")
    var b2CFlow: String? = "",
    @field:SerializedName("isPersonalInfoCompleted")
    var isPersonalInfoCompleted: Boolean = false,
    @field:SerializedName("isContactInfoCompleted")
    var isContactInfoCompleted: Boolean = false,
    @field:SerializedName("isPrivacyPolicyAgreed")
    var isPrivacyPolicyAgreed: Boolean = false,
    @field:SerializedName("isSendNotification")
    var isSendNotification: Boolean = false,
    @field:SerializedName("isDeviceConnected")
    var isDeviceConnected: Boolean = false,
    @field:SerializedName("isWelcomeEmailSent")
    var isWelcomeEmailSent: Boolean = false,
    @field:SerializedName("isVerificationEmailSent")
    var isVerificationEmailSent: Boolean = false,
    @field:SerializedName("isEmailVerified")
    var isEmailVerified: Boolean = false,
    @field:SerializedName("isWorkVerificationEmailSent")
    var isWorkVerificationEmailSent: Boolean = false,
    @field:SerializedName("isWorkEmailVerified")
    var isWorkEmailVerified: Boolean = false,
    @field:SerializedName("userDevices")
    var userDevices: List<UserDevices>? = ArrayList(),
    @field:SerializedName("userRoles")
    var userRoles: List<UserRoles>? = ArrayList(),
    @field:SerializedName("isPrivacySettingCompleted")
    var isPrivacySettingCompleted: Boolean = false,
    @field:SerializedName("isEmployerConnected")
    var isEmployerConnected: Boolean = false,

    @field:SerializedName("totalPoints")
    var totalPoints: Int,
    @field:SerializedName("level")
    var level: String?,
    @field:SerializedName("pointToNextLevel")
    var pointToNextLevel: String?
) : Serializable {

}