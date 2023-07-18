package com.frami.ui.events.category

import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.viewModels
import com.frami.BR
import com.frami.R
import com.frami.data.model.events.EventCategoryResponseData
import com.frami.data.model.explore.EventsData
import com.frami.data.model.explore.ViewTypes
import com.frami.databinding.FragmentEventCategoryListBinding
import com.frami.ui.base.BaseFragment
import com.frami.ui.events.category.adapter.EventCategoryListAdapter
import com.frami.ui.events.details.EventDetailsDialog
import com.frami.utils.AppConstants
import com.frami.utils.extensions.hide
import com.frami.utils.extensions.visible


class EventCategoryListFragment :
    BaseFragment<FragmentEventCategoryListBinding, EventCategoryListFragmentViewModel>(),
    EventCategoryListFragmentNavigator, EventCategoryListAdapter.OnItemClickListener,
    EventDetailsDialog.OnDialogActionListener {

    private val viewModelInstanceCategory: EventCategoryListFragmentViewModel by viewModels {
        viewModeFactory
    }
    private var mViewBinding: FragmentEventCategoryListBinding? = null
    override fun getBindingVariable(): Int = BR.viewModel

    override fun getLayoutId(): Int = R.layout.fragment_event_category_list

    override fun getViewModel(): EventCategoryListFragmentViewModel = viewModelInstanceCategory

    private lateinit var eventCategoryListAdapter: EventCategoryListAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mViewBinding = getViewDataBinding()
        viewModelInstanceCategory.setNavigator(this)

        toolbar()
        handleBackPress()
        clickListener()
        init()
    }

    private fun init() {
        eventCategoryListAdapter =
            EventCategoryListAdapter(
                getViewModel().getEventListList(requireActivity()),
                this
            )
        mViewBinding!!.run {
            recyclerView.adapter = eventCategoryListAdapter

            swipeRefreshLayout.setOnRefreshListener {
                setRefreshEnableDisable(true)
                callAPI()
            }
        }
        setRefreshEnableDisable(true)
        callAPI()
    }

    private fun callAPI() {
        getViewModel().getEventsAPI()
    }

    private fun setRefreshEnableDisable(isRefreshing: Boolean) {
        mViewBinding!!.swipeRefreshLayout.isRefreshing = isRefreshing
        getViewModel().isRefreshing.set(isRefreshing)
    }

    private fun toolbar() {
        mViewBinding!!.toolBarLayout.tvTitle.text = getString(R.string.events)
        mViewBinding!!.toolBarLayout.toolBar.setNavigationOnClickListener { v -> onBack() }
        mViewBinding!!.toolBarLayout.cvSearch.hide()
        mViewBinding!!.toolBarLayout.cvSearch.setOnClickListener {
            mNavController!!.navigate(R.id.toEventSearchFragment)
        }
        getViewModel().isNewNotificationObserver.set(getViewModel().getIIsNewNotification())
        mViewBinding!!.toolBarLayout.cvNotification.visible()
        mViewBinding!!.toolBarLayout.cvNotification.setOnClickListener { navigateToNotification() }
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

    override fun eventDataFetchSuccess(data: EventCategoryResponseData?) {
        setRefreshEnableDisable(false)
        if (data == null) return
        if (isAdded) {
            val eventList: MutableList<ViewTypes> = ArrayList()
            val listMyEvents = ArrayList<EventsData>()
            data.myEvents?.let { listMyEvents.addAll(it) }
            if (listMyEvents.isNotEmpty())
                listMyEvents.add(EventsData())
            eventList.add(
                ViewTypes(
                    name = getString(R.string.my_events),
                    viewType = AppConstants.EXPLORE_VIEW_TYPE.EVENTS,
                    data = listMyEvents as ArrayList<Any>,
                    emptyData = getViewModel().getEmptyEvents(requireActivity())
                )
            )

            val listNetworkEvents = ArrayList<EventsData>()
            data.networkEvents?.let { listNetworkEvents.addAll(it) }
            if (listNetworkEvents.isNotEmpty())
                listNetworkEvents.add(EventsData())
            eventList.add(
                ViewTypes(
                    name = getString(R.string.network),
                    viewType = AppConstants.EXPLORE_VIEW_TYPE.EVENTS,
                    data = listNetworkEvents as ArrayList<Any>,
                    emptyData = getViewModel().getEmptyEvents(requireActivity())
                )
            )

            val listRecommendedEvents = ArrayList<EventsData>()
            data.recommededEvents?.let { listRecommendedEvents.addAll(it) }
            if (listRecommendedEvents.isNotEmpty())
                listRecommendedEvents.add(EventsData())
            eventList.add(
                ViewTypes(
                    name = getString(R.string.recommended),
                    viewType = AppConstants.EXPLORE_VIEW_TYPE.EVENTS,
                    data = listRecommendedEvents as ArrayList<Any>,
                    emptyData = getViewModel().getEmptyEvents(requireActivity())
                )
            )

            eventCategoryListAdapter.data = eventList
        }
    }

    override fun onBack() {
        mNavController!!.navigateUp()
    }

    private fun clickListener() {
    }

    override fun onEmptyPreferencesPress() {
        mNavController!!.navigate(R.id.toSettingsFragment)
    }

    override fun onShowAllPress(data: ViewTypes) {
        val bundle = Bundle()
        bundle.putSerializable(AppConstants.EXPLORE_VIEW_TYPE.EVENTS.toString(), data)
        mNavController!!.navigate(R.id.toEventListFragment, bundle)
    }

    override fun showEventPopUp(data: EventsData) {
        val dialog = EventDetailsDialog(requireActivity(), data)
        dialog.setListener(this)
        dialog.show()
    }

    override fun showEventDetails(data: EventsData) {
        navigateToEventDetails(data.eventId)
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
}