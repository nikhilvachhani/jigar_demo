package com.frami.ui.followers

import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.viewModels
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.frami.BR
import com.frami.R
import com.frami.data.model.follower.FollowerData
import com.frami.databinding.FragmentFollowersBinding
import com.frami.ui.base.BaseFragment
import com.frami.ui.followers.adapter.FollowersAdapter
import com.frami.ui.followers.requestdailog.FollowRequestConfirmationDialog
import com.frami.utils.AppConstants
import com.frami.utils.extensions.hide
import com.frami.utils.extensions.visible


class FollowersFragment :
    BaseFragment<FragmentFollowersBinding, FollowersFragmentViewModel>(),
    FollowersFragmentNavigator, FollowersAdapter.OnItemClickListener,
    FollowRequestConfirmationDialog.OnDialogActionListener {

    private val viewModelInstance: FollowersFragmentViewModel by viewModels {
        viewModeFactory
    }
    private var mViewBinding: FragmentFollowersBinding? = null
    override fun getBindingVariable(): Int = BR.viewModel

    override fun getLayoutId(): Int = R.layout.fragment_followers

    override fun getViewModel(): FollowersFragmentViewModel = viewModelInstance

    private lateinit var followersAdapter: FollowersAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            getViewModel().isFollowing.set(
                requireArguments().getInt(
                    AppConstants.EXTRAS.SELECTION,
                    AppConstants.FOLLOWING_FOLLOWERS.FOLLOWING
                )
            )
            getViewModel().selectedUserId.set(arguments?.getString(AppConstants.EXTRAS.USER_ID))
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mViewBinding = getViewDataBinding()
        viewModelInstance.setNavigator(this)
        setStatusBarColor(R.color.lightBg)

        toolbar()
        handleBackPress()
        init()
        clickListener()

        callAPI()
    }

    private fun callAPI() {
        if (getViewModel().isFollowing.get() == AppConstants.FOLLOWING_FOLLOWERS.FOLLOWERS) {
            getViewModel().getFollowersAPI()
        } else {
            getViewModel().getFollowingsAPI()
        }
    }

    private fun init() {
        mViewBinding!!.swipeRefreshLayout.setOnRefreshListener(SwipeRefreshLayout.OnRefreshListener {
            setRefreshEnableDisable(true)
            callAPI()
        });
        followersAdapter = FollowersAdapter(ArrayList(), this)
        mViewBinding!!.recyclerView.adapter = followersAdapter
    }

    private fun setRefreshEnableDisable(isRefreshing: Boolean) {
        mViewBinding!!.swipeRefreshLayout.isRefreshing = isRefreshing
    }

    private fun toolbar() {
        mViewBinding!!.toolBarLayout.tvTitle.hide()
        mViewBinding!!.toolBarLayout.toolBar.setNavigationOnClickListener { v -> onBack() }
        mViewBinding!!.toolBarLayout.cvSearch.visible()
        mViewBinding!!.toolBarLayout.cvSearch.setOnClickListener { navigateToInviteFriend() }
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
        mViewBinding!!.tvFollowing.setOnClickListener {
            getViewModel().isFollowing.set(AppConstants.FOLLOWING_FOLLOWERS.FOLLOWING)
            callAPI()
        }
        mViewBinding!!.tvFollowers.setOnClickListener {
            getViewModel().isFollowing.set(AppConstants.FOLLOWING_FOLLOWERS.FOLLOWERS)
            callAPI()
        }
    }

    override fun followerFollowingDataFetchSuccess(data: List<FollowerData>?) {
        setRefreshEnableDisable(false)
//        mViewBinding!!.recyclerView.adapter =
        data?.let {
            getViewModel().isDataEmpty.set(it.isEmpty())
//                FollowersAdapter(it, this)
            followersAdapter.data = it
        }
    }

    override fun onFollowUnfollowPress(data: FollowerData, listDataUpdate: List<FollowerData>) {
        when (data.userFollowStatus) {
            AppConstants.KEYS.UnFollow -> {
                getViewModel().removeFollowersAPI(data.userId)
            }
            AppConstants.KEYS.Request -> {
                val dialog =
                    FollowRequestConfirmationDialog(requireActivity(), data, listDataUpdate)
                dialog.setListener(this)
                dialog.show()
            }
            AppConstants.KEYS.Follow -> {
                getViewModel().addFollowersAPI(data.userId)
            }
        }
    }

    override fun onSendFollowRequestYesPress(data: FollowerData) {
        getViewModel().sendFollowRequestAPI(data)
    }

    override fun onSendFollowRequestNoPress(followersList: List<FollowerData>) {
        followersList.forEach { it.isRunning = false }.also {
            followersAdapter.data = followersList
        }
    }

    override fun onItemPress(data: FollowerData) {
        navigateToUserProfile(data.userId)
    }
}