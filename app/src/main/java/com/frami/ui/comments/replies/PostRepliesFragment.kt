package com.frami.ui.comments.replies

import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.viewModels
import com.frami.BR
import com.frami.R
import com.frami.data.model.activity.comment.CommentData
import com.frami.data.model.activity.comment.replay.PostCommentReplayData
import com.frami.databinding.FragmentPostRepliesBinding
import com.frami.ui.base.BaseFragment
import com.frami.ui.comments.replies.adapter.PostCommentReplyAdapter
import com.frami.utils.AppConstants
import com.frami.utils.extensions.visible

class PostRepliesFragment :
    BaseFragment<FragmentPostRepliesBinding, PostRepliesFragmentViewModel>(),
    PostRepliesFragmentNavigator, PostCommentReplyAdapter.OnItemClickListener {

    private val viewModelInstanceCategory: PostRepliesFragmentViewModel by viewModels {
        viewModeFactory
    }
    private var mViewBinding: FragmentPostRepliesBinding? = null
    override fun getBindingVariable(): Int = BR.viewModel

    override fun getLayoutId(): Int = R.layout.fragment_post_replies

    override fun getViewModel(): PostRepliesFragmentViewModel = viewModelInstanceCategory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            if (requireArguments().containsKey(AppConstants.EXTRAS.COMMENT)) {
                getViewModel().commentData.set(requireArguments().getSerializable(AppConstants.EXTRAS.COMMENT) as CommentData)
            }
            if (requireArguments().containsKey(AppConstants.EXTRAS.POST_ID)) {
                getViewModel().postId.set(requireArguments().getString(AppConstants.EXTRAS.POST_ID))
            }
            if (requireArguments().containsKey(AppConstants.EXTRAS.POST_TYPE)) {
                getViewModel().postType.set(requireArguments().getString(AppConstants.EXTRAS.POST_TYPE))
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
        if (getViewModel().postId.get() != null) {
            if (getViewModel().postType.get() == AppConstants.POST_TYPE.Activity) {
                getViewModel().getActivityCommentReplayAPI()
            } else {
                getViewModel().getPostCommentReplayAPI()
            }
        }
    }

    private fun toolbar() {
        mViewBinding!!.toolBarLayout.tvTitle.text = getString(R.string.comment_reply)
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
                    getViewModel().replayCreateOnActivityComment(
                        content.toString(),
                        getViewModel().postId.get()
                    )
                } else {
                    getViewModel().replayCreateOnPostComment(
                        content.toString(),
                        getViewModel().postId.get()
                    )
                }
            }
        }
    }

    override fun commentAddedSuccessfully() {
        callAPI()
    }

    override fun postCommentsReplayFetchSuccess(list: List<PostCommentReplayData>?) {
        setRefreshEnableDisable(false)
        mViewBinding!!.recyclerView.adapter =
            list?.let {
                getViewModel().commentData.get().apply {
                    this?.replyCount = it.size
                    getViewModel().commentData.set(this).apply {
                        mViewBinding!!.viewCommentData.notifyChange()
                    }
                }
                getViewModel().isDataEmpty.set(it.isEmpty())
                PostCommentReplyAdapter(
                    it, this
                )
            }
    }

    override fun onUserIconPress(data: PostCommentReplayData) {
        navigateToUserProfile(data.userId)
    }

    override fun onDeletePress(data: PostCommentReplayData) {
        val alertDialog = AlertDialog.Builder(requireContext(), R.style.AlertDialogTheme).create()
        alertDialog.setMessage(resources.getString(R.string.delete_comment_message))
        alertDialog.setButton(
            AlertDialog.BUTTON_NEGATIVE, resources.getString(R.string.no)
        ) { dialog, which -> }
        alertDialog.setButton(
            AlertDialog.BUTTON_POSITIVE, resources.getString(R.string.yes)
        ) { dialog, which ->
            if (getViewModel().postType.get() == AppConstants.POST_TYPE.Activity) {
                getViewModel().deleteActivityCommentReplayAPI(data.commentReplyId)
            } else {
                getViewModel().deletePostCommentReplayAPI(data.commentReplyId)
            }
        }
        alertDialog.show()
    }
}