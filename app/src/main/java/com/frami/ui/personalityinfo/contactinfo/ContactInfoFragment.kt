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
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.frami.BR
import com.frami.R
import com.frami.data.local.db.AppDatabase
import com.frami.data.model.explore.CommunityData
import com.frami.data.model.user.User
import com.frami.data.model.user.UserRequest
import com.frami.databinding.FragmentContactInfoBinding
import com.frami.ui.base.BaseFragment
import com.frami.ui.common.CommunityCodeDialogFragment
import com.frami.utils.AppConstants
import com.frami.utils.CommonUtils
import com.frami.utils.extensions.hide
import com.frami.utils.extensions.onClick
import com.frami.utils.extensions.visible

class ContactInfoFragment :
    BaseFragment<FragmentContactInfoBinding, ContactInfoFragmentViewModel>(),
    ContactInfoFragmentNavigator, CommunityCodeDialogFragment.DialogListener {

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
        init()
        getViewModel().getUserInfo(true)
    }

    private fun setUserDetails(users: User?) {
        users?.let {
            getViewModel().user.set(it)
        }
        if (isAdded) {
            val user = users ?: getViewModel().user.get()
            if (user != null) {
                if (user.workEmailAddress != null) {
                    mViewBinding!!.etEmail.setText(user.workEmailAddress.takeIf { s ->
                        !TextUtils.isEmpty(s)
                    }
                        .toString())
                    mViewBinding!!.etEmail.text?.length?.let {
                        mViewBinding!!.etEmail.setSelection(
                            it
                        )
                    }
                }
//                if (user.workEmailAddress != null) {
//                    mViewBinding!!.etWorkEmail.setText(user.workEmailAddress.takeIf { s ->
//                        !TextUtils.isEmpty(
//                            s
//                        )
//                    }
//                        .toString())
//                    mViewBinding!!.etWorkEmail.text?.length?.let {
//                        mViewBinding!!.etWorkEmail.setSelection(
//                            it
//                        )
//                    }
//                }
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
                getViewModel().isWorkEmailEdited.set(getViewModel().user.get()?.workEmailAddress != emailId)
                getViewModel().isValidWorkEmail.set(
                    !TextUtils.isEmpty(emailId) && CommonUtils.isValidEmail(
                        emailId
                    )
                )
            }
        })
//        mViewBinding!!.etWorkEmail.addTextChangedListener(object : TextWatcher {
//            override fun beforeTextChanged(
//                charSequence: CharSequence,
//                i: Int,
//                i1: Int,
//                i2: Int
//            ) {
//            }
//
//            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
//            override fun afterTextChanged(editable: Editable) {
//                val emailId = editable.toString().trim { it <= ' ' }
//                getViewModel().isWorkEmailEdited.set(getViewModel().user.get()?.workEmailAddress != emailId)
//                getViewModel().isValidWorkEmail.set(
//                    !TextUtils.isEmpty(emailId) && CommonUtils.isValidEmail(
//                        emailId
//                    )
//                )
//            }
//        })
    }

    private fun toolbar() {
        if (getViewModel().isFromEdit.get()) {
            mViewBinding?.tvTitle?.visible()
            mViewBinding?.tvTitle?.text = getString(R.string.employer)
            mViewBinding?.toolBarLayout?.tvTitle?.hide()
        } else {
            mViewBinding?.toolBarLayout?.tvTitle?.text = getString(R.string.contact_info)
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
//        requireActivity().finish()
        mNavController?.navigateUp()
    }

    private fun clickListener() {
//        mViewBinding!!.tvIfApplicable.setOnClickListener {
//            showWorkEmailInstructionDialog()
//        }
        mViewBinding?.btnConnectCode?.onClick {
            showAddEmployerDialog()
        }
        mViewBinding!!.btnNext.setOnClickListener {
            validateDataAndCallAPI()
        }
//        mViewBinding!!.tvVerifyEmail.setOnClickListener {
//            val emailId = mViewBinding!!.etEmail.text.toString()
//            if ((getViewModel().user.get()?.isWorkEmailVerified == false || getViewModel().isWorkEmailEdited.get()) && emailId.isNotEmpty()) {
//                getViewModel().verifyEmail(true, emailId)
//            }
//        }
//        mViewBinding!!.tvVerifyWorkEmail.setOnClickListener {
//            val workEmailId = mViewBinding!!.etWorkEmail.text.toString()
//            if ((getViewModel().user.get()?.isWorkEmailVerified == false || getViewModel().isWorkEmailEdited.get()) && workEmailId.isNotEmpty()) {
//                getViewModel().verifyEmail(true, workEmailId)
//            }
//        }
    }
    private fun showAddEmployerDialog() {
        val newFragment: DialogFragment = CommunityCodeDialogFragment(this)
        newFragment.show(requireActivity().supportFragmentManager, "addcode")
    }

    private fun validateDataAndCallAPI() {
        val email = mViewBinding!!.etEmail.text?.toString()?.trim()!!
        if (TextUtils.isEmpty(email)) {
            showMessage("Please enter email")
        } else if (!CommonUtils.isValidEmail(email)) {
            showMessage("Please enter valid email")
        } else {
            getViewModel().updateWorkMail(email)
        }
    }

    override fun userInfoFetchSuccess(user: User?) {
        setUserDetails(user)
        if (user?.workEmailAddress.isNullOrEmpty()){
            mViewBinding?.btnNext?.visible()
            mViewBinding?.btnResendCode?.hide()
        }else if (!user?.workEmailAddress.isNullOrEmpty()){
            if (user?.isWorkEmailVerified == true){
                mViewBinding?.btnNext?.hide()
                mViewBinding?.btnResendCode?.hide()
            }else{
                mViewBinding?.btnNext?.hide()
                mViewBinding?.btnResendCode?.visible()
            }
        }
    }

    override fun verificationEmailSentSuccess(workEmail: Boolean, emailId: String) {
        val userDAO = AppDatabase.db.userDao()
        if (workEmail) {
            userDAO.updateWorkEmail(getViewModel().getUserId(), emailId)
        } else {
            userDAO.updateEmail(getViewModel().getUserId(), emailId)
        }
        getViewModel().user.set(userDAO.getByUserId(getViewModel().getUserId()))
    }

    override fun onCommunityCodeConnect(code: String) {
        getViewModel().joinCommunityByCodeAPI(code)
    }
    override fun communityJoinByCode(data: CommunityData?) {
        data?.let {
            mViewBinding?.data = data
            mViewBinding?.nsvEmployer?.visible()
            mViewBinding?.nsvMain?.hide()
//            navigateToCommunityDetails(it.communityId)
        }
    }
}