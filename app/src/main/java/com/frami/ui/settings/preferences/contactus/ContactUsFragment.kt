package com.frami.ui.settings.preferences.contactus

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.frami.BR
import com.frami.R
import com.frami.data.local.db.AppDatabase
import com.frami.data.model.settings.help.ContactUsRequest
import com.frami.databinding.FragmentContactUsBinding
import com.frami.ui.base.BaseFragment
import com.frami.utils.CommonUtils
import com.frami.utils.extensions.hide
import com.frami.utils.extensions.visible

class ContactUsFragment :
    BaseFragment<FragmentContactUsBinding, ContactUsFragmentViewModel>(),
    ContactUsFragmentNavigator {

    private val viewModelInstance: ContactUsFragmentViewModel by viewModels {
        viewModeFactory
    }
    private var mViewBinding: FragmentContactUsBinding? = null
    override fun getBindingVariable(): Int = BR.viewModel

    override fun getLayoutId(): Int = R.layout.fragment_contact_us

    override fun getViewModel(): ContactUsFragmentViewModel = viewModelInstance

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mViewBinding = getViewDataBinding()
        viewModelInstance.setNavigator(this)

        toolbar()
        handleBackPress()
        clickListener()
        init()
    }

    fun init() {
        activity?.let {
            Glide.with(it).asGif().load(R.drawable.successfully).into(mViewBinding!!.ivSuccess)
        };
    }

    private fun toolbar() {
        mViewBinding!!.toolBarLayout.tvTitle.hide()
        mViewBinding!!.toolBarLayout.toolBar.setNavigationOnClickListener { v -> onBack() }
        mViewBinding!!.toolBarLayout.cvBack.visible()
        mViewBinding!!.toolBarLayout.cvBack.setOnClickListener { onBack() }
    }

    private fun handleBackPress() {
        val callback: OnBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (!getViewModel().formSubmittedSuccess.get()) {
                    onBack()
                }
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)
    }

    override fun onBack() {
        hideKeyboard()
        mNavController!!.navigateUp()
    }

    private fun clickListener() {
        mViewBinding!!.btnSubmit.setOnClickListener { validateDataAndCallAPI() }
    }

    private fun validateDataAndCallAPI() {
        hideKeyboard()
        val name = mViewBinding!!.etUserName.text.toString()
        val email = mViewBinding!!.etEmail.text.toString()
        val comment = mViewBinding!!.etComment.text.toString()
        val user = AppDatabase.db.userDao().getById()
        if (name.isEmpty()) {
            showMessage("Please enter name")
        } else if (email.isEmpty()) {
            showMessage("Please enter email address")
        } else if (!CommonUtils.isValidEmail(email)) {
            showMessage("Please enter valid email address")
        } else if (comment.isEmpty()) {
            showMessage("Please enter your comment")
        } else {
            val contactUsRequest = ContactUsRequest(
                userId = user?.userId ?: "",
                userName = name,
                emailAddress = email,
                comment = comment
            )
            getViewModel().contactUsAPI(contactUsRequest)
        }
    }

    override fun contactUsSubmittedSuccess(message: String) {
        getViewModel().formSubmittedSuccess.set(true)
//        showMessage(message)
        Handler(Looper.getMainLooper()).postDelayed({
            onBack()
        }, 5000)
    }
}