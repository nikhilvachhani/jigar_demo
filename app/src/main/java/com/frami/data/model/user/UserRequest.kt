package com.frami.data.model.user

import com.frami.utils.AppConstants
import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class UserRequest(
    @field:SerializedName("UserId")
    var UserId: String = "",
    @field:SerializedName("userName")
    var userName: String,
//    @field:SerializedName("ProfilePhoto")
//    var ProfilePhoto: File,
    @field:SerializedName("profilePhotoUrl")
    var profilePhotoUrl: String? = null,
    @field:SerializedName("firstName")
    var firstName: String = "",
    @field:SerializedName("lastName")
    var lastName: String = "",
    @field:SerializedName("emailAddress")
    var emailAddress: String = "",
    @field:SerializedName("workEmailAddress")
    var workEmailAddress: String = "",
    @field:SerializedName("birthDate")
    var birthDate: String = "",
    @field:SerializedName("gender")
    var gender: String = AppConstants.GENDER.MALE.type,
//    @field:SerializedName("nationality")
//    var nationality: CountryData? = null,
    @field:SerializedName("Nationality.Name")
    var NationalityName: String = "",
    @field:SerializedName("Nationality.Code")
    var NationalityCode: String = "",
    @field:SerializedName("mobileNumber")
    var mobileNumber: String = "",
    @field:SerializedName("identityProvider")
    var identityProvider: String = "",
    @field:SerializedName("b2CFlow")
    var b2CFlow: String = "",
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
    @field:SerializedName("isWorkEmailVerified")
    var isWorkEmailVerified: Boolean = false,
    @field:SerializedName("isWorkVerificationEmailSent")
    var isWorkVerificationEmailSent: Boolean = false,
//    @field:SerializedName("userDevices")
//    var userDevices: List<UserDevices> = ArrayList(),
) : Serializable {

}