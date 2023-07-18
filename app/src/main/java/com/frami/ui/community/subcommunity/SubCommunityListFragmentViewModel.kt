package com.frami.ui.community.subcommunity

import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import com.frami.data.DataManager
import com.frami.data.model.community.subcommunity.SubCommunityData
import com.frami.data.model.explore.CommunityData
import com.frami.ui.base.BaseViewModel
import com.frami.utils.rx.SchedulerProvider
import com.google.gson.Gson
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import javax.inject.Inject

class SubCommunityListFragmentViewModel @Inject constructor(
    dataManager: DataManager,
    schedulerProvider: SchedulerProvider,
    mCompositeDisposable: CompositeDisposable
) : BaseViewModel<SubCommunityListFragmentNavigator>(
    dataManager,
    schedulerProvider,
    mCompositeDisposable
) {
    var communityData = ObservableField<CommunityData>()
    var subCommunityData = ObservableField<SubCommunityData>()
    var isSearchEnabled = ObservableBoolean(false)
    var isDataEmpty = ObservableBoolean(false)
    var isRefreshing = ObservableBoolean(false)

    fun getSubCommunityAPI() {
        if (!isRefreshing.get())
            getNavigator()?.showLoading()
        val disposable: Disposable = getDataManager()
            .getSubCommunityAPI(communityId = communityData.get()?.communityId ?: "")
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.ui())
            .subscribe({ response ->
                if (response != null) {
                    getNavigator()?.log(
                        "getOwnActiveChallengesAPI response>>> ${
                            Gson().toJson(
                                response
                            )
                        }"
                    )
                    getNavigator()?.subCommunityDataFetchSuccess(response.data)
                    if (response.isSuccess()) {
                    } else {
                        getNavigator()?.showMessage(response.getMessage())
                    }
                }
                getNavigator()?.hideLoading()
            }, { throwable ->
                getNavigator()?.subCommunityDataFetchSuccess(ArrayList())
                getNavigator()?.hideLoading()
                getNavigator()?.handleError(throwable)
            })
        mCompositeDisposable.add(disposable)
    }

    fun getChildSubCommunityAPI() {
        if (!isRefreshing.get())
            getNavigator()?.showLoading()
        val disposable: Disposable = getDataManager()
            .getChildSubCommunityAPI(communityId = subCommunityData.get()?.subCommunityId ?: "")
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.ui())
            .subscribe({ response ->
                if (response != null) {
                    getNavigator()?.log(
                        "getChildSubCommunityAPI response>>> ${
                            Gson().toJson(
                                response
                            )
                        }"
                    )
                    getNavigator()?.subCommunityDataFetchSuccess(response.data)
                    if (response.isSuccess()) {
                    } else {
                        getNavigator()?.showMessage(response.getMessage())
                    }
                }
                getNavigator()?.hideLoading()
            }, { throwable ->
                getNavigator()?.subCommunityDataFetchSuccess(ArrayList())
                getNavigator()?.hideLoading()
                getNavigator()?.handleError(throwable)
            })
        mCompositeDisposable.add(disposable)
    }
}