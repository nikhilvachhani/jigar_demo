package com.frami.ui.community.details

import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import com.frami.data.DataManager
import com.frami.data.model.activity.comment.CommentData
import com.frami.data.model.explore.CommunityData
import com.frami.ui.base.BaseViewModel
import com.frami.utils.rx.SchedulerProvider
import com.google.gson.Gson
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import javax.inject.Inject

class CommunityDetailsFragmentViewModel @Inject constructor(
    dataManager: DataManager,
    schedulerProvider: SchedulerProvider,
    mCompositeDisposable: CompositeDisposable
) : BaseViewModel<CommunityDetailsFragmentNavigator>(
    dataManager,
    schedulerProvider,
    mCompositeDisposable
) {
    var communityData = ObservableField<CommunityData>()
    var isAbleToGoBack = ObservableBoolean(true)
    var communityId = ObservableField<String>()
    var isRefreshing = ObservableBoolean(false)
    var postType = ObservableField<String>()
    var postId = ObservableField<String>()
    var screenType = ObservableField<String>()
    var commentData = ObservableField<CommentData>()

    fun getCommunityDetailsAPI() {
        if (!isRefreshing.get())
            getNavigator()?.showLoading()
        val disposable: Disposable = getDataManager()
            .getCommunityDetailsAPI(communityId = communityId.get() ?: "", inviteCommunityId = null)
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.ui())
            .subscribe({ response ->
                if (response != null) {
                    getNavigator()?.log(
                        "getCommunityDetailsAPI response>>> ${
                            Gson().toJson(
                                response
                            )
                        }"
                    )
                    if (response.isSuccess()) {
                        getNavigator()?.communityDetailsFetchSuccess(response.data)
                    } else {
                        getNavigator()?.showMessage(response.getMessage())
                    }
                }
                getNavigator()?.hideLoading()
            }, { throwable ->
                getNavigator()?.hideLoading()
                getNavigator()?.handleError(throwable)
            })
        mCompositeDisposable.add(disposable)
    }

    fun deleteCommunity() {
        getNavigator()?.showLoading()
        val disposable: Disposable = getDataManager()
            .deleteCommunity(communityId = communityId.get() ?: "")
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.ui())
            .subscribe({ response ->
                getNavigator()?.hideLoading()
                if (response != null) {
                    getNavigator()?.log(
                        "deleteChallenge response>>> ${
                            Gson().toJson(
                                response
                            )
                        }"
                    )
                    if (response.isSuccess()) {
                        getNavigator()?.communityDeleteSuccess(response.getMessage())
                    } else {
                        getNavigator()?.showMessage(response.getMessage())
                    }
                }
            }, { throwable ->
                getNavigator()?.hideLoading()
                getNavigator()?.handleError(throwable)
            })
        mCompositeDisposable.add(disposable)
    }

    fun unJoinCommunityAPI() {
        getNavigator()?.showLoading()
        val disposable: Disposable = getDataManager().unJoinCommunityAPI(communityId = communityId.get() ?: "")
            .subscribeOn(schedulerProvider.io()).observeOn(schedulerProvider.ui())
            .subscribe({ response ->
                getNavigator()?.hideLoading()
                if (response != null) {
                    getNavigator()?.log(
                        "unJoinCommunityAPI response>>> ${
                            Gson().toJson(
                                response
                            )
                        }"
                    )
                    if (response.isSuccess()) {
                        getNavigator()?.communityUnjoinSuccess(response.getMessage())
                    } else {
                        getNavigator()?.showMessage(response.getMessage())
                    }
                }
            }, { throwable ->
                getNavigator()?.hideLoading()
                getNavigator()?.handleError(throwable)
            })
        mCompositeDisposable.add(disposable)
    }
}