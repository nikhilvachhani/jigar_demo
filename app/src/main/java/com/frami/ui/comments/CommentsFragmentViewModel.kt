package com.frami.ui.comments

import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import com.frami.data.DataManager
import com.frami.data.model.activity.request.CreateRemoveApplauseCommentActivityUser
import com.frami.data.model.home.ActivityData
import com.frami.data.model.post.create.CreateCommentOnPostRequest
import com.frami.ui.base.BaseViewModel
import com.frami.utils.AppConstants
import com.frami.utils.rx.SchedulerProvider
import com.google.gson.Gson
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import javax.inject.Inject

class CommentsFragmentViewModel @Inject constructor(
    dataManager: DataManager,
    schedulerProvider: SchedulerProvider,
    mCompositeDisposable: CompositeDisposable
) : BaseViewModel<CommentsFragmentNavigator>(
    dataManager,
    schedulerProvider,
    mCompositeDisposable
) {

    var postType = ObservableField<String>(AppConstants.POST_TYPE.Event)
    var relatedId = ObservableField<String>()
    var isRefreshing = ObservableBoolean(false)
    var activityData = ObservableField<ActivityData>()
    var isDataEmpty = ObservableBoolean(false)
    fun getCommentAPI() {
        if (postType.get().isNullOrBlank()) {
            isRefreshing.set(false)
            return
        }

        if (!isRefreshing.get())
            getNavigator()?.showLoading()
        val disposable: Disposable = getDataManager()
            .getCommentAPI(relatedId.get() ?: "")
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.ui())
            .subscribe({ response ->
                if (response != null) {
                    getNavigator()?.log("getCommentAPI response>>> ${Gson().toJson(response)}")
                    getNavigator()?.commentsFetchSuccess(response.data)
                    if (response.isSuccess()) {
                    } else {
                        getNavigator()?.showMessage(response.getMessage())
                    }
                }
                getNavigator()?.hideLoading()
            }, { throwable ->
                getNavigator()?.commentsFetchSuccess(null)
                getNavigator()?.hideLoading()
                getNavigator()?.handleError(throwable)
            })
        mCompositeDisposable.add(disposable)
    }

    fun deleteCommentAPI(commentId: String) {
        val disposable: Disposable = getDataManager()
            .deleteCommentAPI(commentId = commentId)
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.ui())
            .subscribe({ response ->
                if (response != null) {
                    if (response.isSuccess()) {
                        getCommentAPI()
                    } else {
                        getNavigator()?.showMessage(response.getMessage())
                    }
                }
            }, { throwable ->
                getNavigator()?.handleError(throwable)
            })
        mCompositeDisposable.add(disposable)
    }

    fun getActivityDetailsAPI() {
        val disposable: Disposable = getDataManager()
            .getActivityDetailsAPI(activityId = relatedId.get() ?: "")
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.ui())
            .subscribe({ response ->
                if (response != null) {
//                    getNavigator()?.log("Activity Types response>>> ${Gson().toJson(response)}")
                    if (response.isSuccess()) {
                        getNavigator()?.activityDetailsFetchSuccess(response.data)
                    } else {
                        getNavigator()?.showMessage(response.getMessage())
                    }
                }
            }, { throwable ->
                getNavigator()?.handleError(throwable)
            })
        mCompositeDisposable.add(disposable)
    }

    /*
    *POST
     * */
    fun getPostCommentAPI() {
        if (relatedId.get().isNullOrBlank()) {
            isRefreshing.set(false)
            return
        }

        if (!isRefreshing.get())
            getNavigator()?.showLoading()
        val disposable: Disposable = getDataManager()
            .getPostCommentAPI(relatedId.get() ?: "")
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.ui())
            .subscribe({ response ->
                if (response != null) {
                    getNavigator()?.log("getPostCommentAPI response>>> ${Gson().toJson(response)}")
                    getNavigator()?.commentsFetchSuccess(response.data)
                    if (response.isSuccess()) {
                    } else {
                        getNavigator()?.showMessage(response.getMessage())
                    }
                }
                getNavigator()?.hideLoading()
            }, { throwable ->
                getNavigator()?.commentsFetchSuccess(null)
                getNavigator()?.hideLoading()
                getNavigator()?.handleError(throwable)
            })
        mCompositeDisposable.add(disposable)
    }

    fun commentCreateOnPost(content: String, postId: String?) {
        if (user.get() == null) return
        val createCommentOnPostRequest = CreateCommentOnPostRequest(
            postId = postId ?: "",
            userId = getUserId(),
            user = CreateRemoveApplauseCommentActivityUser(
                userId = user.get()?.userId ?: "",
                userName = user.get()?.userName ?: "",
                profilePhotoUrl = user.get()?.profilePhotoUrl ?: ""
            ),
            content = content
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
            .commentCreateOnPost(createCommentOnPostRequest)
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

    fun deletePostCommentAPI(commentId: String) {
        val disposable: Disposable = getDataManager()
            .deletePostCommentAPI(commentId = commentId)
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.ui())
            .subscribe({ response ->
                if (response != null) {
                    if (response.isSuccess()) {
                        getPostCommentAPI()
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