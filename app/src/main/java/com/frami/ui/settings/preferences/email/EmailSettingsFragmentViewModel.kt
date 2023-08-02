package com.frami.ui.settings.preferences.email

import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import com.frami.data.DataManager
import com.frami.data.model.profile.contactinfo.VerificationEmailRequest
import com.frami.data.model.settings.EmailSettingRequest
import com.frami.data.model.settings.pushnotificationmenu.mainmenu.PushNotificationMenuData
import com.frami.data.model.user.UserRequest
import com.frami.ui.base.BaseViewModel
import com.frami.utils.rx.SchedulerProvider
import com.google.gson.Gson
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import javax.inject.Inject

class EmailSettingsFragmentViewModel @Inject constructor(
    dataManager: DataManager,
    schedulerProvider: SchedulerProvider,
    mCompositeDisposable: CompositeDisposable
) : BaseViewModel<EmailSettingsFragmentNavigator>(
    dataManager,
    schedulerProvider,
    mCompositeDisposable
) {

    var isValidEmail = ObservableBoolean(false)
    var isEmailEdited = ObservableBoolean(false)
    val pushNotificationMenuData = ObservableField<PushNotificationMenuData>()

    fun updateEMailSetting(request: EmailSettingRequest) {
        getNavigator()?.showLoading()
        val disposable: Disposable = getDataManager()
            .updateEMailSetting(request)
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.ui())
            .subscribe({ response ->
                getNavigator()?.hideLoading()
                if (response != null) {
                    getNavigator()?.log(
                        "verifyEmail response>>> ${
                            Gson().toJson(
                                response
                            )
                        }"
                    )
                    if (response.isSuccess()) {
                        getNavigator()?.updateUserSuccess(response.data)
                    } else {
                    }
                    getNavigator()?.showMessage(response.getMessage())
                }
            }, { throwable ->
                getNavigator()?.hideLoading()
                getNavigator()?.handleError(throwable)
            })
        mCompositeDisposable.add(disposable)
    }

}