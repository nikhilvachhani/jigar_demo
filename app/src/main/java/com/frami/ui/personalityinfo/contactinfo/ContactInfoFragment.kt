package com.frami.ui.personalityinfo.contactinfo

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
import com.frami.ui.base.BaseFragment
import com.frami.utils.AppConstants
import com.frami.utils.CommonUtils
import com.frami.utils.extensions.hide
import com.frami.utils.extensions.visible

class ContactInfoFragment :
    BaseFragment<FragmentContactInfoBinding, ContactInfoFragmentViewModel>(),
    ContactInfoFragmentNavigator {

    private val viewModelInstance: ContactInfoFragmentViewModel by viewModels {
        viewModeFactory
    }
    private var mViewBinding: FragmentContactInfoBinding? = null
    override fun getBindingVariable(): Int = BR.viewModel

    override fun getLayoutId(): Int = R.layout.fragment_contact_info

    override fun getViewModel(): ContactInfoFragmentViewModel = viewModelInstance

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
        setPrivacyPolicyText()
        init()
        Handler(Looper.getMainLooper()).postDelayed({
            getViewModel().getUserInfo(true)
        }, 400)
    }

    private fun setUserDetails(users: User?) {
        users?.let {
            getViewModel().user.set(it)
        }
        if (isAdded) {
            val user = users ?: getViewModel().user.get()
            if (user != null) {
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
                if (user.workEmailAddress != null) {
                    mViewBinding!!.etWorkEmail.setText(user.workEmailAddress.takeIf { s ->
                        !TextUtils.isEmpty(
                            s
                        )
                    }
                        .toString())
                    mViewBinding!!.etWorkEmail.text?.length?.let {
                        mViewBinding!!.etWorkEmail.setSelection(
                            it
                        )
                    }
                }
                mViewBinding!!.cbPolicy.isChecked = user.isPrivacyPolicyAgreed
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
        mViewBinding!!.etWorkEmail.addTextChangedListener(object : TextWatcher {
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
                getViewModel().isWorkEmailEdited.set(getViewModel().user.get()?.workEmailAddress != emailId)
                getViewModel().isValidWorkEmail.set(
                    !TextUtils.isEmpty(emailId) && CommonUtils.isValidEmail(
                        emailId
                    )
                )
            }
        })
    }

    private fun toolbar() {
        if (getViewModel().isFromEdit.get()) {
            mViewBinding!!.toolBarLayout.tvTitle.hide()
            mViewBinding!!.toolBarLayout.cvDone.visible()
            mViewBinding!!.toolBarLayout.cvDone.setOnClickListener { validateDataAndCallAPI() }
        } else {
            mViewBinding!!.toolBarLayout.tvTitle.text = getString(R.string.contact_info)
        }
        mViewBinding!!.toolBarLayout.cvBack.visible()
        mViewBinding!!.toolBarLayout.cvBack.setOnClickListener { onBack() }
        mViewBinding!!.toolBarLayout.toolBar.setNavigationOnClickListener { v -> onBack() }
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
//        requireActivity().finish()
        mNavController!!.navigateUp()
    }

    private fun clickListener() {
        mViewBinding!!.tvFramiWould.setOnClickListener {
            mViewBinding!!.cbFramiWould.isChecked = !mViewBinding!!.cbFramiWould.isChecked
        }
        mViewBinding!!.tvIfApplicable.setOnClickListener {
            showWorkEmailInstructionDialog()
        }
        mViewBinding!!.btnNext.setOnClickListener {
            validateDataAndCallAPI()
        }
        mViewBinding!!.tvVerifyEmail.setOnClickListener {
            val emailId = mViewBinding!!.etEmail.text.toString()
            if ((getViewModel().user.get()?.isEmailVerified == false || getViewModel().isEmailEdited.get()) && emailId.isNotEmpty()) {
                getViewModel().verifyEmail(false, emailId)
            }
        }
        mViewBinding!!.tvVerifyWorkEmail.setOnClickListener {
            val workEmailId = mViewBinding!!.etWorkEmail.text.toString()
            if ((getViewModel().user.get()?.isWorkEmailVerified == false || getViewModel().isWorkEmailEdited.get()) && workEmailId.isNotEmpty()) {
                getViewModel().verifyEmail(true, workEmailId)
            }
        }
    }

    private fun setPrivacyPolicyText() {
        val isFromEdit = !getViewModel().isFromEdit.get()
        val spannableString =
            SpannableString(getString(if (isFromEdit) R.string.you_agree_t else R.string.you_agree_while_login))
        val privacy: ClickableSpan = object : ClickableSpan() {
            override fun onClick(textView: View) {
                navigateToPrivacyPolicy()
            }

            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                ds.isUnderlineText = false
            }
        }
        val tnc: ClickableSpan = object : ClickableSpan() {
            override fun onClick(textView: View) {
                navigateToTNC()
            }

            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                ds.isUnderlineText = false
            }
        }

        val checkUncheck: ClickableSpan = object : ClickableSpan() {
            override fun onClick(textView: View) {
                mViewBinding!!.cbPolicy.isChecked = !mViewBinding!!.cbPolicy.isChecked
            }

            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                ds.isUnderlineText = false
            }
        }
        try {
            spannableString.setSpan(
                checkUncheck,
                0,
                if (isFromEdit) 50 else 17,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            spannableString.setSpan(
                tnc,
                if (isFromEdit) 50 else 17,
                if (isFromEdit) 65 else 33,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            if (!isFromEdit) {
                spannableString.setSpan(checkUncheck, 36, 44, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
            }
            spannableString.setSpan(
                privacy,
                if (isFromEdit) 67 else 44,
                if (isFromEdit) 79 else 58,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            spannableString.setSpan(
                ForegroundColorSpan(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.colorSubHeaderText
                    )
                ), 0, if (isFromEdit) 50 else 17, 0
            )
            spannableString.setSpan(
                ForegroundColorSpan(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.lightPink
                    )
                ), if (isFromEdit) 50 else 17, if (isFromEdit) 65 else 33, 0
            )
            if (!isFromEdit) {
                spannableString.setSpan(
                    ForegroundColorSpan(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.colorSubHeaderText
                        )
                    ), 36, 44, 0
                )
            }
            spannableString.setSpan(
                ForegroundColorSpan(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.lightPink
                    )
                ), if (isFromEdit) 65 else 44, if (isFromEdit) 79 else 58, 0
            )
        } catch (e: Exception) {
            e.printStackTrace()
        }
        mViewBinding!!.tvPolicy.movementMethod = LinkMovementMethod.getInstance()
        mViewBinding!!.tvPolicy.setText(spannableString, TextView.BufferType.SPANNABLE)
    }

    private fun showWorkEmailInstructionDialog() {
        val alertDialog =
            AlertDialog.Builder(
                requireContext(), R.style.AlertDialogStyle
            )
                .setCancelable(true)
                .create()
        alertDialog.setMessage(
            resources.getString(R.string.work_email_instruction_full)
        )
        alertDialog.setButton(
            AlertDialog.BUTTON_POSITIVE, resources.getString(R.string.ok)
        ) { dialog, which ->
            dialog.dismiss()
        }
        alertDialog.show()
    }

    private fun validateDataAndCallAPI() {
        val email = mViewBinding!!.etEmail.text?.toString()?.trim()!!
        val workEmail = mViewBinding!!.etWorkEmail.text?.toString()?.trim()
        val isPolicyAccept = mViewBinding!!.cbPolicy.isChecked
        if (TextUtils.isEmpty(email)) {
            showMessage("Please enter email")
        } else if (!CommonUtils.isValidEmail(email)) {
            showMessage("Please enter valid email")
        } else if (!isPolicyAccept) {
            showMessage("Please accept terms and condition")
        } else {
            val user = getViewModel().user.get()
            val userRequest = user?.let {
                UserRequest(
                    userName = it.userName,
                    firstName = it.firstName,
                    lastName = it.lastName,
//                ProfilePhoto = File(getViewModel().selectedProfilePhoto.get()?.path),
                    gender = if (it.gender == null) AppConstants.GENDER.MALE.type else it.gender!!,
//                    nationality = it.nationality,
                    NationalityCode = if (it.nationality == null) "" else it.nationality?.code!!,
                    NationalityName = if (it.nationality == null) "" else it.nationality?.name!!,
                    UserId = it.userId,
                    identityProvider = if (it.identityProvider == null) "" else it.identityProvider!!,
                    b2CFlow = if (it.b2CFlow == null) "" else it.b2CFlow!!,
                    profilePhotoUrl = it.profilePhotoUrl,
                    birthDate = if (it.birthDate == null) "" else it.birthDate!!,
                    emailAddress = email,
                    workEmailAddress = if (!TextUtils.isEmpty(workEmail)) workEmail!! else "",
                    isPersonalInfoCompleted = user.isPersonalInfoCompleted,
                    isContactInfoCompleted = true,
                    isPrivacyPolicyAgreed = isPolicyAccept,
                    isSendNotification = user.isSendNotification,
                    isDeviceConnected = user.isDeviceConnected,
                    isWelcomeEmailSent = user.isWelcomeEmailSent,
                    isVerificationEmailSent = user.isVerificationEmailSent,
                    isEmailVerified = user.isEmailVerified,
                    isWorkVerificationEmailSent = user.isWorkVerificationEmailSent,
//                    userDevices = if (user.userDevices!=null) user.userDevices!! else ArrayList<UserDevices>()
                )
            }
            userRequest?.let {
                getViewModel().updateUser(
                    it
                )
            }
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
        setUserDetails(user)
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