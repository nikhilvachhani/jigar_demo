package com.frami.ui.settings

import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.viewModels
import com.frami.BR
import com.frami.R
import com.frami.data.model.common.IdNameData
import com.frami.data.model.profile.UserProfileData
import com.frami.databinding.FragmentSettingsBinding
import com.frami.ui.base.BaseFragment
import com.frami.ui.dashboard.rewards.details.MenuListAdapter
import com.frami.utils.AppConstants
import com.frami.utils.extensions.hide
import com.frami.utils.extensions.onClick
import com.frami.utils.extensions.visible


class SettingsFragment :
    BaseFragment<FragmentSettingsBinding, SettingsFragmentViewModel>(),
    SettingsFragmentNavigator, MenuListAdapter.OnItemClickListener {

    private val viewModelInstance: SettingsFragmentViewModel by viewModels {
        viewModeFactory
    }
    private var mViewBinding: FragmentSettingsBinding? = null
    override fun getBindingVariable(): Int = BR.viewModel

    override fun getLayoutId(): Int = R.layout.fragment_settings

    override fun getViewModel(): SettingsFragmentViewModel = viewModelInstance

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setStatusBarColor(R.color.lightBg)
        mViewBinding = getViewDataBinding()
        viewModelInstance.setNavigator(this)

        toolbar()
        handleBackPress()
        clickListener()
        init()
        getViewModel().getUserProfileAPI(getViewModel().getUserId())
    }

    private fun init() {
        mViewBinding?.recyclerView?.adapter = MenuListAdapter(getViewModel().getSettingsMenu(requireActivity()),
            this,idNameViewType = AppConstants.IDNAME_VIEW_TYPE.SETTINGS)
    }

    private fun toolbar() {
        mViewBinding!!.toolBarLayout.tvTitle.hide()
        mViewBinding!!.toolBarLayout.toolBar.setNavigationOnClickListener { v -> onBack() }
        mViewBinding!!.toolBarLayout.cvNotification.hide()
        mViewBinding!!.toolBarLayout.cvSearch.hide()
        mViewBinding?.toolBarLayout?.cvBack?.visible()
        mViewBinding?.toolBarLayout?.cvBack?.setImageResource(R.drawable.ic_back_new)
        mViewBinding?.toolBarLayout?.cvBack?.onClick { onBack() }
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
        mNavController!!.navigateUp()
    }

    private fun clickListener() {
        mViewBinding!!.tvLogout.setOnClickListener {
            displayLogoutDialog()
        }

    }

    override fun onItemPress(data: IdNameData) {
        when (data.value) {
            getString(R.string.personal_info) -> {
                val bundle = Bundle()
                bundle.putBoolean(AppConstants.FROM.EDIT, true)
                mNavController?.navigate(R.id.toPersonalInfoFragment, bundle)
            }
            getString(R.string.employer) -> {
                val bundle = Bundle()
                bundle.putBoolean(AppConstants.FROM.EDIT, true)
                mNavController?.navigate(R.id.toContactInfoFragment,bundle)
            }
            getString(R.string.privacy) -> {
                mNavController?.navigate(R.id.toPrivacyControlFragment)
            }
            getString(R.string.notifications) -> {
                mNavController?.navigate(R.id.toPushNotificationPreferenceMenuFragment)
            }
//            getString(R.string.contact_info) -> {
//                val bundle = Bundle()
//                bundle.putBoolean(AppConstants.FROM.EDIT, true)
//                mNavController?.navigate(R.id.toContactInfoFragment, bundle)
//            }
            getString(R.string.device_and_applications) -> {
                val bundle = Bundle()
                bundle.putString(AppConstants.EXTRAS.FROM, AppConstants.FROM.SETTINGS)
                mNavController?.navigate(R.id.toWearableActivity, bundle)
            }
            getString(R.string.help) -> {
                mNavController?.navigate(R.id.toHelpFragment)
            }
            getString(R.string.content_preferences) -> {
                mNavController?.navigate(R.id.toContentPreferenceFragment)
            }
        }
    }

    override fun userProfileFetchSuccess(data: UserProfileData?) {
        if (data == null) return
        getViewModel().userProfileData.set(data)
    }
}