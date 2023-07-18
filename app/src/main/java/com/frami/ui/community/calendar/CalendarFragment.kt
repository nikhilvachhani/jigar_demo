package com.frami.ui.community.calendar

import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.viewModels
import com.frami.BR
import com.frami.R
import com.frami.data.model.community.subcommunity.SubCommunityData
import com.frami.data.model.explore.CommunityData
import com.frami.databinding.FragmentCalendarBinding
import com.frami.ui.base.BaseFragment
import com.frami.utils.AppConstants
import com.frami.utils.extensions.hide
import com.frami.utils.extensions.visible


class CalendarFragment :
    BaseFragment<FragmentCalendarBinding, CalendarFragmentViewModel>(), CalendarFragmentNavigator {

    private val viewModelInstanceCategory: CalendarFragmentViewModel by viewModels {
        viewModeFactory
    }
    private var mViewBinding: FragmentCalendarBinding? = null
    override fun getBindingVariable(): Int = BR.viewModel

    override fun getLayoutId(): Int = R.layout.fragment_calendar

    override fun getViewModel(): CalendarFragmentViewModel = viewModelInstanceCategory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
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
        mViewBinding = getViewDataBinding()
        viewModelInstanceCategory.setNavigator(this)

        toolbar()
        handleBackPress()
        clickListener()
        init()
    }

    private fun init() {
    }

    private fun toolbar() {
        if (getViewModel().communityData.get() != null) {
            mViewBinding!!.toolBarLayout.tvTitle.text =
                getViewModel().communityData.get()?.communityName.plus(" ")
                    .plus(getText(R.string.calendar))
        } else if (getViewModel().subCommunityData.get() != null) {
            mViewBinding!!.toolBarLayout.tvTitle.text =
                getViewModel().subCommunityData.get()?.subCommunityName.plus(" ")
                    .plus(getText(R.string.calendar))
        }
        mViewBinding!!.toolBarLayout.cvSearch.hide()
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
    }
}