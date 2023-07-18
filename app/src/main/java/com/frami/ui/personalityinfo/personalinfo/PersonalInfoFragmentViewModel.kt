package com.frami.ui.personalityinfo.personalinfo

import android.net.Uri
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import com.frami.data.DataManager
import com.frami.data.model.lookup.CountryData
import com.frami.data.model.user.UserRequest
import com.frami.ui.base.BaseViewModel
import com.frami.utils.AppConstants
import com.frami.utils.CommonUtils
import com.frami.utils.rx.SchedulerProvider
import com.google.gson.Gson
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import java.io.File
import javax.inject.Inject

class PersonalInfoFragmentViewModel @Inject constructor(
    dataManager: DataManager,
    schedulerProvider: SchedulerProvider,
    mCompositeDisposable: CompositeDisposable
) : BaseViewModel<PersonalInfoFragmentNavigator>(
    dataManager,
    schedulerProvider,
    mCompositeDisposable
) {
    var isFromEdit = ObservableBoolean(false)
    var genderSelected = ObservableField<AppConstants.GENDER>(AppConstants.GENDER.MALE)
    var birthYear = ObservableField<Int>()
    var birthMonth = ObservableField<Int>()
    var birthDay = ObservableField<Int>()
    var selectedCountry = ObservableField<CountryData>()
    var selectedProfilePhoto = ObservableField<Uri>()
    var birthDate = ObservableField<String>()
    var isEnableNavigationToForward = ObservableBoolean(false)


    fun createUser(userRequest: UserRequest, profilePhoto: File) {
        CommonUtils.log("userRequest>> " + Gson().toJson(userRequest))
        getNavigator()?.showLoading()
        val disposable: Disposable = getDataManager()
            .createUser(userRequest, profilePhoto = profilePhoto)
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.ui())
            .subscribe({ response ->
                CommonUtils.log("createUser response>>> ${Gson().toJson(response)}")
                getNavigator()?.hideLoading()
                if (response.isSuccess()) {
                    updateFCMTokenAPI()
                    getNavigator()?.createOrUpdateUserSuccess(response.data)
                } else {
                    getNavigator()?.showMessage(response.getMessage())
                }
            }, { throwable ->
                getNavigator()?.hideLoading()
                getNavigator()?.handleError(throwable)
            })
        mCompositeDisposable.add(disposable)
    }

    fun createUser(userRequest: UserRequest) {
        CommonUtils.log("userRequest>> " + Gson().toJson(userRequest))
        getNavigator()?.showLoading()
        val disposable: Disposable = getDataManager()
            .createUser(userRequest)
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.ui())
            .subscribe({ response ->
                CommonUtils.log("createUser response>>> ${Gson().toJson(response)}")
                getNavigator()?.hideLoading()
                if (response.isSuccess()) {
                    updateFCMTokenAPI()
                    getNavigator()?.createOrUpdateUserSuccess(response.data)
                } else {
                    getNavigator()?.showMessage(response.getMessage())
                }
            }, { throwable ->
                getNavigator()?.hideLoading()
                getNavigator()?.handleError(throwable)
            })
        mCompositeDisposable.add(disposable)
    }

//    fun getCountryCount() {
//        val disposable: Disposable = getDataManager().getCountryCount()
//            .observeOn(schedulerProvider.ui())
//            .subscribeOn(schedulerProvider.io()) //When data empty then need to be handle below exception
//            .onErrorResumeNext { error ->
//                return@onErrorResumeNext Single.error(error)
//            }
//            .doOnError(Consumer<Throwable> { throwable ->
//                CommonUtils.log("doOnError " + throwable.message)
//                throwable.printStackTrace()
//            })
//            .doOnSuccess(Consumer<Int> { count ->
//                if (count == 0) {
//                    getCountry()
//                } else {
//                    getCountryDataFromDB()
//                }
//            })
//            .subscribe()
//        mCompositeDisposable.add(disposable)
//    }

    fun updateUser(userRequest: UserRequest, profilePhoto: File) {
        getNavigator()?.log("updateRequest>> " + Gson().toJson(userRequest))
        getNavigator()?.showLoading()
        val disposable: Disposable = getDataManager()
            .updateUser(userRequest, profilePhoto)
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.ui())
            .subscribe({ response ->
                getNavigator()?.log("updateUser response>>> ${Gson().toJson(response)}")
                getNavigator()?.hideLoading()
                if (response.isSuccess()) {
                    getNavigator()?.createOrUpdateUserSuccess(response.data)
                    if (!isFromEdit.get())
                        updateFCMTokenAPI()
                } else {
                    getNavigator()?.showMessage(response.getMessage())
                }
            }, { throwable ->
                getNavigator()?.hideLoading()
                getNavigator()?.handleError(throwable)
            })
        mCompositeDisposable.add(disposable)
    }

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
                    getNavigator()?.createOrUpdateUserSuccess(response.data)
                    if (!isFromEdit.get())
                        updateFCMTokenAPI()
                } else {
                    getNavigator()?.showMessage(response.getMessage())
                }
            }, { throwable ->
                getNavigator()?.hideLoading()
                getNavigator()?.handleError(throwable)
            })
        mCompositeDisposable.add(disposable)
    }

//    private fun getCountryDataFromDB() {
//        val disposable: Disposable = getDataManager().getCountryListFromDb()
//            .observeOn(schedulerProvider.ui())
//            .subscribeOn(schedulerProvider.io()) //When data empty then need to be handle below exception
//            .onErrorResumeNext { error ->
//                return@onErrorResumeNext Single.error(error)
//            }
//            .doOnError(Consumer<Throwable> { throwable ->
//                CommonUtils.log("doOnError " + throwable.message)
//                throwable.printStackTrace()
//            })
//            .doOnSuccess(Consumer<List<CountryData>> { countryList ->
//                if (countryList.isNotEmpty()) {
//                    getNavigator()?.countryLoadFromDatabaseSuccess(countryList)
//                }
//            })
//            .subscribe()
//        mCompositeDisposable.add(disposable)
//    }
}