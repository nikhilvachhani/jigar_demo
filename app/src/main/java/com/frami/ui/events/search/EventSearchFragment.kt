package com.frami.ui.events.search

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.viewModels
import com.frami.BR
import com.frami.R
import com.frami.data.model.explore.EventsData
import com.frami.data.model.explore.ViewTypes
import com.frami.databinding.FragmentEventSearchBinding
import com.frami.ui.base.BaseFragment
import com.frami.ui.dashboard.explore.adapter.EventsAdapter
import com.frami.ui.events.details.EventDetailsDialog
import com.frami.utils.extensions.visible


class EventSearchFragment :
    BaseFragment<FragmentEventSearchBinding, EventSearchFragmentViewModel>(),
    EventSearchFragmentNavigator, EventsAdapter.OnItemClickListener,
    EventDetailsDialog.OnDialogActionListener {

    private val viewModelInstanceCategory: EventSearchFragmentViewModel by viewModels {
        viewModeFactory
    }
    private var mViewBinding: FragmentEventSearchBinding? = null
    override fun getBindingVariable(): Int = BR.viewModel

    override fun getLayoutId(): Int = R.layout.fragment_event_search

    override fun getViewModel(): EventSearchFragmentViewModel = viewModelInstanceCategory

    private var listAdapter = EventsAdapter(
        ArrayList(),
        this,
        true
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

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
        mViewBinding!!.recyclerView.adapter = listAdapter
        listAdapter.data =
            getViewModel().getExploreEventsList(requireActivity()) as MutableList<EventsData>

        mViewBinding!!.searchLayout.etSearchView.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun afterTextChanged(editable: Editable) {
                filter(editable.toString())
            }
        })
    }

    private fun filter(searchString: String) {
        listAdapter.filter.filter(searchString)
    }


    private fun toolbar() {
        mViewBinding!!.toolBarLayout.tvTitle.text = requireActivity().getString(R.string.events)
        mViewBinding!!.toolBarLayout.toolBar.setNavigationOnClickListener { v -> onBack() }
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
        hideKeyboard()
        mNavController!!.navigateUp()
    }

    private fun clickListener() {
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
    }

    override fun onViewEventPress(data: EventsData) {
        navigateToEventDetails(data.eventId)
    }

}