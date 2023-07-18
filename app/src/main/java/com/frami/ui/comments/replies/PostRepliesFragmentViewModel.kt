package com.frami.ui.comments.replies

import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import com.frami.data.DataManager
import com.frami.data.model.activity.comment.CommentData
import com.frami.data.model.activity.comment.replay.request.CreateReplayOnActivityCommentRequest
import com.frami.data.model.activity.comment.replay.request.CreateReplayOnPostCommentRequest
import com.frami.data.model.activity.request.CreateRemoveApplauseCommentActivityUser
import com.frami.ui.base.BaseViewModel
import com.frami.utils.AppConstants
import com.frami.utils.rx.SchedulerProvider
import com.google.gson.Gson
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import javax.inject.Inject

class PostRepliesFragmentViewModel @Inject constructor(
    dataManager: DataManager,
    schedulerProvider: SchedulerProvider,
    mCompositeDisposable: CompositeDisposable
) : BaseViewModel<PostRepliesFragmentNavigator>(
    dataManager,
    schedulerProvider,
    mCompositeDisposable
) {

    var postType = ObservableField<String>(AppConstants.POST_TYPE.Event)
    var postId = ObservableField<String>()
    var commentData = ObservableField<CommentData>()
    var isRefreshing = ObservableBoolean(false)
    var isDataEmpty = ObservableBoolean(false)

    /*
    *POST
     * */
    fun getPostCommentReplayAPI() {
        if (postId.get().isNullOrBlank()) {
            isRefreshing.set(false)
            return
        }

        if (!isRefreshing.get())
            getNavigator()?.showLoading()
        val disposable: Disposable = getDataManager()
            .getPostCommentReplayAPI(commentData.get()?.commentId ?: "")
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.ui())
            .subscribe({ response ->
                if (response != null) {
                    getNavigator()?.log(
                        "getPostCommentReplayAPI response>>> ${
                            Gson().toJson(
                                response
                            )
                        }"
                    )
                    getNavigator()?.postCommentsReplayFetchSuccess(response.data)
                    if (response.isSuccess()) {
                    } else {
                        getNavigator()?.showMessage(response.getMessage())
                    }
                }
                getNavigator()?.hideLoading()
            }, { throwable ->
                getNavigator()?.postCommentsReplayFetchSuccess(null)
                getNavigator()?.hideLoading()
                getNavigator()?.handleError(throwable)
            })
        mCompositeDisposable.add(disposable)
    }

    fun replayCreateOnPostComment(content: String, postId: String?) {
        if (user.get() == null) return
        val createCommentOnPostRequest = CreateReplayOnPostCommentRequest(
            postId = postId ?: "",
            commentId = commentData.get()?.commentId ?: "",
            user = CreateRemoveApplauseCommentActivityUser(
                userId = user.get()?.userId ?: "",
                userName = user.get()?.userName ?: "",
                profilePhotoUrl = user.get()?.profilePhotoUrl ?: ""
            ),
            content = content,
        )
        getNavigator()!!.log(
            "commentCreateOnPost Request>> ${
                Gson().toJson(
                    createCommentOnPostRequest
                )
            }"
        )
//        getNavigator()?.showLoading()
        val disposable: Disposable = getDataManager()
            .replayCreateOnPostComment(createCommentOnPostRequest)
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.ui())
            .subscribe({ response ->
//                getNavigator()?.hideLoading()
                getNavigator()!!.log(
                    "commentCreateOnPost Response>> ${
                        Gson().toJson(
                            response
                        )
                    }"
                )
                if (response.isSuccess()) {
                    getNavigator()?.commentAddedSuccessfully()
                } else {
                    getNavigator()?.showMessage(response.getMessage())
                }
            }, { throwable ->
                throwable.printStackTrace()
//                getNavigator()?.hideLoading()
                getNavigator()?.handleError(throwable)
            })
        mCompositeDisposable.add(disposable)
    }

    fun deletePostCommentReplayAPI(commentId: String) {
        val disposable: Disposable = getDataManager()
            .deletePostCommentReplayAPI(commentId = commentId)
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.ui())
            .subscribe({ response ->
                if (response != null) {
                    if (response.isSuccess()) {
                        getPostCommentReplayAPI()
                    } else {
                        getNavigator()?.showMessage(response.getMessage())
                    }
                }
            }, { throwable ->
                getNavigator()?.handleError(throwable)
            })
        mCompositeDisposable.add(disposable)
    }

    fun getActivityCommentReplayAPI() {
        if (postId.get().isNullOrBlank()) {
            isRefreshing.set(false)
            return
        }

        if (!isRefreshing.get())
            getNavigator()?.showLoading()
        val disposable: Disposable = getDataManager()
            .getActivityCommentReplayAPI(commentData.get()?.commentId ?: "")
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.ui())
            .subscribe({ response ->
                if (response != null) {
                    getNavigator()?.log(
                        "getActivityCommentReplayAPI response>>> ${
                            Gson().toJson(
                                response
                            )
                        }"
                    )
                    getNavigator()?.postCommentsReplayFetchSuccess(response.data)
                    if (response.isSuccess()) {
                    } else {
                        getNavigator()?.showMessage(response.getMessage())
                    }
                }
                getNavigator()?.hideLoading()
            }, { throwable ->
                getNavigator()?.postCommentsReplayFetchSuccess(null)
                getNavigator()?.hideLoading()
                getNavigator()?.handleError(throwable)
            })
        mCompositeDisposable.add(disposable)
    }

    fun replayCreateOnActivityComment(content: String, activityId: String?) {
        if (user.get() == null) return
        val createReplayOnActivityCommentRequest = CreateReplayOnActivityCommentRequest(
            activityId = activityId ?: "",
            commentId = commentData.get()?.commentId ?: "",
            user = CreateRemoveApplauseCommentActivityUser(
                userId = user.get()?.userId ?: "",
                userName = user.get()?.userName ?: "",
                profilePhotoUrl = user.get()?.profilePhotoUrl ?: ""
            ),
            content = content,
        )
        getNavigator()!!.log(
            "replayCreateOnActivityComment Request>> ${
                Gson().toJson(
                    createReplayOnActivityCommentRequest
                )
            }"
        )
//        getNavigator()?.showLoading()
        val disposable: Disposable = getDataManager()
            .replayCreateOnActivityComment(createReplayOnActivityCommentRequest)
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.ui())
            .subscribe({ response ->
//                getNavigator()?.hideLoading()
                getNavigator()!!.log(
                    "commentCreateOnPost Response>> ${
                        Gson().toJson(
                            response
                        )
                    }"
                )
                if (response.isSuccess()) {
                    getNavigator()?.commentAddedSuccessfully()
                } else {
                    getNavigator()?.showMessage(response.getMessage())
                }
            }, { throwable ->
                throwable.printStackTrace()
//                getNavigator()?.hideLoading()
                getNavigator()?.handleError(throwable)
            })
        mCompositeDisposable.add(disposable)
    }

    fun deleteActivityCommentReplayAPI(commentId: String) {
        val disposable: Disposable = getDataManager()
            .deleteActivityCommentReplayAPI(commentId = commentId)
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.ui())
            .subscribe({ response ->
                if (response != null) {
                    if (response.isSuccess()) {
                        getActivityCommentReplayAPI()
                    } else {
                        getNavigator()?.showMessage(response.getMessage())
                    }
                }
            }, { throwable ->
                getNavigator()?.handleError(throwable)
            })
        mCompositeDisposable.add(disposable)
    }
}