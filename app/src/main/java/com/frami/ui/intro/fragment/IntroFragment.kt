package com.frami.ui.intro.fragment

import android.os.Bundle
import android.text.SpannableString
import android.text.style.UnderlineSpan
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.viewpager2.widget.ViewPager2
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import com.frami.BR
import com.frami.R
import com.frami.data.model.user.User
import com.frami.databinding.FragmentIntroBinding
import com.frami.ui.base.BaseFragment
import com.frami.ui.dialog.LoginDialog
import com.frami.ui.dialog.SignupDialog
import com.google.android.material.tabs.TabLayoutMediator
import com.microsoft.identity.client.IAuthenticationResult

class IntroFragment : BaseFragment<FragmentIntroBinding, IntroFragmentViewModel>(),
    IntroFragmentNavigator, SignupDialog.OnDialogActionListener,
    LoginDialog.OnDialogActionListener {

    private var mViewBinding: FragmentIntroBinding? = null
    override fun getBindingVariable(): Int = BR.introFragmentViewModel
    override fun getLayoutId(): Int = R.layout.fragment_intro

    private val viewModelInstance: IntroFragmentViewModel by viewModels {
        viewModeFactory
    }

    override fun getViewModel(): IntroFragmentViewModel = viewModelInstance

    private var introPagerFragmentAdapter: IntroPagerFragmentAdapter? = null


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mViewBinding = getViewDataBinding()
        viewModelInstance.setNavigator(this)

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
        introPagerFragmentAdapter = IntroPagerFragmentAdapter(
            childFragmentManager,
            lifecycle
        )

        // set Orientation in your ViewPager2
        mViewBinding!!.pagerIntro.orientation = ViewPager2.ORIENTATION_HORIZONTAL
        mViewBinding!!.pagerIntro.offscreenPageLimit = 1
        mViewBinding!!.pagerIntro.adapter = introPagerFragmentAdapter


        val tabLayoutMediator = TabLayoutMediator(
            mViewBinding!!.tabLayout, mViewBinding!!.pagerIntro
        ) { tab, position -> }
        tabLayoutMediator.attach()

        introPagerFragmentAdapter!!.setIntroModels(getViewModel().getInfoModels(requireActivity()))

        mViewBinding!!.pagerIntro.registerOnPageChangeCallback(object : OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                mViewBinding!!.ivIntro.setImageResource(getViewModel().getInfoModels(requireActivity())[position].image)
                val isLastPOS =
                    (position == (getViewModel().getInfoModels(requireActivity()).size - 1))
                getViewModel().isLastPosition.set(isLastPOS)
//                if (isLastPOS) {
//                    mViewBinding!!.tvSkip.performClick()
//                }
                super.onPageSelected(position)
            }

            override fun onPageScrollStateChanged(state: Int) {
                log("onPageScrollStateChanged STATE $state")
                if (getViewModel().isLastPosition.get() && state == 1) {
                    //TODO
//                    mViewBinding!!.tvSkip.performClick()
                }
            }
        })
        for (i in 0 until mViewBinding?.tabLayout!!.tabCount) {
            val tab = (mViewBinding?.tabLayout!!.getChildAt(0) as ViewGroup).getChildAt(i)
            val p = tab.layoutParams as ViewGroup.MarginLayoutParams
            p.setMargins(6, 0, 6, 0)
            tab.requestLayout()
        }

        val content = SpannableString(requireActivity().getString(R.string.log_in))
        content.setSpan(UnderlineSpan(), 0, content.length, 0)
        mViewBinding?.tvLogin?.text = content
    }

    private fun clickListener() {
        mViewBinding!!.tvSkip.setOnClickListener {
            mNavController?.navigate(R.id.toLoginSignupActivity)
            activity?.finish() //Added because popUpToInclusive="true" not working properly
        }
        mViewBinding!!.btnSignUp.setOnClickListener {
            openSignupDialog()
        }
        mViewBinding!!.tvLogin.setOnClickListener {
            openLoginDialog()
        }
    }

    override fun onBack() {
//        mNavController!!.navigateUp()

        activity?.finish()
    }

    private fun openSignupDialog() {
        val signupDialog = SignupDialog(requireActivity(), this)
        signupDialog.show()
    }

    private fun openLoginDialog() {
        val loginDialog = LoginDialog(requireActivity(), this)
        loginDialog.show()
    }

    override fun onLoginSuccess(result: IAuthenticationResult) {
        getViewModel().saveIsAppTutorialDone(true)
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

    override fun validateSuccess(user: User?) {
        if (user != null) {
            authFlow(user, false, wearableDeviceActivityResultLauncher, null)
        } else {
            navigateToLogin()
        }
    }

    // You can do the assignment inside onAttach or onCreate, i.e, before the activity is displayed
    var wearableDeviceActivityResultLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult(),
        ActivityResultCallback<ActivityResult> { result ->
            getViewModel().getUserLiveData().observe(
                viewLifecycleOwner,
                Observer { user ->
                    if (user != null) {
                        authFlow(user, false, null, null)
                    }
                })
        })
}