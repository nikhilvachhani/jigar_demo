package com.frami.ui.events.details

import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import com.frami.data.DataManager
import com.frami.data.model.activity.comment.CommentData
import com.frami.data.model.explore.EventsData
import com.frami.ui.base.BaseViewModel
import com.frami.utils.rx.SchedulerProvider
import com.google.gson.Gson
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import javax.inject.Inject

class EventDetailsFragmentViewModel @Inject constructor(
    dataManager: DataManager,
    schedulerProvider: SchedulerProvider,
    mCompositeDisposable: CompositeDisposable
) : BaseViewModel<EventDetailsFragmentNavigator>(
    dataManager, schedulerProvider, mCompositeDisposable
) {
    var eventsData = ObservableField<EventsData>()
    var isAbleToGoBack = ObservableBoolean(true)
    var eventId = ObservableField<String>()
    var isRefreshing = ObservableBoolean(false)
    var postType = ObservableField<String>()
    var postId = ObservableField<String>()
    var screenType = ObservableField<String>()
    var commentData = ObservableField<CommentData>()

    fun getEventDetailsAPI() {
        if (!isRefreshing.get()) getNavigator()?.showLoading()
        val disposable: Disposable =
            getDataManager().getEventDetailsAPI(eventId = eventId.get() ?: "")
                .subscribeOn(schedulerProvider.io()).observeOn(schedulerProvider.ui())
                .subscribe({ response ->
                    if (response != null) {
                        getNavigator()?.log(
                            "getEventDetailsAPI response>>> ${
                                Gson().toJson(
                                    response
                                )
                            }"
                        )
                        if (response.isSuccess()) {
                            getNavigator()?.eventDetailsFetchSuccess(response.data)
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

    fun deleteEvent() {
        getNavigator()?.showLoading()
        val disposable: Disposable = getDataManager().deleteEvent(eventId = eventId.get() ?: "")
            .subscribeOn(schedulerProvider.io()).observeOn(schedulerProvider.ui())
            .subscribe({ response ->
                getNavigator()?.hideLoading()
                if (response != null) {
                    getNavigator()?.log(
                        "eventDeleteSuccess response>>> ${
                            Gson().toJson(
                                response
                            )
                        }"
                    )
                    if (response.isSuccess()) {
                        getNavigator()?.eventDeleteSuccess(response.getMessage())
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

    fun unJoinEvent() {
        getNavigator()?.showLoading()
        val disposable: Disposable = getDataManager().unJoinEventAPI(eventId = eventId.get() ?: "")
            .subscribeOn(schedulerProvider.io()).observeOn(schedulerProvider.ui())
            .subscribe({ response ->
                getNavigator()?.hideLoading()
                if (response != null) {
                    getNavigator()?.log(
                        "unJoinEvent response>>> ${
                            Gson().toJson(
                                response
                            )
                        }"
                    )
                    if (response.isSuccess()) {
                        getEventDetailsAPI()
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