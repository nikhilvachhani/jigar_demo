package com.frami.ui.post

import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import com.frami.data.DataManager
import com.frami.data.model.home.ActivityData
import com.frami.data.model.post.request.GetPostRequest
import com.frami.ui.base.BaseViewModel
import com.frami.utils.rx.SchedulerProvider
import com.google.gson.Gson
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import javax.inject.Inject

class PostFragmentViewModel @Inject constructor(
    dataManager: DataManager,
    schedulerProvider: SchedulerProvider,
    mCompositeDisposable: CompositeDisposable
) : BaseViewModel<PostFragmentNavigator>(
    dataManager,
    schedulerProvider,
    mCompositeDisposable
) {

    var relatedId = ObservableField<String>()
    var postType = ObservableField<String>()
    var postTitle = ObservableField<String>()
    var postId = ObservableField<String>()
    var screenType = ObservableField<String>()
    var isReportedPost = ObservableBoolean(false)
    var isRefreshing = ObservableBoolean(false)
    var activityData = ObservableField<ActivityData>()
    var isDataEmpty = ObservableBoolean(false)
    var isLoadMore = ObservableBoolean(false)

    fun getPostAPIWithLoadMore(relatedId: String, postType: String) {
        if (getContinuousToken() != null) {
            if (isNotContinuousToken()) {
                isLoadMore.set(false)
                return
            }
        }
        val getPostRequest = GetPostRequest(
            relatedId = relatedId,
            postType = postType
        )
        if (!isRefreshing.get() && !isLoadMore.get())
            getNavigator()?.showLoading()
        val disposable: Disposable = getDataManager()
            .getPostAPI(getPostRequest, true)
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.ui())
            .subscribe({ response ->
                if (response != null) {
                    getNavigator()?.log("getPostAPI response>>> ${Gson().toJson(response)}")
                    getNavigator()?.postFetchSuccess(response.data, )
                    if (response.isSuccess()) {
                    } else {
                        getNavigator()?.showMessage(response.getMessage())
                    }
                }
                getNavigator()?.hideLoading()
            }, { throwable ->
                getNavigator()?.postFetchSuccess(null, )
                getNavigator()?.hideLoading()
                getNavigator()?.handleError(throwable)
            })
        mCompositeDisposable.add(disposable)
    }

}