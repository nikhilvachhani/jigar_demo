package com.frami.ui.loginsignup.option

import com.frami.data.DataManager
import com.frami.ui.base.BaseViewModel
import com.frami.utils.CommonUtils
import com.frami.utils.rx.SchedulerProvider
import com.google.gson.Gson
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import javax.inject.Inject

class LoginOptionFragmentViewModel @Inject constructor(
    dataManager: DataManager,
    schedulerProvider: SchedulerProvider,
    mCompositeDisposable: CompositeDisposable
) : BaseViewModel<LoginOptionFragmentNavigator>(
    dataManager,
    schedulerProvider,
    mCompositeDisposable
) {

    fun validateUser() {
        getNavigator()?.showLoading()
        val disposable: Disposable = getDataManager()
            .validateUser()
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.ui())
            .subscribe({ response ->
                CommonUtils.log("validateUser response>>> " + Gson().toJson(response))
                getNavigator()?.hideLoading()
                if (response.isSuccess()) {
                    updateFCMTokenAPI()
                    getNavigator()?.validateSuccess(response.data)
                } else {
                    getNavigator()?.showMessage(response.getMessage())
                    setAccessToken("")
                }
            }, { throwable ->
                getNavigator()?.hideLoading()
                getNavigator()?.handleError(throwable)
                setAccessToken("")
            })
        mCompositeDisposable.add(disposable)
    }

}