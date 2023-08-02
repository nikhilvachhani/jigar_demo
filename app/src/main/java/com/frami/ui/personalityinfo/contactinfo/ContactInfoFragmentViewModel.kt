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
    var isValidWorkEmail = ObservableBoolean(false)
    var isWorkEmailEdited = ObservableBoolean(false)

    var isValidatingEmail = ObservableBoolean(false)
    var isValidatedEmail = ObservableBoolean(false)
    var isTextHideEmail = ObservableBoolean(false)

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
    fun updateWorkMail(emailId: String) {
        getNavigator()?.log(
            "UpdateWorkMail request>>> $emailId"
        )
        isTextHideEmail.set(true)
        isValidatingEmail.set(true)
        isValidatedEmail.set(false)
        val disposable: Disposable = getDataManager()
            .updateWorkMail(emailId)
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
                        isValidatingEmail.set(false)
                        isValidatedEmail.set(true)
                        getNavigator()?.verificationEmailSentSuccess(true, emailId)
                    } else {
                        isValidatingEmail.set(false)
                        isTextHideEmail.set(false)
                        isValidatedEmail.set(true)
                    }
                    getNavigator()?.showMessage(response.getMessage())
                }
            }, { throwable ->
                isValidatingEmail.set(false)
                isTextHideEmail.set(false)
                isValidatedEmail.set(true)
                getNavigator()?.handleError(throwable)
            })
        mCompositeDisposable.add(disposable)
    }

    fun joinCommunityByCodeAPI(code: String) {
        getNavigator()?.showLoading()
        val disposable: Disposable = getDataManager().joinCommunityByCodeAPI(code = code)
            .subscribeOn(schedulerProvider.io()).observeOn(schedulerProvider.ui())
            .subscribe({ response ->
                getNavigator()?.hideLoading()
                if (response != null) {
                    getNavigator()?.log(
                        "joinCommunityByCodeAPI response>>> ${
                            Gson().toJson(
                                response
                            )
                        }"
                    )
                    if (response.isSuccess()) {
                        getNavigator()?.communityJoinByCode(response.data)
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