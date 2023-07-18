package com.frami.ui.rewards.addreward

import android.net.Uri
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import com.frami.data.DataManager
import com.frami.data.model.challenge.request.CreateChallengeRequest
import com.frami.data.model.common.IdNameData
import com.frami.data.model.lookup.challenges.ChallengesOptionsData
import com.frami.data.model.lookup.reward.RewardOptionsData
import com.frami.data.model.rewards.Organizer
import com.frami.data.model.rewards.RewardsDetails
import com.frami.data.model.rewards.add.RewardCodeData
import com.frami.ui.base.BaseViewModel
import com.frami.utils.AppConstants
import com.frami.utils.rx.SchedulerProvider
import com.google.gson.Gson
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import java.io.File
import javax.inject.Inject

class AddRewardsFragmentViewModel @Inject constructor(
    dataManager: DataManager,
    schedulerProvider: SchedulerProvider,
    mCompositeDisposable: CompositeDisposable
) : BaseViewModel<AddRewardsFragmentNavigator>(
    dataManager,
    schedulerProvider,
    mCompositeDisposable
) {

    val rewardCreatedSuccess = ObservableBoolean(false)
    val type = ObservableField<Int>(AppConstants.IS_FROM.CHALLENGE)
    val isChallengeReward = ObservableBoolean(false)
    val isUnlimitedAvailableRewards = ObservableBoolean(false)
    val isChallengeCompleted = ObservableBoolean(false)
    val isHardEnableLotteryReward = ObservableBoolean(true)
    val isLotteryReward = ObservableBoolean(false)
    var selectedRewardPhoto = ObservableField<Uri>()
    var selectedIsAddReward = ObservableField<IdNameData>()

    //    var selectedWhoCanJoin = ObservableField<IdNameData>()
    var challengesOptionsData = ObservableField<ChallengesOptionsData>()

    var createChallengeRequest = ObservableField<CreateChallengeRequest>()
    var photoList = ObservableField<List<Uri>>()
    var rewardOptionsData = ObservableField<RewardOptionsData>()

    var selectedOrganizer = ObservableField<Organizer>()

    var organizerList = ObservableField<List<Organizer>>()
    var generateOrAddRewardList = ObservableField<List<IdNameData>>()
    var selectedGenerateOrAddReward = ObservableField<IdNameData>()

    var noOfRewards = ObservableField<Int>()

    var startDateYear = ObservableField<Int>()
    var startDateMonth = ObservableField<Int>()
    var startDateDay = ObservableField<Int>()
    var expiryDate = ObservableField<String>()

    fun getRewardList(): List<RewardCodeData> {
        val list = ArrayList<RewardCodeData>()
        val number = noOfRewards.get() ?: 0
        for (i in 0 until number) {
            val selectedRewardList = rewardCodeList.get()
            if (selectedRewardList != null) {
                if (selectedRewardList.size > i) {
                    list.add(RewardCodeData(selectedRewardList[i].couponCode))
                } else {
                    list.add(RewardCodeData(""))
                }
            } else {
                list.add(RewardCodeData(""))
            }
        }
        return list
    }

    val rewardCodeList = ObservableField<List<RewardCodeData>>()

    fun createRewards(
        createChallengeRequest: HashMap<String, Any>,
        rewardPhotoList: ArrayList<File>
    ) {
        getNavigator()?.showLoading()
        getNavigator()?.log("createRewardsRequest ${createChallengeRequest}")
        val disposable: Disposable = getDataManager()
            .createRewards(createChallengeRequest, rewardPhotoList)
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.ui())
            .subscribe({ response ->
                if (response != null) {
                    getNavigator()?.log(
                        "createRewards response>>> ${
                            Gson().toJson(
                                response
                            )
                        }"
                    )
                    if (response.isSuccess()) {
                        getNavigator()?.createRewardsSuccess(response.data)
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

    fun createChallenge(
        createChallengeRequest: HashMap<String, Any>,
        photoList: ArrayList<File>,
        rewardPhotoList: ArrayList<File>
    ) {
        getNavigator()?.showLoading()
        getNavigator()?.log("createChallengeRequest ${createChallengeRequest}")
        val disposable: Disposable = getDataManager()
            .createChallenge(createChallengeRequest, photoList, rewardPhotoList)
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.ui())
            .subscribe({ response ->
                if (response != null) {
                    getNavigator()?.log(
                        "createChallenge response>>> ${
                            Gson().toJson(
                                response
                            )
                        }"
                    )
                    if (response.isSuccess()) {
                        getNavigator()?.createChallengeSuccess(response.data)
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


    fun updateRewards(
        updateRewardRequest: HashMap<String, Any>,
        rewardPhotoList: ArrayList<File>
    ) {
        getNavigator()?.showLoading()
        getNavigator()?.log("createRewardsRequest ${updateRewardRequest}")
        val disposable: Disposable = getDataManager()
            .updateRewards(updateRewardRequest, rewardPhotoList)
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.ui())
            .subscribe({ response ->
                if (response != null) {
                    getNavigator()?.log(
                        "updateRewardRequest response>>> ${
                            Gson().toJson(
                                response
                            )
                        }"
                    )
                    if (response.isSuccess()) {
                        getNavigator()?.createRewardsSuccess(response.data)
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

    //EDIT REWARDS
    val rewardsDetails = ObservableField<RewardsDetails>()
}