package com.frami.ui.settings.help

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

class HelpFragmentViewModel @Inject constructor(
    dataManager: DataManager,
    schedulerProvider: SchedulerProvider,
    mCompositeDisposable: CompositeDisposable
) : BaseViewModel<HelpFragmentNavigator>(
    dataManager,
    schedulerProvider,
    mCompositeDisposable
) {
    fun getHelpMenu(activity: Activity): List<IdNameData> {
        val list: MutableList<IdNameData> = ArrayList()

        list.add(IdNameData(value = activity.getString(R.string.faq), isShowForward = true))
        list.add(IdNameData(value = activity.getString(R.string.about_frami),isShowForward = true))
        list.add(IdNameData(value = activity.getString(R.string.contact_us),isShowForward = true))
        list.add(IdNameData(value = activity.getString(R.string.tnc), isShowForward = true))
        list.add(IdNameData(value = activity.getString(R.string.privacy_policy),isShowForward = true))
        list.add(IdNameData(value = activity.getString(R.string.delete_account),isShowForward = true))

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