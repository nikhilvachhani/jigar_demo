package com.frami.ui.events.list

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.viewModels
import com.frami.BR
import com.frami.R
import com.frami.data.model.community.subcommunity.SubCommunityData
import com.frami.data.model.explore.CommunityData
import com.frami.data.model.explore.EventsData
import com.frami.data.model.explore.ViewTypes
import com.frami.databinding.FragmentEventListBinding
import com.frami.ui.base.BaseFragment
import com.frami.ui.dashboard.explore.adapter.EventsAdapter
import com.frami.ui.events.details.EventDetailsDialog
import com.frami.utils.AppConstants
import com.frami.utils.extensions.hide
import com.frami.utils.extensions.visible


class EventListFragment :
    BaseFragment<FragmentEventListBinding, EventListFragmentViewModel>(),
    EventListFragmentNavigator, EventsAdapter.OnItemClickListener,
    EventDetailsDialog.OnDialogActionListener {

    private val viewModelInstanceCategory: EventListFragmentViewModel by viewModels {
        viewModeFactory
    }
    private var mViewBinding: FragmentEventListBinding? = null
    override fun getBindingVariable(): Int = BR.viewModel

    override fun getLayoutId(): Int = R.layout.fragment_event_list

    override fun getViewModel(): EventListFragmentViewModel = viewModelInstanceCategory

    private lateinit var eventsAdapter: EventsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            if (arguments?.containsKey(AppConstants.EXPLORE_VIEW_TYPE.EVENTS.toString()) == true) {
                getViewModel().viewTypes.set(
                    requireArguments().getSerializable(
                        AppConstants.EXPLORE_VIEW_TYPE.EVENTS.toString()
                    ) as ViewTypes
                )
            } else {
                getViewModel().viewTypes.set(
                    ViewTypes(
                        name = getString(R.string.events),
                        viewType = AppConstants.EXPLORE_VIEW_TYPE.EVENTS,
                        data = ArrayList<EventsData>() as ArrayList<Any>,
                        emptyData = getViewModel().getEmptyEvents(requireActivity())
                    )
                )
            }
            getViewModel().eventType.set(
                requireArguments().getInt(
                    AppConstants.EXTRAS.SELECTION,
                    AppConstants.EVENT_TYPE.UPCOMING
                )
            )
            if (arguments?.containsKey(AppConstants.EXTRAS.USER_ID) == true)
                getViewModel().selectedUserId.set(arguments?.getString(AppConstants.EXTRAS.USER_ID))
            if (arguments?.containsKey(AppConstants.EXTRAS.IS_SEARCH) == true)
                getViewModel().isSearchEnabled.set(arguments?.getBoolean(AppConstants.EXTRAS.IS_SEARCH)!!)
            if (arguments?.containsKey(AppConstants.EXTRAS.COMMUNITY) == true) {
                getViewModel().communityData.set(arguments?.getSerializable(AppConstants.EXTRAS.COMMUNITY) as CommunityData)
            }
            if (arguments?.containsKey(AppConstants.EXTRAS.SUB_COMMUNITY) == true) {
                getViewModel().subCommunityData.set(arguments?.getSerializable(AppConstants.EXTRAS.SUB_COMMUNITY) as SubCommunityData)
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setStatusBarColor(R.color.lightBg)
        mViewBinding = getViewDataBinding()
        viewModelInstanceCategory.setNavigator(this)

        if (getViewModel().isSearchEnabled.get()) {
            toolbarTitleCenter()
        } else {
            toolbar()
        }
        handleBackPress()
        clickListener()
        init()
    }

    private fun init() {
        eventsAdapter = EventsAdapter(ArrayList<EventsData>(), this, true)
        mViewBinding!!.run {
            recyclerView.adapter = eventsAdapter

            swipeRefreshLayout.setOnRefreshListener {
                setRefreshEnableDisable(true)
                callAPI()
            }
        }
        setRefreshEnableDisable(true)
        callAPI()

        mViewBinding!!.etSearchView.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun afterTextChanged(editable: Editable) {
                filter(editable.toString())
            }
        })
    }

    private fun filter(searchString: String) {
        eventsAdapter.filter.filter(searchString)
    }

    private fun callAPI() {
        if (getViewModel().eventType.get() == AppConstants.EVENT_TYPE.UPCOMING) {
            if (getViewModel().communityData.get() != null) {
                getViewModel().getCommunityUpcomingEventAPI()
            } else if (getViewModel().subCommunityData.get() != null) {
                getViewModel().subCommunityData.get()?.let {
                    if (it.parentSubCommunityId.isNullOrEmpty()) {
                        getViewModel().getSubCommunityUpcomingEventAPI()
                    } else {
                        getViewModel().getChildSubCommunityUpcomingEventAPI()
                    }
                }
            } else {
                when (getViewModel().viewTypes.get()?.name.toString()) {
                    getString(R.string.my_events), getString(R.string.events) -> {
                        getViewModel().getOwnUpcomingEventAPI()
                    }
                    getString(R.string.network) -> {
                        getViewModel().getNetworkUpcomingEventAPI()
                    }
                    getString(R.string.recommended) -> {
                        getViewModel().getRecommendedUpcomingEventAPI()
                    }
                }
            }
        } else {
            if (getViewModel().communityData.get() != null) {
                getViewModel().getCommunityPreviousEventAPI()
            } else if (getViewModel().subCommunityData.get() != null) {
                getViewModel().subCommunityData.get()?.let {
                    if (it.parentSubCommunityId.isNullOrEmpty()) {
                        getViewModel().getSubCommunityPreviousEventAPI()
                    } else {
                        getViewModel().getChildSubCommunityPreviousEventAPI()
                    }
                }
            } else {
                when (getViewModel().viewTypes.get()?.name.toString()) {
                    getString(R.string.my_events), getString(R.string.events) -> {
                        getViewModel().getOwnPreviousEventAPI()
                    }
                    getString(R.string.network) -> {
                        getViewModel().getNetworkPreviousEventAPI()
                    }
                    getString(R.string.recommended) -> {
                        getViewModel().getRecommendedPreviousEventAPI()
                    }
                }
            }
        }
    }

    private fun setRefreshEnableDisable(isRefreshing: Boolean) {
        mViewBinding!!.swipeRefreshLayout.isRefreshing = isRefreshing
        getViewModel().isRefreshing.set(isRefreshing)
    }

    private fun toolbar() {
        mViewBinding!!.toolBarLayout.tvTitle.hide()
        mViewBinding!!.toolBarLayout.toolBar.setNavigationOnClickListener { v -> onBack() }
        if (!getViewModel().isSearchEnabled.get()) {
            mViewBinding!!.toolBarLayout.cvSearch.visible()
            mViewBinding!!.toolBarLayout.cvSearch.setOnClickListener {
                val bundle = Bundle()
                bundle.putBoolean(AppConstants.EXTRAS.IS_SEARCH, true)
                bundle.putSerializable(
                    AppConstants.EXPLORE_VIEW_TYPE.EVENTS.toString(),
                    getViewModel().viewTypes.get()
                )
                getViewModel().communityData.get()?.let {
                    bundle.putSerializable(AppConstants.EXTRAS.COMMUNITY, it)
                }
                getViewModel().subCommunityData.get()?.let {
                    bundle.putSerializable(AppConstants.EXTRAS.SUB_COMMUNITY, it)
                }
                bundle.putString(AppConstants.EXTRAS.USER_ID, getViewModel().selectedUserId.get())
                getViewModel().eventType.get()
                    ?.let { it1 -> bundle.putInt(AppConstants.EXTRAS.SELECTION, it1) }
                mNavController!!.navigate(R.id.toEventSearchFragment, bundle)
            }
        }
        mViewBinding!!.toolBarLayout.cvNotification.hide()
        mViewBinding!!.toolBarLayout.cvBack.visible()
        mViewBinding!!.toolBarLayout.cvBack.setOnClickListener { onBack() }
    }

    private fun toolbarTitleCenter() {
        if (getViewModel().communityData.get() != null) {
            mViewBinding!!.toolBarTitleCenterLayout.tvTitle.text =
                getViewModel().communityData.get()?.communityName.plus(" ")
                    .plus(getString(R.string.event))
        } else if (getViewModel().subCommunityData.get() != null) {
            mViewBinding!!.toolBarTitleCenterLayout.tvTitle.text =
                getViewModel().subCommunityData.get()?.subCommunityName.plus(" ")
                    .plus(getString(R.string.event))
        } else {
            mViewBinding!!.toolBarTitleCenterLayout.tvTitle.text =
                getViewModel().viewTypes.get()?.name
        }
        mViewBinding!!.toolBarTitleCenterLayout.toolBar.setNavigationOnClickListener { v -> onBack() }
        mViewBinding!!.toolBarTitleCenterLayout.cvBack.visible()
        mViewBinding!!.toolBarTitleCenterLayout.cvBack.setOnClickListener { onBack() }
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
        hideKeyboard()
        mNavController!!.navigateUp()
    }

    private fun clickListener() {
        mViewBinding!!.tvActive.setOnClickListener {
            getViewModel().eventType.set(AppConstants.ISACTIVE.ACTIVE).apply { callAPI() }
        }
        mViewBinding!!.tvPrevious.setOnClickListener {
            getViewModel().eventType.set(AppConstants.ISACTIVE.PREVIOUS).apply { callAPI() }
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
//        navigateToEventDetails(eventId)
    }

    override fun onViewEventPress(data: EventsData) {
        navigateToEventDetails(data.eventId)
    }

    override fun eventDataFetchSuccess(list: List<EventsData>?) {
        setRefreshEnableDisable(false)
        getViewModel().isDataEmpty.set(list?.isEmpty() == true)
        if (list == null) return
        eventsAdapter.data = list
    }
}