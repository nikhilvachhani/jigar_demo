package com.frami.ui.settings.help

import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.viewModels
import com.frami.BR
import com.frami.R
import com.frami.data.model.common.IdNameData
import com.frami.databinding.FragmentHelpBinding
import com.frami.ui.base.BaseFragment
import com.frami.ui.dashboard.rewards.details.MenuListAdapter
import com.frami.utils.AppConstants
import com.frami.utils.extensions.hide
import com.frami.utils.extensions.onClick
import com.frami.utils.extensions.visible


class HelpFragment :
    BaseFragment<FragmentHelpBinding, HelpFragmentViewModel>(),
    HelpFragmentNavigator, MenuListAdapter.OnItemClickListener {

    private val viewModelInstance: HelpFragmentViewModel by viewModels {
        viewModeFactory
    }
    private var mViewBinding: FragmentHelpBinding? = null
    override fun getBindingVariable(): Int = BR.viewModel

    override fun getLayoutId(): Int = R.layout.fragment_help

    override fun getViewModel(): HelpFragmentViewModel = viewModelInstance

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setStatusBarColor(R.color.lightBg)
        mViewBinding = getViewDataBinding()
        viewModelInstance.setNavigator(this)

        toolbar()
        handleBackPress()
        clickListener()
        init()
    }

    private fun init() {
        mViewBinding!!.recyclerView.adapter =
            MenuListAdapter(
                getViewModel().getHelpMenu(requireActivity()),
                this,
                AppConstants.IDNAME_VIEW_TYPE.SETTINGS
            )
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
    }

    override fun onItemPress(data: IdNameData) {
        when (data.value) {
            getString(R.string.contact_us) -> {
                mNavController!!.navigate(R.id.toContactUsFragment)
            }
            getString(R.string.privacy_policy) -> {
                navigateToPrivacyPolicy()
            }
            getString(R.string.tnc) -> {
                navigateToTNC()
            }
            getString(R.string.faq) -> {
                navigateToFAQ()
            }
            getString(R.string.about_frami) -> {
                navigateToAbout()
            }
            getString(R.string.delete_account) -> {
                displayDeleteAccountDialog()
            }
        }
    }

    private fun displayDeleteAccountDialog() {
        val alertDialog = AlertDialog.Builder(requireContext(), R.style.AlertDialogTheme).create()
        alertDialog.setMessage(resources.getString(R.string.delete_account_message))
        alertDialog.setButton(
            AlertDialog.BUTTON_NEGATIVE, resources.getString(R.string.cancel)
        ) { dialog, which -> }
        alertDialog.setButton(
            AlertDialog.BUTTON_POSITIVE, resources.getString(R.string.delete)
        ) { dialog, which -> deleteAccount() }
        alertDialog.show()
    }

    private fun deleteAccount() {
        if (getViewModel().user.get() != null) {
            getViewModel().deleteUser(getViewModel().user.get()?.userId!!)
        }
    }

    override fun deleteUserSuccess() {
        getBaseActivity().logout()
    }


}