package com.frami.ui.dashboard.explore

import androidx.databinding.ObservableBoolean
import com.frami.data.DataManager
import com.frami.ui.base.BaseViewModel
import com.frami.utils.rx.SchedulerProvider
import com.google.gson.Gson
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import javax.inject.Inject

class ExploreFragmentViewModel @Inject constructor(
    dataManager: DataManager,
    schedulerProvider: SchedulerProvider,
    mCompositeDisposable: CompositeDisposable
) : BaseViewModel<ExploreFragmentNavigator>(
    dataManager,
    schedulerProvider,
    mCompositeDisposable
) {

    var isRefreshing = ObservableBoolean(false)
    var isLoadMore = ObservableBoolean(false)
    var isFeedEmpty = ObservableBoolean(false)

    fun getExploreAPI() {
        setContinuousToken(null)
        if (!isRefreshing.get())
            getNavigator()?.showLoading()
        val disposable: Disposable = getDataManager()
            .getExploreAPI()
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.ui())
            .subscribe({ response ->
                if (response != null) {
                    getNavigator()?.log("getExploreAPI response>>> ${Gson().toJson(response)}")
                    if (response.isSuccess()) {
                        getNavigator()?.exploreDataFetchSuccess(response.data)
                    } else {
                        getNavigator()?.showMessage(response.getMessage())
                    }
                }
                getNavigator()?.hideLoading()
            }, { throwable ->
                getNavigator()?.exploreDataFetchSuccess(null)
                getNavigator()?.hideLoading()
                getNavigator()?.handleError(throwable)
            })
        mCompositeDisposable.add(disposable)
    }

    fun getExploreSearchAPI(search: String) {
        if (getContinuousToken() != null) {
            if (isNotContinuousToken()) {
                isLoadMore.set(false)
                return
            }
        }
//        if (!isRefreshing.get() && !isLoadMore.get())
//            getNavigator()?.showLoading()
        val disposable: Disposable = getDataManager()
            .getExploreSearchAPI(search)
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.ui())
            .subscribe({ response ->
                if (response != null) {
                    getNavigator()?.homeFeedDataFetchSuccess(response.data)
                    getNavigator()?.log("getExploreSearchAPI response>>> ${Gson().toJson(response)}")
                    if (response.isSuccess()) {
                    } else {
                        getNavigator()?.showMessage(response.getMessage())
                    }
                }
                getNavigator()?.hideLoading()
            }, { throwable ->
                getNavigator()?.homeFeedDataFetchSuccess(null)
                getNavigator()?.hideLoading()
                getNavigator()?.handleError(throwable)
            })
        mCompositeDisposable.add(disposable)
    }
}