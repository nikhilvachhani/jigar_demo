package com.frami.ui.dialog

import android.app.Activity
import android.widget.FrameLayout
import androidx.databinding.DataBindingUtil
import com.frami.BuildConfig
import com.frami.R
import com.frami.databinding.DialogSignupBinding
import com.frami.utils.CommonUtils
import com.frami.utils.NetworkUtils
import com.frami.utils.b2c.B2CConfiguration
import com.frami.utils.b2c.B2CUser
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.gson.Gson
import com.microsoft.identity.client.*
import com.microsoft.identity.client.exception.MsalClientException
import com.microsoft.identity.client.exception.MsalException
import com.microsoft.identity.client.exception.MsalServiceException

class SignupDialog(
    private val mActivity: Activity,
    private val onDialogActionListener: OnDialogActionListener? = null
) : BottomSheetDialog(mActivity, R.style.BottomSheetDialogStyle) {

    private var bindingSheet: DialogSignupBinding? = null
    private var users: List<B2CUser>? = null
    private var b2cApp: IMultipleAccountPublicClientApplication? = null

    override fun show() {
        super.show()
        val bottomSheet =
            findViewById<FrameLayout>(com.google.android.material.R.id.design_bottom_sheet)
        if (bottomSheet != null) {
            BottomSheetBehavior.from(bottomSheet).state = BottomSheetBehavior.STATE_EXPANDED
        }
    }

    init {
        this.bindingSheet = DataBindingUtil.inflate<DialogSignupBinding>(
            this.layoutInflater,
            R.layout.dialog_signup,
            null,
            false
        )
        this.setContentView(bindingSheet!!.root)
        setData()
        clickListener()
    }

    private fun clickListener() {
        bindingSheet!!.ivClose.setOnClickListener {
            this.dismiss()
            onDialogActionListener?.onLoginDialogClose()
        }
        bindingSheet!!.btnSignupWithEmail.setOnClickListener {
            if (b2cApp == null) {
                return@setOnClickListener;
            }
            /**
             * Runs user flow interactively.
             *
             *
             * Once the user finishes with the flow, you will also receive an access token containing the claims for the scope you passed in (see B2CConfiguration.getScopes()),
             * which you can subsequently use to obtain your resources.
             */
            /**
             * Runs user flow interactively.
             *
             *
             * Once the user finishes with the flow, you will also receive an access token containing the claims for the scope you passed in (see B2CConfiguration.getScopes()),
             * which you can subsequently use to obtain your resources.
             */
            requestLogin(BuildConfig.B2C_EMAIL)
        }
        bindingSheet!!.cvFacebook.setOnClickListener {
            if (b2cApp == null) {
                return@setOnClickListener;
            }
            requestLogin(BuildConfig.B2C_FACEBOOK)
        }
        bindingSheet!!.cvGoogle.setOnClickListener {
            if (b2cApp == null) {
                return@setOnClickListener;
            }
            requestLogin(BuildConfig.B2C_GOOGLE)
        }
        bindingSheet!!.cvApple.setOnClickListener {
            if (b2cApp == null) {
                return@setOnClickListener;
            }
            requestLogin(BuildConfig.B2C_APPLE)
        }
    }


    private fun requestLogin(policy: String) {
        if (NetworkUtils.isNetworkConnected(mActivity)) {
            showLoading()
            val parameters = AcquireTokenParameters.Builder()
                .startAuthorizationFromActivity(mActivity)
                .fromAuthority(B2CConfiguration.getAuthorityFromPolicyName(policy))
                .withScopes(B2CConfiguration.scopes)
                .withPrompt(Prompt.LOGIN)
                .withCallback(authInteractiveCallback)
                .build()

            b2cApp!!.acquireToken(parameters)
        } else {
            onDialogActionListener?.onShowMessage(mActivity.getString(R.string.no_internet))
        }
    }

    private fun showLoading() {
        onDialogActionListener?.onShowLoadingDialog(true)
    }

    private fun hideLoading() {
        onDialogActionListener?.onShowLoadingDialog(false)
    }

    private fun setData() {
        // Creates a PublicClientApplication object with res/raw/auth_config_single_account.json
        PublicClientApplication.createMultipleAccountPublicClientApplication(
            context,
            R.raw.auth_config_b2c,
            object : IPublicClientApplication.IMultipleAccountApplicationCreatedListener {
                override fun onCreated(application: IMultipleAccountPublicClientApplication) {
                    b2cApp = application
                    loadAccounts()
                }

                override fun onError(exception: MsalException) {
                    displayError(exception)
                }
            })
    }

    interface OnDialogActionListener {
        fun onLoginDialogClose()
        fun onLoginSuccess(result: IAuthenticationResult)
        fun onShowLoadingDialog(isShow: Boolean)
        fun onShowMessage(message: String)
    }

    /**
     * Load signed-in accounts, if there's any.
     */
    private fun loadAccounts() {
        if (b2cApp == null) {
            return
        }
        b2cApp!!.getAccounts(object : IPublicClientApplication.LoadAccountsCallback {
            override fun onTaskCompleted(result: List<IAccount>) {
                users = B2CUser.getB2CUsersFromAccountList(result)
                updateUI(users)
            }

            override fun onError(exception: MsalException) {
                displayError(exception)
            }
        })
    }/* Tokens expired or no session, retry with interactive *//* Exception when communicating with the STS, likely config issue *//* Exception inside MSAL, more info inside MsalError.java *//* Failed to acquireToken *//* Successfully got a token. */

    /* display result info */

    /* Reload account asynchronously to get the up-to-date list. */
    /**
     * Callback used for interactive request.
     * If succeeds we use the access token to call the Microsoft Graph.
     * Does not check cache.
     */
    private val authInteractiveCallback: AuthenticationCallback
        private get() = object : AuthenticationCallback {
            override fun onSuccess(authenticationResult: IAuthenticationResult) {
                /* Successfully got a token, use it to call a protected resource - MSGraph */
                CommonUtils.log("Successfully authenticated")
                /* display result info */displayResult(authenticationResult)

                /* Reload account asynchronously to get the up-to-date list. */loadAccounts()
            }

            override fun onError(exception: MsalException) {
                val B2C_PASSWORD_CHANGE = "AADB2C90118"
                hideLoading()
                if (exception.message!!.contains(B2C_PASSWORD_CHANGE)) {
                    CommonUtils.log(
                        """
                        The user clicks the 'Forgot Password' link in a sign-up or sign-in user flow.
                        Your application needs to handle this error code by running a specific user flow that resets the password.
                        """.trimIndent()
                    )
                    return
                }

                CommonUtils.log("Authentication failed: $exception")
                displayError(exception)
                if (exception is MsalClientException) {
                    /* Exception inside MSAL, more info inside MsalError.java */
                } else if (exception is MsalServiceException) {
                    /* Exception when communicating with the STS, likely config issue */
                }
            }

            override fun onCancel() {
                /* User canceled the authentication */
                CommonUtils.log("User cancelled login. ")
                hideLoading()
            }
        }
    //
    // Helper methods manage UI updates
    // ================================
    // displayResult() - Display the authentication result.
    // displayError() - Display the token error.
    // updateSignedInUI() - Updates UI when the user is signed in
    // updateSignedOutUI() - Updates UI when app sign out succeeds
    //
    /**
     * Display the graph response
     */
    private fun displayResult(result: IAuthenticationResult) {
//        hideLoading()
        this.dismiss()
        onDialogActionListener?.onLoginSuccess(result)
        val output = """
         Access Token :${result.accessToken}
         Scope : ${result.scope}
         Expiry : ${result.expiresOn}
         Tenant ID : ${result.tenantId}
         
         """.trimIndent()
        CommonUtils.log("output $output")
    }

    /**
     * Display the error message
     */
    private fun displayError(exception: Exception) {
        hideLoading()
        CommonUtils.log("exception>> $exception")
    }

    /**
     * Updates UI based on the obtained user list.
     */
    private fun updateUI(users: List<B2CUser>?) {
        hideLoading()
        CommonUtils.log("users>> ${Gson().toJson(users)}")
    }

}