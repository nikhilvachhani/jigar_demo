package com.frami.ui.personalityinfo.contactinfo

import com.frami.data.model.explore.CommunityData
import com.frami.data.model.user.User
import com.frami.ui.base.BaseNavigator

interface ContactInfoFragmentNavigator :
    BaseNavigator {
    fun verificationEmailSentSuccess(workEmail: Boolean, emailId: String)
    fun communityJoinByCode(data: CommunityData?)
}