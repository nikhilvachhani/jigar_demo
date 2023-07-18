package com.frami.ui.settings.preferences.notificationpreference.specific

import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.viewModels
import com.frami.BR
import com.frami.R
import com.frami.data.model.settings.pushnotificationmenu.notificationdetails.PushNotificationsOnResponseData
import com.frami.data.model.settings.pushnotificationmenu.specific.SpecificPushNotificationOnData
import com.frami.databinding.FragmentSpecificNotificationPreferenceBinding
import com.frami.ui.base.BaseFragment
import com.frami.ui.settings.preferences.notificationpreference.specific.adapter.SpecificPushNotificationAdapter
import com.frami.utils.AppConstants
import com.frami.utils.extensions.hide
import com.frami.utils.extensions.visible


class SpecificNotificationPreferenceFragment :
    BaseFragment<FragmentSpecificNotificationPreferenceBinding, SpecificNotificationPreferenceFragmentViewModel>(),
    SpecificNotificationPreferenceFragmentNavigator,
    SpecificPushNotificationAdapter.OnItemClickListener {

    private val viewModelInstance: SpecificNotificationPreferenceFragmentViewModel by viewModels {
        viewModeFactory
    }
    private var mViewBinding: FragmentSpecificNotificationPreferenceBinding? = null
    override fun getBindingVariable(): Int = BR.viewModel

    override fun getLayoutId(): Int = R.layout.fragment_specific_notification_preference

    override fun getViewModel(): SpecificNotificationPreferenceFragmentViewModel = viewModelInstance

    private lateinit var specificPushNotificationAdapter: SpecificPushNotificationAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            if (requireArguments().containsKey(AppConstants.EXTRAS.PUSHNOTIFICATIONSONRESPONSEDATA))
                getViewModel().pushNotificationsOnResponseData.set(
                    requireArguments().getSerializable(
                        AppConstants.EXTRAS.PUSHNOTIFICATIONSONRESPONSEDATA
                    ) as PushNotificationsOnResponseData
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
        specificPushNotificationAdapter = SpecificPushNotificationAdapter(ArrayList(), this)
        mViewBinding!!.recyclerView.adapter = specificPushNotificationAdapter
        callAPI()
    }

    private fun callAPI() {
        getViewModel().pushNotificationsOnResponseData.get()?.let {
            getViewModel().getUserSpecificPushNotificationPreference(it.specificKey() ?: "")
        }
    }

    private fun toolbar() {
        mViewBinding!!.toolBarLayout.tvTitle.hide()
        mViewBinding!!.toolBarLayout.toolBar.setNavigationOnClickListener { v -> onBack() }
        mViewBinding!!.toolBarLayout.cvNotification.hide()
        mViewBinding!!.toolBarLayout.cvSearch.hide()
        mViewBinding!!.toolBarLayout.cvBack.visible()
        mViewBinding!!.toolBarLayout.cvBack.setOnClickListener { onBack() }
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
        data: List<SpecificPushNotificationOnData>,
        adapterPosition: Int,
        checked: Boolean
    ) {
        getViewModel().updateSpecificPushNotificationOnPreferenceAPIWithUpdateLogic(
            data,
            adapterPosition,
            checked
        )
    }

    override fun onSpecificItemPress(data: SpecificPushNotificationOnData) {
        when (data.key) {
            AppConstants.KEYS.COMMUNITY -> navigateToCommunityDetails(data.relatedItemId, true)
            AppConstants.KEYS.SUBCOMMUNITY -> navigateToSubCommunityDetails(
                data.relatedItemId,
                true
            )
            AppConstants.KEYS.CHALLENGE -> navigateToChallengeDetails(data.relatedItemId, true)
            AppConstants.KEYS.EVENT -> navigateToEventDetails(data.relatedItemId, true)
        }
    }

    override fun specificPushNotificationUpdatePreferenceSuccessWithUpdate(
        data: List<SpecificPushNotificationOnData>,
        adapterPosition: Int,
        isChecked: Boolean
    ) {
        data[adapterPosition].value = isChecked
        specificPushNotificationAdapter.notifyItemChanged(adapterPosition)
    }

    override fun notificationPreferenceDataFetchSuccess(data: List<SpecificPushNotificationOnData>?) {
        data?.let {
            if (it.isNotEmpty()) {
                specificPushNotificationAdapter.data = it
            } else {
                onBack()
            }
        }
    }
}