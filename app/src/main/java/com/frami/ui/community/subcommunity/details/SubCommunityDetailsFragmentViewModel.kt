package com.frami.ui.community.subcommunity.details

import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import com.frami.data.DataManager
import com.frami.data.model.activity.comment.CommentData
import com.frami.data.model.community.subcommunity.SubCommunityData
import com.frami.ui.base.BaseViewModel
import com.frami.utils.AppConstants
import com.frami.utils.rx.SchedulerProvider
import com.google.gson.Gson
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import javax.inject.Inject

class SubCommunityDetailsFragmentViewModel @Inject constructor(
    dataManager: DataManager,
    schedulerProvider: SchedulerProvider,
    mCompositeDisposable: CompositeDisposable
) : BaseViewModel<SubCommunityDetailsFragmentNavigator>(
    dataManager,
    schedulerProvider,
    mCompositeDisposable
) {
    var isChildSubCommunity = ObservableBoolean(false)
    var subCommunityId = ObservableField<String>()
    var isAbleToGoBack = ObservableBoolean(true)
    var subCommunityData = ObservableField<SubCommunityData>()
    var isActive = ObservableField<Int>(AppConstants.ISACTIVE.ACTIVE)
    var isRefreshing = ObservableBoolean(false)
    var postType = ObservableField<String>()
    var postId = ObservableField<String>()
    var screenType = ObservableField<String>()
    var commentData = ObservableField<CommentData>()
    var postTypeSubOrChild = ObservableField<String>(AppConstants.POST_TYPE.SubCommunity)


    fun getSubCommunityDetailsAPI() {
        if (!isRefreshing.get())
            getNavigator()?.showLoading()
        val disposable: Disposable = getDataManager()
            .getSubCommunityDetailsAPI(
                subCommunityId = subCommunityId.get()
                    ?: ""
            )
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
                        getNavigator()?.subCommunityDetailsFetchSuccess(response.data)
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

    fun deleteSubCommunity() {
        getNavigator()?.showLoading()
        val disposable: Disposable = getDataManager()
            .deleteSubCommunity(subCommunityId = subCommunityData.get()?.id ?: "")
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.ui())
            .subscribe({ response ->
                getNavigator()?.hideLoading()
                if (response != null) {
                    getNavigator()?.log(
                        "deleteSubCommunity response>>> ${
                            Gson().toJson(
                                response
                            )
                        }"
                    )
                    if (response.isSuccess()) {
                        getNavigator()?.subCommunityDeleteSuccess(response.getMessage())
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

    //Child Sub Community
    fun getChildSubCommunityDetailsAPI() {
        if (!isRefreshing.get())
            getNavigator()?.showLoading()
        val subCommunityId =
            if (subCommunityData.get() != null) subCommunityData.get()?.id else subCommunityId.get()
        val disposable: Disposable = getDataManager()
            .getChildSubCommunityDetailsAPI(
                subCommunityId = subCommunityId ?: ""
            )
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.ui())
            .subscribe({ response ->
                if (response != null) {
                    getNavigator()?.log(
                        "getChildSubCommunityDetailsAPI response>>> ${
                            Gson().toJson(
                                response
                            )
                        }"
                    )
                    if (response.isSuccess()) {
                        getNavigator()?.subCommunityDetailsFetchSuccess(response.data)
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

    fun deleteChildSubCommunity() {
        getNavigator()?.showLoading()
        val disposable: Disposable = getDataManager()
            .deleteChildSubCommunity(subCommunityId = subCommunityData.get()?.id ?: "")
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.ui())
            .subscribe({ response ->
                getNavigator()?.hideLoading()
                if (response != null) {
                    getNavigator()?.log(
                        "deleteSubCommunity response>>> ${
                            Gson().toJson(
                                response
                            )
                        }"
                    )
                    if (response.isSuccess()) {
                        getNavigator()?.subCommunityDeleteSuccess(response.getMessage())
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