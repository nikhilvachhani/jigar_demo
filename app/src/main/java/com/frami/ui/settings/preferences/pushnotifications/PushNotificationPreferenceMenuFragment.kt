package com.frami.ui.settings.preferences.pushnotifications

import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.viewModels
import com.frami.BR
import com.frami.R
import com.frami.data.model.settings.pushnotificationmenu.mainmenu.PushNotificationMenuData
import com.frami.databinding.FragmentPushNotificationPreferenceMenuBinding
import com.frami.ui.base.BaseFragment
import com.frami.ui.settings.preferences.pushnotifications.adapter.PushNotificationMenuListAdapter
import com.frami.utils.AppConstants
import com.frami.utils.extensions.hide
import com.frami.utils.extensions.onClick
import com.frami.utils.extensions.visible

class PushNotificationPreferenceMenuFragment :
    BaseFragment<FragmentPushNotificationPreferenceMenuBinding, PushNotificationPreferenceMenuFragmentViewModel>(),
    PushNotificationPreferenceMenuFragmentNavigator,
    PushNotificationMenuListAdapter.OnItemClickListener {

    private val viewModelInstance: PushNotificationPreferenceMenuFragmentViewModel by viewModels {
        viewModeFactory
    }
    private var mViewBinding: FragmentPushNotificationPreferenceMenuBinding? = null
    override fun getBindingVariable(): Int = BR.viewModel

    override fun getLayoutId(): Int = R.layout.fragment_push_notification_preference_menu

    override fun getViewModel(): PushNotificationPreferenceMenuFragmentViewModel = viewModelInstance

    private lateinit var notificationPreferenceListAdapter: PushNotificationMenuListAdapter

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
        notificationPreferenceListAdapter = PushNotificationMenuListAdapter(
//            getViewModel().getNotificationPreferenceMenu(requireActivity()),
            ArrayList(),
            this
        )
        mViewBinding!!.recyclerView.adapter = notificationPreferenceListAdapter
        getViewModel().getNotificationOnPreferenceAPI()
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

    override fun notificationPreferenceDataFetchSuccess(data: List<PushNotificationMenuData>?) {
//        getViewModel().notificationPreferenceResponseData.set(data)
//        val notificationPreferenceDataList =
//            getViewModel().getNotificationPreferenceMenu(requireActivity())
        data?.let {
            notificationPreferenceListAdapter.data = it
        }
    }

    override fun onItemPress(data: PushNotificationMenuData) {
        if (data.key.equals("EMAILSETTING",true)){
            val bundle = Bundle()
            bundle.putSerializable(AppConstants.EXTRAS.NOTIFICATION_PREFERENCE_DATA, data)
            mNavController?.navigate(R.id.toEmailSettingsFragment, bundle)
        }else{
            val bundle = Bundle()
            bundle.putSerializable(AppConstants.EXTRAS.NOTIFICATION_PREFERENCE_DATA, data)
            mNavController?.navigate(R.id.toNotificationPreferenceFragment, bundle)
        }

    }
}