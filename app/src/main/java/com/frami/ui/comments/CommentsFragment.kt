package com.frami.ui.comments

import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.viewModels
import com.frami.BR
import com.frami.R
import com.frami.data.model.activity.comment.CommentData
import com.frami.data.model.home.ActivityDetailsData
import com.frami.data.model.home.LabelValueData
import com.frami.databinding.FragmentCommentsBinding
import com.frami.ui.base.BaseFragment
import com.frami.ui.comments.adapter.CommentsAdapter
import com.frami.ui.dashboard.home.adapter.ActivityAttributeAdapter
import com.frami.utils.AppConstants
import com.frami.utils.extensions.visible

class CommentsFragment :
    BaseFragment<FragmentCommentsBinding, CommentsFragmentViewModel>(),
    CommentsFragmentNavigator, CommentsAdapter.OnItemClickListener,
    ActivityAttributeAdapter.OnItemClickListener {

    private val viewModelInstanceCategory: CommentsFragmentViewModel by viewModels {
        viewModeFactory
    }
    private var mViewBinding: FragmentCommentsBinding? = null
    override fun getBindingVariable(): Int = BR.viewModel

    override fun getLayoutId(): Int = R.layout.fragment_comments

    override fun getViewModel(): CommentsFragmentViewModel = viewModelInstanceCategory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            if (requireArguments().containsKey(AppConstants.EXTRAS.POST_TYPE)) {
                getViewModel().postType.set(requireArguments().getString(AppConstants.EXTRAS.POST_TYPE))
            }
            if (requireArguments().containsKey(AppConstants.EXTRAS.RELATED_ID)) {
                getViewModel().relatedId.set(requireArguments().getString(AppConstants.EXTRAS.RELATED_ID))
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
        callAPI()
        mViewBinding!!.swipeRefreshLayout.setOnRefreshListener {
            setRefreshEnableDisable(true)
            callAPI()
        }
    }

    private fun setRefreshEnableDisable(isRefreshing: Boolean) {
        mViewBinding!!.swipeRefreshLayout.isRefreshing = isRefreshing
        getViewModel().isRefreshing.set(isRefreshing)
    }

    private fun callAPI() {
        if (getViewModel().postType.get() == AppConstants.POST_TYPE.Activity) {
            getViewModel().getActivityDetailsAPI()
            getViewModel().getCommentAPI()
        } else {
            getViewModel().getPostCommentAPI()
        }
    }

    private fun toolbar() {
        mViewBinding!!.toolBarLayout.tvTitle.text = getString(R.string.comments)
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
        mViewBinding!!.cvSend.setOnClickListener {
            val content = mViewBinding!!.etMessage.text?.trim()
            if (content?.isNotEmpty() == true) {
                mViewBinding!!.etMessage.text?.clear()
                if (getViewModel().postType.get() == AppConstants.POST_TYPE.Activity) {
                    getViewModel().commentCreateOnActivity(
                        content.toString(),
                        getViewModel().relatedId.get(),
                        getViewModel().activityData.get()?.userId
                    )
                } else {
                    getViewModel().commentCreateOnPost(
                        content.toString(),
                        getViewModel().relatedId.get()
                    )
                }
            }
        }
        mViewBinding!!.activityItem.cvActivity.setOnClickListener {
            navigateToActivityDetails(getViewModel().relatedId.get())
        }
    }

    override fun commentAddedSuccessfully() {
        callAPI()
    }

    override fun commentsFetchSuccess(list: List<CommentData>?) {
        setRefreshEnableDisable(false)
        mViewBinding!!.recyclerView.adapter =
            list?.let {
                getViewModel().isDataEmpty.set(it.isEmpty())
                CommentsAdapter(
                    it, this,
//                    getViewModel().postType.get() != AppConstants.POST_TYPE.Activity
                    true
                )
            }
    }

    override fun onUserIconPress(data: CommentData) {
        navigateToUserProfile(data.userId)
    }

    override fun onCommentReplayPress(data: CommentData) {
        getViewModel().relatedId.get()?.let {
            navigateToPostCommentReplayScreen(
                postId = it,
                postType = getViewModel().postType.get(),
                data
            )
        }
    }

    override fun onDeletePress(data: CommentData) {
        val alertDialog = AlertDialog.Builder(requireContext(), R.style.AlertDialogTheme).create()
        alertDialog.setMessage(resources.getString(R.string.delete_comment_message))
        alertDialog.setButton(
            AlertDialog.BUTTON_NEGATIVE, resources.getString(R.string.no)
        ) { dialog, which -> }
        alertDialog.setButton(
            AlertDialog.BUTTON_POSITIVE, resources.getString(R.string.yes)
        ) { dialog, which ->
            if (getViewModel().postType.get() == AppConstants.POST_TYPE.Activity) {
                getViewModel().deleteCommentAPI(data.commentId)
            } else {
                getViewModel().deletePostCommentAPI(data.commentId)
            }
        }
        alertDialog.show()
    }

    override fun activityDetailsFetchSuccess(data: ActivityDetailsData?) {
        if (data == null) return
        getViewModel().activityData.set(data)
        mViewBinding!!.activityItem.rvAttributes.adapter =
            ActivityAttributeAdapter(data.attributes, this)
    }

    override fun onAttributePress(data: LabelValueData) {
        navigateToActivityDetails(getViewModel().relatedId.get())
    }

}