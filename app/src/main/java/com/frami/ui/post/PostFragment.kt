package com.frami.ui.post

import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.frami.BR
import com.frami.R
import com.frami.data.model.post.MediaUrlsData
import com.frami.data.model.post.PostData
import com.frami.databinding.FragmentPostsBinding
import com.frami.ui.base.BaseFragment
import com.frami.ui.post.adapter.PostAdapter
import com.frami.utils.AppConstants
import com.frami.utils.CommonUtils
import com.frami.utils.extensions.visible

class PostFragment :
    BaseFragment<FragmentPostsBinding, PostFragmentViewModel>(),
    PostFragmentNavigator, PostAdapter.OnItemClickListener {

    private val viewModelInstanceCategory: PostFragmentViewModel by viewModels {
        viewModeFactory
    }
    private var mViewBinding: FragmentPostsBinding? = null
    override fun getBindingVariable(): Int = BR.viewModel

    override fun getLayoutId(): Int = R.layout.fragment_posts

    override fun getViewModel(): PostFragmentViewModel = viewModelInstanceCategory

    private lateinit var listAdapter: PostAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            getViewModel().relatedId.set(requireArguments().getString(AppConstants.EXTRAS.RELATED_ID))
            //TODO when came from notification then post type is not getting
            if (requireArguments().containsKey(AppConstants.EXTRAS.POST_TYPE)) {
                getViewModel().postType.set(requireArguments().getString(AppConstants.EXTRAS.POST_TYPE))
            }
            if (requireArguments().containsKey(AppConstants.EXTRAS.POST_ID)) {
                getViewModel().postId.set(requireArguments().getString(AppConstants.EXTRAS.POST_ID))
            }
            if (requireArguments().containsKey(AppConstants.EXTRAS.SCREEN_TYPE)) {
                getViewModel().screenType.set(requireArguments().getString(AppConstants.EXTRAS.SCREEN_TYPE))
                    .also {
                        getViewModel().isReportedPost.set(getViewModel().screenType.get() == AppConstants.NOTIFICATION_SCREEN_TYPE.ReportPost)
                    }

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
        listAdapter = PostAdapter(ArrayList<PostData>(), this)
        mViewBinding!!.recyclerView.adapter = listAdapter
        callAPI(true)
        mViewBinding!!.swipeRefreshLayout.setOnRefreshListener {
            setRefreshEnableDisable(true)
            callAPI(true)
        }
        if (!getViewModel().isReportedPost.get()) {
            mViewBinding!!.recyclerView.addOnScrollListener(object :
                RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                    val totalItemCount = layoutManager.itemCount ?: 0
                    val lastVisibleItem = layoutManager.findLastVisibleItemPosition() ?: 0
//                log("totalItemCount $totalItemCount lastVisibleItem $lastVisibleItem")
                    if (totalItemCount > 6) {
                        if (totalItemCount <= lastVisibleItem + 1 && !getViewModel().isLoadMore.get()) {
//                    log("CALL APIIIIIII")
                            callAPI(false)
                        }
                    }
                }
            })
        }
    }

    private fun setRefreshEnableDisable(isRefreshing: Boolean) {
        mViewBinding!!.swipeRefreshLayout.isRefreshing = isRefreshing
        getViewModel().isRefreshing.set(isRefreshing)
    }

    private fun callAPI(isFreshCall: Boolean) {
        if (isFreshCall) {
            listAdapter.data = ArrayList()
            getViewModel().setContinuousToken(null)
        } else {
            setIsLoadMore(true)
        }
        callPostAPI()
    }

    private fun callPostAPI() {
        if (getViewModel().screenType.get() != null) {
            getViewModel().postId.get()?.let { postId ->
                getViewModel().relatedId.get()
                    ?.let { relatedId -> getViewModel().getPostDetailsAPI(postId, relatedId) }
            }
        } else {
            getViewModel().postType.get()
                ?.let {
                    getViewModel().relatedId.get()
                        ?.let { it1 -> getViewModel().getPostAPIWithLoadMore(it1, it) }
                }
        }
    }

    private fun setIsLoadMore(isLoading: Boolean) {
        getViewModel().isLoadMore.set(isLoading)
    }

    private fun toolbar() {
        mViewBinding!!.toolBarLayout.tvTitle.text =
            if (getViewModel().isReportedPost.get()) getString(R.string.reported_post) else if (getViewModel().screenType.get() != null) if (!getViewModel().postTitle.get()
                    .isNullOrEmpty()
            ) getString(
                R.string.post_on
            ).plus(" " + getViewModel().postTitle.get()) else getString(
                R.string.posts
            ) else getString(
                R.string.all_posts
            )
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

    override fun postFetchSuccess(list: List<PostData>?, isFromDetails: Boolean?, relatedItemData: String?) {
        setIsLoadMore(false)
        setRefreshEnableDisable(false)
        list?.let {
            listAdapter.appendData(it).apply {
                getViewModel().isDataEmpty.set(listAdapter.data.isEmpty())
            }
        }
    }

    override fun applauseStatusUpdateSuccessfullyOnPost(postData: PostData, adapterPosition: Int) {
        if (postData.isApplauseGiven == true) {
            postData.applauseCount =
                if (postData.applauseCount > 0) postData.applauseCount - 1 else 0
        } else {
            postData.applauseCount = postData.applauseCount + 1
        }
        postData.isApplauseGiven = !postData.isApplauseGiven!!
        listAdapter.notifyItem(postData, adapterPosition)
    }

    override fun onItemPress(data: PostData) {
    }

    override fun onApplauseIconPress(data: PostData, adapterPosition: Int) {
        getViewModel().applauseCreateRemovePost(data, adapterPosition)
    }

    override fun onApplauseCountPress(data: PostData) {
        navigateToApplauseScreen(data.postId, getViewModel().postType.get())
    }

    override fun onCommentIconPress(data: PostData) {
        navigateToCommentScreen(
            data.postId,
            getViewModel().postType.get() ?: AppConstants.POST_TYPE.Event
        )
    }

    override fun onShowAllPress() {

    }

    override fun onEditPostPress(data: PostData) {
        val bundle = Bundle()
        bundle.putString(AppConstants.EXTRAS.POST_TYPE, getViewModel().postType.get().toString())
        data.let {
            bundle.putSerializable(AppConstants.EXTRAS.POST, it)
            mNavController!!.navigate(R.id.toCreatePostFragment, bundle)
        }
    }

    override fun onDeletePostPress(data: PostData) {
        data.let {
            getViewModel().deletePost(it.postId)
        }
    }

    override fun postDeleteSuccess(message: String) {
        showMessage(message)
        callPostAPI()
    }

    override fun onReportPostPress(data: PostData) {
        getViewModel().createPostReport(data)
    }

    override fun onPhotoItemPress(data: MediaUrlsData) {
        if (data.mediaType == AppConstants.MEDIA_TYPE.Image) {
            CommonUtils.showZoomImage(requireActivity(), data.url)
        } else {
            openVideo(data.url)
        }
    }
}