package com.frami.ui.followers.findfriends

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.viewModels
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.frami.BR
import com.frami.R
import com.frami.data.model.follower.FollowerData
import com.frami.databinding.FragmentInviteFriendBinding
import com.frami.ui.base.BaseFragment
import com.frami.ui.followers.adapter.FollowersAdapter
import com.frami.ui.followers.requestdailog.FollowRequestConfirmationDialog
import com.frami.utils.AppConstants
import com.frami.utils.extensions.visible


class InviteFriendFragment :
    BaseFragment<FragmentInviteFriendBinding, InviteFriendFragmentViewModel>(),
    InviteFriendFragmentNavigator, FollowersAdapter.OnItemClickListener,
    FollowRequestConfirmationDialog.OnDialogActionListener {

    private val viewModelInstance: InviteFriendFragmentViewModel by viewModels {
        viewModeFactory
    }
    private var mViewBinding: FragmentInviteFriendBinding? = null
    override fun getBindingVariable(): Int = BR.viewModel

    override fun getLayoutId(): Int = R.layout.fragment_invite_friend

    override fun getViewModel(): InviteFriendFragmentViewModel = viewModelInstance

    private lateinit var listAdapter: FollowersAdapter

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
        getViewModel().getSearchUsersAPI()
    }

    private fun init() {
        listAdapter = FollowersAdapter(ArrayList<FollowerData>(), this)
        mViewBinding!!.recyclerView.adapter = listAdapter
        mViewBinding!!.swipeRefreshLayout.setOnRefreshListener(SwipeRefreshLayout.OnRefreshListener {
            mViewBinding!!.etSearchView.text?.clear()
            setRefreshEnableDisable(true)
            callAPI()
        })
        mViewBinding!!.etSearchView.addTextChangedListener(object : TextWatcher {
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

    private fun setRefreshEnableDisable(isRefreshing: Boolean) {
        mViewBinding!!.swipeRefreshLayout.isRefreshing = isRefreshing
    }

    private fun toolbar() {
        mViewBinding!!.toolBarLayout.tvTitle.text = activity?.getString(R.string.find_friends)
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
    }

    override fun searchUserDataFetchSuccess(data: List<FollowerData>?) {
        setRefreshEnableDisable(false)
        mViewBinding!!.etSearchView.text?.clear()
        data?.let {
            getViewModel().isDataEmpty.set(it.isEmpty())
            listAdapter.data = it
        }
    }

    override fun onFollowUnfollowPress(data: FollowerData, listDataUpdate: List<FollowerData>) {
        when (data.userFollowStatus) {
            AppConstants.KEYS.UnFollow -> {
                getViewModel().removeFollowersAPI(data.userId)
            }
            AppConstants.KEYS.Request -> {
                val dialog = FollowRequestConfirmationDialog(
                    requireActivity(),
                    data,
                    listDataUpdate
                )
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
            listAdapter.data = followersList
        }
    }

    override fun onItemPress(data: FollowerData) {
        navigateToUserProfile(data.userId)
    }
}