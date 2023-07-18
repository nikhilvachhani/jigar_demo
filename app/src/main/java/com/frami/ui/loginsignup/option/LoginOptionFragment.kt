package com.frami.ui.loginsignup.option

import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult
import androidx.fragment.app.viewModels
import com.frami.BR
import com.frami.BuildConfig
import com.frami.R
import com.frami.data.model.user.User
import com.frami.databinding.FragmentLoginOptionBinding
import com.frami.ui.base.BaseFragment
import com.frami.ui.dialog.LoginDialog
import com.frami.ui.dialog.SignupDialog
import com.microsoft.identity.client.IAuthenticationResult


class LoginOptionFragment :
    BaseFragment<FragmentLoginOptionBinding, LoginOptionFragmentViewModel>(),
    LoginOptionFragmentNavigator, View.OnClickListener, LoginDialog.OnDialogActionListener,
    SignupDialog.OnDialogActionListener {

    private val viewModelInstanceOption: LoginOptionFragmentViewModel by viewModels {
        viewModeFactory
    }
    private var mBinding: FragmentLoginOptionBinding? = null

    override fun getBindingVariable(): Int = BR.loViewModel

    override fun getLayoutId(): Int = R.layout.fragment_login_option

    override fun getViewModel(): LoginOptionFragmentViewModel = viewModelInstanceOption

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mBinding = getViewDataBinding()
        viewModelInstanceOption.setNavigator(this)
        if (BuildConfig.DEBUG) {
//            mBinding?.edtMobile!!.setText("9924274295")
        }
        handleBackPress()
        findViewById()
        clickListener()
    }

    private fun handleBackPress() {
        val callback: OnBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                onBack()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)
    }

    private fun findViewById() {

    }


    private fun clickListener() {
        mBinding!!.btnLogin.setOnClickListener {
            openLoginDialog()
        }
        mBinding!!.btnSignup.setOnClickListener {
            openSignupDialog()
        }
        mBinding!!.btnGuestLogin.setOnClickListener {
            navigateToDashboard(null)
        }
    }


    override fun onBack() {
        requireActivity().finish()
    }


    override fun onClick(view: View) {
        hideKeyboard()
        when (view) {
        }
    }

    private fun openLoginDialog() {
        val loginDialog = LoginDialog(requireActivity(), this)
        loginDialog.show()
    }

    override fun onLoginSuccess(result: IAuthenticationResult) {
        getViewModel().setAccessToken(result.accessToken)
        getViewModel().setTokenExpiresOn(result.expiresOn.time)
        getViewModel().validateUser()
    }

    override fun onLoginDialogClose() {

    }

    override fun onShowLoadingDialog(isShow: Boolean) {
        if (isShow) {
            showLoading()
        } else {
            hideLoading()
        }
    }

    override fun onShowMessage(message: String) {
        showMessage(message)
    }

    private fun openSignupDialog() {
        val signupDialog = SignupDialog(requireActivity(), this)
        signupDialog.show()
    }

    override fun validateSuccess(user: User?) {
        if (user != null) {
            authFlow(user, false, wearableDeviceActivityResultLauncher, null)
        } else {
            navigateToLogin()
        }
    }

    // You can do the assignment inside onAttach or onCreate, i.e, before the activity is displayed
    var wearableDeviceActivityResultLauncher = registerForActivityResult(
        StartActivityForResult(),
        ActivityResultCallback<ActivityResult> { result ->
            getViewModel().getUserLiveData().observe(
                viewLifecycleOwner,
                androidx.lifecycle.Observer { user ->
                    if (user != null) {
                        authFlow(user, false, null, null)
                    }
                })
        })
}