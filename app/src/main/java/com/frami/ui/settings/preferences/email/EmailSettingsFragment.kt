package com.frami.ui.settings.preferences.email

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.viewModels
import com.frami.BR
import com.frami.R
import com.frami.data.local.db.AppDatabase
import com.frami.data.model.settings.EmailSettingRequest
import com.frami.data.model.settings.pushnotificationmenu.mainmenu.PushNotificationMenuData
import com.frami.data.model.user.User
import com.frami.databinding.FragmentEmailSettingsBinding
import com.frami.ui.base.BaseFragment
import com.frami.utils.AppConstants
import com.frami.utils.CommonUtils
import com.frami.utils.extensions.hide
import com.frami.utils.extensions.onClick
import com.frami.utils.extensions.visible

class EmailSettingsFragment :
    BaseFragment<FragmentEmailSettingsBinding, EmailSettingsFragmentViewModel>(),
    EmailSettingsFragmentNavigator {

    private val viewModelInstance: EmailSettingsFragmentViewModel by viewModels {
        viewModeFactory
    }
    private var mViewBinding: FragmentEmailSettingsBinding? = null
    override fun getBindingVariable(): Int = BR.viewModel

    override fun getLayoutId(): Int = R.layout.fragment_email_settings

    override fun getViewModel(): EmailSettingsFragmentViewModel = viewModelInstance
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            if (requireArguments().containsKey(AppConstants.EXTRAS.NOTIFICATION_PREFERENCE_DATA))
                getViewModel().pushNotificationMenuData.set(
                    requireArguments().getSerializable(
                        AppConstants.EXTRAS.NOTIFICATION_PREFERENCE_DATA
                    ) as PushNotificationMenuData
                )
        }
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setStatusBarColor(R.color.lightBg)
        mViewBinding = getViewDataBinding()
        viewModelInstance.setNavigator(this)

        toolbar()
        handleBackPress()
        clickListener()
        init()
        getViewModel().getUserInfo(true)
    }
    override fun userInfoFetchSuccess(user: User?) {
        setUserDetails(user)
    }

    private fun setUserDetails(users: User?) {
        users?.let {
            getViewModel().user.set(it)
        }
        if (isAdded) {
            val user = users ?: getViewModel().user.get()
            if (user != null) {
                mViewBinding?.chInfo?.isChecked = user.isSendNotification
                if (user.emailAddress != null) {
                    mViewBinding!!.etEmail.setText(user.emailAddress.takeIf { s ->
                        !TextUtils.isEmpty(
                            s
                        )
                    }
                        .toString())
                    mViewBinding!!.etEmail.text?.length?.let {
                        mViewBinding!!.etEmail.setSelection(
                            it
                        )
                    }
                }
            }
        }
    }

    private fun init() {
        mViewBinding!!.etEmail.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(
                charSequence: CharSequence,
                i: Int,
                i1: Int,
                i2: Int
            ) {
            }

            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun afterTextChanged(editable: Editable) {
                val emailId = editable.toString().trim { it <= ' ' }
                getViewModel().isEmailEdited.set(getViewModel().user.get()?.emailAddress != emailId)
                getViewModel().isValidEmail.set(
                    !TextUtils.isEmpty(emailId) && CommonUtils.isValidEmail(
                        emailId
                    )
                )
            }
        })
    }

    private fun toolbar() {
        mViewBinding?.toolBarLayout?.tvTitle?.hide()
        mViewBinding?.toolBarLayout?.tvSave?.visible()
        mViewBinding?.toolBarLayout?.tvSave?.setOnClickListener {
            if (getViewModel().isValidEmail.get()){
                validateDataAndCallAPI()
            } else{
                showMessage("Please enter valid email")
            }

        }

        mViewBinding?.toolBarLayout?.cvBack?.visible()
        mViewBinding?.toolBarLayout?.cvBack?.setImageResource(R.drawable.ic_back_new)
        mViewBinding?.toolBarLayout?.cvBack?.onClick { onBack() }
        mViewBinding?.toolBarLayout?.toolBar?.setNavigationOnClickListener { v -> onBack() }
    }

    private fun handleBackPress() {
        val callback: OnBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                onBack()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)
    }

    override fun onBack() {
        mNavController?.navigateUp()
    }

    private fun clickListener() {

//        mViewBinding!!.tvVerifyEmail.setOnClickListener {
//            val emailId = mViewBinding!!.etEmail.text.toString()
//            if ((getViewModel().user.get()?.isEmailVerified == false || getViewModel().isEmailEdited.get()) && emailId.isNotEmpty()) {
//                getViewModel().verifyEmail(false, emailId)
//            }
//        }
    }

    private fun validateDataAndCallAPI() {
        val email = mViewBinding!!.etEmail.text?.toString()?.trim()!!

        if (TextUtils.isEmpty(email)) {
            showMessage("Please enter email")
        } else if (!CommonUtils.isValidEmail(email)) {
            showMessage("Please enter valid email")
        } else {
            val request  = EmailSettingRequest(email, mViewBinding?.chInfo?.isChecked == true)
            getViewModel().updateEMailSetting(request)
        }
    }

    override fun updateUserSuccess(user: User?) {
        getViewModel().user.set(user)
        onBack()
    }

    override fun verificationEmailSentSuccess(workEmail: Boolean, emailId: String) {
        val userDAO = AppDatabase.db.userDao()
        userDAO.updateEmail(getViewModel().getUserId(), emailId)
        getViewModel().user.set(userDAO.getByUserId(getViewModel().getUserId()))
    }
}