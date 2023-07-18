package com.frami.ui.events.fragment

import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.viewModels
import com.frami.BR
import com.frami.R
import com.frami.data.model.explore.EventsData
import com.frami.data.model.explore.ViewTypes
import com.frami.databinding.FragmentEventsBinding
import com.frami.ui.base.BaseFragment
import com.frami.ui.dashboard.explore.adapter.EventsAdapter
import com.frami.ui.events.details.EventDetailsDialog
import com.frami.utils.extensions.hide
import com.frami.utils.extensions.visible


class EventsFragment :
    BaseFragment<FragmentEventsBinding, EventsFragmentViewModel>(),
    EventsFragmentNavigator, EventsAdapter.OnItemClickListener,
    EventDetailsDialog.OnDialogActionListener {

    private val viewModelInstance: EventsFragmentViewModel by viewModels {
        viewModeFactory
    }
    private var mViewBinding: FragmentEventsBinding? = null
    override fun getBindingVariable(): Int = BR.eventFragmentViewModel

    override fun getLayoutId(): Int = R.layout.fragment_events

    override fun getViewModel(): EventsFragmentViewModel = viewModelInstance

    private lateinit var eventsAdapter: EventsAdapter

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
        eventsAdapter =
            EventsAdapter(
                ArrayList(),
                this,
                true
            )
        mViewBinding!!.run {
            recyclerView.adapter = eventsAdapter

            swipeRefreshLayout.setOnRefreshListener {
                callAPI()
            }
        }
        callAPI()
    }

    private fun setRefreshEnableDisable(isRefreshing: Boolean) {
        mViewBinding!!.swipeRefreshLayout.isRefreshing = isRefreshing
        getViewModel().isRefreshing.set(isRefreshing)
    }

    private fun callAPI() {
        setRefreshEnableDisable(true)
        getViewModel().getRecommendedEventAPI()
    }

    private fun filter(searchString: String) {
        eventsAdapter.filter.filter(searchString)
    }

    private fun toolbar() {
        mViewBinding!!.toolBarLayout.tvTitle.hide()
        mViewBinding!!.toolBarLayout.toolBar.setNavigationOnClickListener { v -> onBack() }
        mViewBinding!!.toolBarLayout.cvSearch.hide() //TODO
        mViewBinding!!.toolBarLayout.cvNotification.hide()
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
        mViewBinding!!.btnCreateNewEvents.setOnClickListener {
//            showComingSoon()
            mNavController!!.navigate(R.id.toCreateEventStep1Fragment)
        }
    }

    override fun showEventPopUp(data: EventsData) {
        val dialog = EventDetailsDialog(requireActivity(), data)
        dialog.setListener(this)
        dialog.show()
    }

    override fun showEventDetails(data: EventsData) {
        navigateToEventDetails(data.eventId)
    }

    override fun onViewAllPress(viewType: ViewTypes?) {

    }

    override fun onEventParticipantStatusChange(data: EventsData, participantStatus: String) {
        getViewModel().changeParticipantStatusEvent(data.eventId, participantStatus)
    }

    override fun changeEventParticipantStatusSuccess(eventId: String, participantStatus: String) {
        setRefreshEnableDisable(true)
        callAPI()
    }

    override fun onViewEventPress(data: EventsData) {
        navigateToEventDetails(data.eventId)
    }

    override fun eventsDataFetchSuccess(list: List<EventsData>?) {
        setRefreshEnableDisable(false)
        getViewModel().isDataEmpty.set(list?.isEmpty() == true)
        if (list == null) return
        eventsAdapter.data = list
    }
}