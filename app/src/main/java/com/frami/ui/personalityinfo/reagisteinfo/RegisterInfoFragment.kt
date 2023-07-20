package com.frami.ui.personalityinfo.reagisteinfo

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.*
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.view.View
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import com.frami.BR
import com.frami.R
import com.frami.data.local.db.AppDatabase
import com.frami.data.model.user.User
import com.frami.data.model.user.UserRequest
import com.frami.databinding.FragmentContactInfoBinding
import com.frami.databinding.FragmentRegisterInfoBinding
import com.frami.ui.base.BaseFragment
import com.frami.utils.AppConstants
import com.frami.utils.CommonUtils
import com.frami.utils.extensions.hide
import com.frami.utils.extensions.onClick
import com.frami.utils.extensions.visible

class RegisterInfoFragment :
    BaseFragment<FragmentRegisterInfoBinding, RegisterInfoFragmentViewModel>(),
    RegisterInfoFragmentNavigator {

    private val viewModelInstance: RegisterInfoFragmentViewModel by viewModels {
        viewModeFactory
    }
    private var mViewBinding: FragmentRegisterInfoBinding? = null
    override fun getBindingVariable(): Int = BR.viewModel

    override fun getLayoutId(): Int = R.layout.fragment_register_info

    override fun getViewModel(): RegisterInfoFragmentViewModel = viewModelInstance

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            if (arguments?.containsKey(AppConstants.FROM.EDIT) == true) {
                getViewModel().isFromEdit.set(arguments?.getBoolean(AppConstants.FROM.EDIT)!!)
            }
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
//        Handler(Looper.getMainLooper()).postDelayed({
//            getViewModel().getUserInfo(true)
//        }, 400)
    }

    private fun init() {

    }

    private fun toolbar() {
        mViewBinding?.toolBarLayout?.tvTitle?.hide()
        mViewBinding?.toolBarLayout?.cvClose?.visible()
        mViewBinding?.toolBarLayout?.cvClose?.onClick { onBack() }
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
        requireActivity().finish()
    }

    private fun clickListener() {
        mViewBinding?.tvPolicy?.onClick {
            mViewBinding?.cbPolicy?.isChecked = mViewBinding?.cbPolicy?.isChecked != true
        }
        mViewBinding?.tvFramiWould?.onClick {
            mViewBinding?.cbFramiWould?.isChecked = mViewBinding?.cbFramiWould?.isChecked != true
        }
        mViewBinding?.btnNext?.onClick {
            validateDataAndCallAPI()
        }
        mViewBinding?.linearReadTC?.onClick { navigateToTNC() }
        mViewBinding?.linearReadPP?.onClick { navigateToPrivacyPolicy() }
    }


    private fun validateDataAndCallAPI() {
        val isPolicyAccept = mViewBinding?.cbPolicy?.isChecked
        if (isPolicyAccept != true) {
            showMessage(getString(R.string.please_accept_terms_and_condition))
        } else {
            mNavController?.navigate(R.id.toPersonalInfoFragment)
//            val user = getViewModel().user.get()
//            val userRequest = user?.let {
//                UserRequest(
//                    userName = it.userName,
//                    firstName = it.firstName,
//                    lastName = it.lastName,
////                ProfilePhoto = File(getViewModel().selectedProfilePhoto.get()?.path),
//                    gender = if (it.gender == null) AppConstants.GENDER.MALE.type else it.gender!!,
////                    nationality = it.nationality,
//                    NationalityCode = if (it.nationality == null) "" else it.nationality?.code!!,
//                    NationalityName = if (it.nationality == null) "" else it.nationality?.name!!,
//                    UserId = it.userId,
//                    identityProvider = if (it.identityProvider == null) "" else it.identityProvider!!,
//                    b2CFlow = if (it.b2CFlow == null) "" else it.b2CFlow!!,
//                    profilePhotoUrl = it.profilePhotoUrl,
//                    birthDate = if (it.birthDate == null) "" else it.birthDate!!,
//                    emailAddress = email,
//                    workEmailAddress = if (!TextUtils.isEmpty(workEmail)) workEmail!! else "",
//                    isPersonalInfoCompleted = user.isPersonalInfoCompleted,
//                    isContactInfoCompleted = true,
//                    isPrivacyPolicyAgreed = isPolicyAccept,
//                    isSendNotification = user.isSendNotification,
//                    isDeviceConnected = user.isDeviceConnected,
//                    isWelcomeEmailSent = user.isWelcomeEmailSent,
//                    isVerificationEmailSent = user.isVerificationEmailSent,
//                    isEmailVerified = user.isEmailVerified,
//                    isWorkVerificationEmailSent = user.isWorkVerificationEmailSent,
////                    userDevices = if (user.userDevices!=null) user.userDevices!! else ArrayList<UserDevices>()
//                )
//            }
//            userRequest?.let {
//                getViewModel().updateUser(
//                    it
//                )
//            }
        }
    }

    override fun updateUserSuccess(user: User?) {
        if (getViewModel().isFromEdit.get()) {
            onBack()
        } else {
            if (user != null) {
                authFlow(user, false, wearableDeviceActivityResultLauncher, null)
            } else {
                navigateToLogin()
            }
        }
    }

    override fun userInfoFetchSuccess(user: User?) {
//        setUserDetails(user)
        if (getViewModel().isEnableNavigationToForward.get()) {
            if (user != null) {
                authFlow(user, false, null, null)
            }
        }
    }

    // You can do the assignment inside onAttach or onCreate, i.e, before the activity is displayed
    var wearableDeviceActivityResultLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult(),
        ActivityResultCallback<ActivityResult> { result ->
            getViewModel().isEnableNavigationToForward.set(true)
            getViewModel().getUserInfo(true)
//            getViewModel().getUserLiveData().observe(
//                viewLifecycleOwner,
//                Observer { user ->
//                    if (user != null) {
//                        authFlow(user, false, null)
//                    }
//                })
        })

    override fun verificationEmailSentSuccess(workEmail: Boolean, emailId: String) {
        val userDAO = AppDatabase.db.userDao()
        if (workEmail) {
            userDAO.updateWorkEmail(getViewModel().getUserId(), emailId)
        } else {
            userDAO.updateEmail(getViewModel().getUserId(), emailId)
        }
        getViewModel().user.set(userDAO.getByUserId(getViewModel().getUserId()))
    }
}