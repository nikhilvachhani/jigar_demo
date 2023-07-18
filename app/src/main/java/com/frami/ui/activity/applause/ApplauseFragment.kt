package com.frami.ui.activity.applause

import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.viewModels
import com.frami.BR
import com.frami.R
import com.frami.data.model.activity.applause.ApplauseData
import com.frami.data.model.follower.FollowerData
import com.frami.data.model.home.ActivityDetailsData
import com.frami.data.model.home.LabelValueData
import com.frami.databinding.FragmentApplauseBinding
import com.frami.ui.activity.applause.adapter.ApplauseAdapter
import com.frami.ui.base.BaseFragment
import com.frami.ui.dashboard.home.adapter.ActivityAttributeAdapter
import com.frami.ui.followers.requestdailog.FollowRequestConfirmationDialog
import com.frami.utils.AppConstants
import com.frami.utils.extensions.visible

class ApplauseFragment :
    BaseFragment<FragmentApplauseBinding, ApplauseFragmentViewModel>(),
    ApplauseFragmentNavigator, ApplauseAdapter.OnItemClickListener,
    ActivityAttributeAdapter.OnItemClickListener,
    FollowRequestConfirmationDialog.OnDialogActionListener {

    private val viewModelInstanceCategory: ApplauseFragmentViewModel by viewModels {
        viewModeFactory
    }
    private var mViewBinding: FragmentApplauseBinding? = null
    override fun getBindingVariable(): Int = BR.viewModel

    override fun getLayoutId(): Int = R.layout.fragment_applause

    override fun getViewModel(): ApplauseFragmentViewModel = viewModelInstanceCategory

    private lateinit var applauseAdapter: ApplauseAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            if (requireArguments().containsKey(AppConstants.EXTRAS.ACTIVITY_ID)) {
                getViewModel().activityId.set(requireArguments().getString(AppConstants.EXTRAS.ACTIVITY_ID))
            }
            if (requireArguments().containsKey(AppConstants.EXTRAS.RELATED_ID)) {
                getViewModel().postId.set(requireArguments().getString(AppConstants.EXTRAS.RELATED_ID))
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setStatusBarColor(R.color.lightBg)
        mViewBinding = getViewDataBinding()
        viewModelInstanceCategory.setNavigator(this)

        toolbar()
        handleBackPress()
        clickListener()
        init()
    }

    private fun init() {
        applauseAdapter = ApplauseAdapter(ArrayList(), this)
        mViewBinding!!.recyclerView.adapter = applauseAdapter
        callAPI()
        mViewBinding!!.swipeRefreshLayout.setOnRefreshListener {
            setRefreshEnableDisable(true)
            callAPI()
        }
    }

    private fun callAPI() {
        if (getViewModel().activityId.get() != null) {
            getViewModel().getActivityDetailsAPI()
            getViewModel().getApplauseAPI()
        } else if (getViewModel().postId.get() != null) {
            getViewModel().getPostApplauseAPI()
        }
    }

    private fun setRefreshEnableDisable(isRefreshing: Boolean) {
        mViewBinding!!.swipeRefreshLayout.isRefreshing = isRefreshing
        getViewModel().isRefreshing.set(isRefreshing)
    }

    private fun toolbar() {
        mViewBinding!!.toolBarLayout.tvTitle.text = getString(R.string.applause)
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
        mNavController!!.navigateUp()
    }

    private fun clickListener() {
        mViewBinding!!.activityItem.cvActivity.setOnClickListener {
            navigateToActivityDetails(getViewModel().activityId.get())
        }
    }

    override fun applauseDataFetchSuccess(list: List<ApplauseData>?) {
        setRefreshEnableDisable(false)
        list?.let {
            getViewModel().isDataEmpty.set(it.isEmpty())
            applauseAdapter.data = it
        }
    }

    override fun onApplauseItemPress(data: ApplauseData) {
        navigateToUserProfile(data.userId)
    }

    override fun onFollowUnfollowPress(data: FollowerData) {
        when (data.userFollowStatus) {
            AppConstants.KEYS.UnFollow -> {
                getViewModel().removeFollowersAPI(data.userId)
            }
            AppConstants.KEYS.Request -> {
                val dialog = FollowRequestConfirmationDialog(
                    requireActivity(),
                    data,
                    ArrayList()//TODO
                )
                dialog.setListener(this)
                dialog.show()
            }
            AppConstants.KEYS.Follow -> {
                getViewModel().addFollowersAPI(data.userId)
            }
        }
    }

    override fun activityDetailsFetchSuccess(data: ActivityDetailsData?) {
        if (data == null) return
        getViewModel().activityData.set(data)
        mViewBinding!!.activityItem.rvAttributes.adapter =
            ActivityAttributeAdapter(data.attributes, this)
    }

    override fun onAttributePress(data: LabelValueData) {
        navigateToActivityDetails(getViewModel().activityId.get())
    }

    override fun onSendFollowRequestYesPress(data: FollowerData) {
        getViewModel().sendFollowRequestAPI(data)
    }

    override fun onSendFollowRequestNoPress(followersList: List<FollowerData>) {
        applauseAdapter.data.forEach { it.isRunning = false }
            .also { applauseAdapter.notifyDataSetChanged() }
    }
}