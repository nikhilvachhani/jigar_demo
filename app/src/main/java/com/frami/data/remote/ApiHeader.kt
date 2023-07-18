package com.frami.data.remote

import com.frami.data.local.pref.PreferencesHelper
import com.google.gson.annotations.SerializedName
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ApiHeader @Inject constructor(private val preferencesHelper: PreferencesHelper) {
    fun getApiHeader(): TokenHeader {
        return if (preferencesHelper.getContinuousToken()
                .isNullOrEmpty() || preferencesHelper.getContinuousToken()
                ?.compareTo("NoMoreRecords") == 0
        ) TokenHeader(preferencesHelper.getAccessToken()) else TokenHeaderContinuous(
            preferencesHelper.getAccessToken(),
            preferencesHelper.getContinuousToken()
        )
    }

    class TokenHeaderContinuous(accessToken: String, continuationToken: String? = null) :
        TokenHeader(accessToken) {
//        @SerializedName("Authorization")
//        private val mAccessToken: String = String.format("Bearer %s", accessToken)
//
//        @SerializedName("Accept")
//        private val acceptHeader: String = "application/json"

        @SerializedName("continuationToken")
        private val continuationToken: String = String.format("%s", continuationToken)

    }

    open class TokenHeader(accessToken: String) {
        @SerializedName("Authorization")
        private val mAccessToken: String = String.format("Bearer %s", accessToken)

        @SerializedName("Accept")
        private val acceptHeader: String = "application/json"
    }
}