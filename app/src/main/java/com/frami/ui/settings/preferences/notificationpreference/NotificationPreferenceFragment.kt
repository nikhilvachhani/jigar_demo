package com.frami.ui.settings.preferences.notificationpreference

import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.viewModels
import com.frami.BR
import com.frami.R
import com.frami.data.model.settings.pushnotificationmenu.mainmenu.PushNotificationMenuData
import com.frami.data.model.settings.pushnotificationmenu.notificationdetails.PushNotificationOnData
import com.frami.data.model.settings.pushnotificationmenu.notificationdetails.PushNotificationsOnResponseData
import com.frami.databinding.FragmentNotificationPreferenceBinding
import com.frami.ui.base.BaseFragment
import com.frami.ui.settings.preferences.notificationpreference.adapter.PushNotificationParentAdapter
import com.frami.utils.AppConstants
import com.frami.utils.extensions.hide
import com.frami.utils.extensions.onClick
import com.frami.utils.extensions.visible


class NotificationPreferenceFragment :
    BaseFragment<FragmentNotificationPreferenceBinding, NotificationPreferenceFragmentViewModel>(),
    NotificationPreferenceFragmentNavigator, PushNotificationParentAdapter.OnItemClickListener {

    private val viewModelInstance: NotificationPreferenceFragmentViewModel by viewModels {
        viewModeFactory
    }
    private var mViewBinding: FragmentNotificationPreferenceBinding? = null
    override fun getBindingVariable(): Int = BR.viewModel

    override fun getLayoutId(): Int = R.layout.fragment_notification_preference

    override fun getViewModel(): NotificationPreferenceFragmentViewModel = viewModelInstance

    private lateinit var notificationPreferenceListAdapter: PushNotificationParentAdapter

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
    }

    private fun init() {
        notificationPreferenceListAdapter = PushNotificationParentAdapter(ArrayList(), this)
        mViewBinding!!.recyclerView.adapter = notificationPreferenceListAdapter
        callAPI()
    }

    private fun callAPI() {
        getViewModel().pushNotificationMenuData.get()?.let {
            getViewModel().getNotificationPreferenceAPI(it.key)
        }
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

//    override fun onUpdatePreferenceItem(
//        data: List<NotificationPreferenceData>,
//        adapterPosition: Int,
//        isChecked: Boolean
//    ) {
//        val notificationData = NotificationPreferenceResponseData()
//        getViewModel().notificationPreferenceResponseData.get()?.let {
//            notificationData.id = getViewModel().notificationPreferenceResponseData.get()?.id!!
//            notificationData.userId =
//                getViewModel().notificationPreferenceResponseData.get()?.userId!!
//        }
//        for (notificationPreference in data) {
//            when (notificationPreference.title) {
//                getString(R.string.news_and_updates) -> {
//                    notificationData.newsUpdates = notificationPreference.isSelected
//                }
//                getString(R.string.challenges) -> {
//                    notificationData.challanges = notificationPreference.isSelected
//                }
//                getString(R.string.rewards) -> {
//                    notificationData.rewards = notificationPreference.isSelected
//                }
//                getString(R.string.events) -> {
//                    notificationData.events = notificationPreference.isSelected
//                }
//                getString(R.string.notifications) -> {
//                    notificationData.notifications = notificationPreference.isSelected
//                }
//            }
//        }
//        when (data[adapterPosition].title) {
//            getString(R.string.news_and_updates) -> {
//                notificationData.newsUpdates = isChecked
//            }
//            getString(R.string.challenges) -> {
//                notificationData.challanges = isChecked
//            }
//            getString(R.string.rewards) -> {
//                notificationData.rewards = isChecked
//            }
//            getString(R.string.events) -> {
//                notificationData.events = isChecked
//            }
//            getString(R.string.notifications) -> {
//                notificationData.notifications = isChecked
//            }
//        }
//        getViewModel().updateNotificationPreferenceAPI(notificationData)
//    }

//    override fun notificationPreferenceDataFetchSuccess(data: NotificationPreferenceResponseData?) {
//        getViewModel().notificationPreferenceResponseData.set(data)
//        val notificationPreferenceDataList =
//            getViewModel().getNotificationPreferenceMenu(requireActivity())
//        data?.let {
//            for (notificationPreference in notificationPreferenceDataList) {
//                when (notificationPreference.title) {
//                    getString(R.string.news_and_updates) -> {
//                        notificationPreference.isSelected = data.newsUpdates
//                    }
//                    getString(R.string.challenges) -> {
//                        notificationPreference.isSelected = data.challanges
//                    }
//                    getString(R.string.rewards) -> {
//                        notificationPreference.isSelected = data.rewards
//                    }
//                    getString(R.string.events) -> {
//                        notificationPreference.isSelected = data.events
//                    }
//                    getString(R.string.notifications) -> {
//                        notificationPreference.isSelected = data.notifications
//                    }
//                }
//            }
//            notificationPreferenceListAdapter.data = notificationPreferenceDataList
//        }
//    }

    override fun onUpdatePreferenceItem(
        parentPosition: Int,
        data: List<PushNotificationOnData>,
        adapterPosition: Int,
        checked: Boolean
    ) {
        getViewModel().updatePushNotificationOnPreferenceAPI(
            parentPosition,
            data,
            adapterPosition,
            checked
        )
    }

    override fun specificPushNotificationUpdate(data: PushNotificationOnData) {
        when (data.key) {
            AppConstants.KEYS.CHALLENGEPOSTS,
            AppConstants.KEYS.EVENTSPOSTS,
            AppConstants.KEYS.COMMUNITIESPOSTS -> {
                callAPI()
            }
        }
    }

    override fun notificationUpdatePreferenceSuccess(
        parentPosition: Int,
        data: List<PushNotificationOnData>,
        adapterPosition: Int,
        isChecked: Boolean
    ) {
        data[adapterPosition].value = isChecked
        notificationPreferenceListAdapter.notifySubItem(parentPosition, adapterPosition, isChecked)
    }

    override fun notificationPreferenceDataFetchSuccess(data: List<PushNotificationsOnResponseData>?) {
        data?.let {
            notificationPreferenceListAdapter.data = it
        }
    }

    override fun onSpecificClick(pushNotificationsOnResponseData: PushNotificationsOnResponseData) {
        // TODO specific challenge, specific community click changes remain
        val bundle = Bundle()
        bundle.putSerializable(
            AppConstants.EXTRAS.PUSHNOTIFICATIONSONRESPONSEDATA,
            pushNotificationsOnResponseData
        )
        mNavController!!.navigate(R.id.toSpecificNotificationPreferenceFragment, bundle)


    }
}