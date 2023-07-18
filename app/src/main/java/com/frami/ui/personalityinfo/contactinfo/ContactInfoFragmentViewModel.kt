package com.frami.ui.personalityinfo.contactinfo

import androidx.databinding.ObservableBoolean
import com.frami.data.DataManager
import com.frami.data.model.profile.contactinfo.VerificationEmailRequest
import com.frami.data.model.user.UserRequest
import com.frami.ui.base.BaseViewModel
import com.frami.utils.rx.SchedulerProvider
import com.google.gson.Gson
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import javax.inject.Inject

class ContactInfoFragmentViewModel @Inject constructor(
    dataManager: DataManager,
    schedulerProvider: SchedulerProvider,
    mCompositeDisposable: CompositeDisposable
) : BaseViewModel<ContactInfoFragmentNavigator>(
    dataManager,
    schedulerProvider,
    mCompositeDisposable
) {
    var isFromEdit = ObservableBoolean(false)
    var isValidEmail = ObservableBoolean(false)
    var isValidWorkEmail = ObservableBoolean(false)
    var isEnableNavigationToForward = ObservableBoolean(false)
    var isEmailEdited = ObservableBoolean(false)
    var isWorkEmailEdited = ObservableBoolean(false)

    fun updateUser(userRequest: UserRequest) {
        getNavigator()?.log("updateRequest>> " + Gson().toJson(userRequest))
        getNavigator()?.showLoading()
        val disposable: Disposable = getDataManager()
            .updateUser(userRequest)
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.ui())
            .subscribe({ response ->
                getNavigator()?.log("updateUser response>>> ${Gson().toJson(response)}")
                getNavigator()?.hideLoading()
                if (response.isSuccess()) {
                    getNavigator()?.updateUserSuccess(response.data)
                } else {
                    getNavigator()?.showMessage(response.getMessage())
                }
            }, { throwable ->
                getNavigator()?.hideLoading()
                getNavigator()?.handleError(throwable)
            })
        mCompositeDisposable.add(disposable)
    }

    fun verifyEmail(isWorkEmail: Boolean, emailId: String) {
        val verificationEmailRequest = VerificationEmailRequest()
        verificationEmailRequest.userId = getUserId()
        user.get().let {
            verificationEmailRequest.userName = it?.userName.toString()
            verificationEmailRequest.emailAddress = emailId
            //if (isWorkEmail) it?.workEmailAddress.toString() else it?.emailAddress.toString()
        }
        verificationEmailRequest.isWorkEmail = isWorkEmail

        getNavigator()?.showLoading()
        getNavigator()?.log(
            "verifyEmail request>>> ${
                Gson().toJson(
                    verificationEmailRequest
                )
            }"
        )
        val disposable: Disposable = getDataManager()
            .verifyEmail(verificationEmailRequest)
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.ui())
            .subscribe({ response ->
                if (response != null) {
                    getNavigator()?.log(
                        "verifyEmail response>>> ${
                            Gson().toJson(
                                response
                            )
                        }"
                    )
                    if (response.isSuccess()) {
                        getNavigator()?.verificationEmailSentSuccess(isWorkEmail, emailId)
                    } else {
                    }
                    getNavigator()?.showMessage(response.getMessage())
                }
                getNavigator()?.hideLoading()
            }, { throwable ->
                getNavigator()?.handleError(throwable)
                getNavigator()?.hideLoading()
            })
        mCompositeDisposable.add(disposable)
    }
}