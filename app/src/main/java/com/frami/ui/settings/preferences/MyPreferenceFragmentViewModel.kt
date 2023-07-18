package com.frami.ui.settings.preferences

import android.app.Activity
import com.frami.R
import com.frami.data.DataManager
import com.frami.data.model.common.IdNameData
import com.frami.ui.base.BaseViewModel
import com.frami.utils.CommonUtils
import com.frami.utils.rx.SchedulerProvider
import com.google.gson.Gson
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import javax.inject.Inject

class MyPreferenceFragmentViewModel @Inject constructor(
    dataManager: DataManager,
    schedulerProvider: SchedulerProvider,
    mCompositeDisposable: CompositeDisposable
) : BaseViewModel<MyPreferenceFragmentNavigator>(
    dataManager,
    schedulerProvider,
    mCompositeDisposable
) {
    fun getMyPreferenceMenu(activity: Activity): List<IdNameData> {
        val list: MutableList<IdNameData> = ArrayList()
        list.add(
            IdNameData(
                value = activity.getString(R.string.content),
                isShowForward = true
            )
        )
        list.add(
            IdNameData(
                value = activity.getString(R.string.notifications),
                isShowForward = true
            )
        )
//        list.add(
//            IdNameData(
//                value = activity.getString(R.string.change_password),
//                isShowForward = true
//            )
//        )
        list.add(
            IdNameData(
                value = activity.getString(R.string.privacy_controls),
                isShowForward = true
            )
        )
        list.add(
            IdNameData(
                value = activity.getString(R.string.delete_account),
                isShowForward = true
            )
        )
//        list.add(
//            IdNameData(
//                value = activity.getString(R.string.deactivate_account),
//                isShowForward = true
//            )
//        )
        return list
    }

    fun deleteUser(userId: String) {
        getNavigator()?.showLoading()
        val disposable: Disposable = getDataManager()
            .deleteUser(userId)
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.ui())
            .subscribe({ response ->
                CommonUtils.log("createUser response>>> ${Gson().toJson(response)}")
                getNavigator()?.hideLoading()
                if (response.isSuccess()) {
                    getNavigator()?.deleteUserSuccess()
                } else {
                    getNavigator()?.showMessage(response.getMessage())
                }
            }, { throwable ->
                getNavigator()?.hideLoading()
                getNavigator()?.deleteUserSuccess()
                getNavigator()?.handleError(throwable)
            })
        mCompositeDisposable.add(disposable)
    }
}