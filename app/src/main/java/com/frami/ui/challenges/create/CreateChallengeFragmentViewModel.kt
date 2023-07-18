package com.frami.ui.challenges.create

import android.net.Uri
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import com.frami.data.DataManager
import com.frami.data.model.challenge.request.CreateChallengeRequest
import com.frami.data.model.common.IdNameData
import com.frami.data.model.explore.ChallengesData
import com.frami.data.model.home.ActivityTypes
import com.frami.data.model.lookup.challenges.ChallengesOptionsData
import com.frami.data.model.rewards.Organizer
import com.frami.ui.base.BaseViewModel
import com.frami.utils.rx.SchedulerProvider
import com.google.gson.Gson
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import java.io.File
import javax.inject.Inject

class CreateChallengeFragmentViewModel @Inject constructor(
    dataManager: DataManager,
    schedulerProvider: SchedulerProvider,
    mCompositeDisposable: CompositeDisposable
) : BaseViewModel<CreateChallengeFragmentNavigator>(
    dataManager,
    schedulerProvider,
    mCompositeDisposable
) {
    var isFromEdit = ObservableBoolean(false)
    var challengesData = ObservableField<ChallengesData>()
    var startDateYear = ObservableField<Int>()
    var startDateMonth = ObservableField<Int>()
    var startDateDay = ObservableField<Int>()
    var endDateYear = ObservableField<Int>()
    var endDateMonth = ObservableField<Int>()
    var endDateDay = ObservableField<Int>()
    var selectedChallengePhoto = ObservableField<Uri>()
    var selectedActivityTypes = ObservableField<ActivityTypes>()
    var selectedActivityNames = ObservableField<String>("")
    var createChallengeRequest = ObservableField<CreateChallengeRequest>()
    var selectedChallengeTypes = ObservableField<IdNameData>()
    var selectedChallengeTypeCategory = ObservableField<IdNameData>()
    var selectedMinLevelCriteria = ObservableField<IdNameData>()
    var selectedMaxLevelCriteria = ObservableField<IdNameData>()
    var selectedOrganizer = ObservableField<Organizer>()
    var selectedPrivacy = ObservableField<IdNameData>()
    var isCompetitorOptionVisible = ObservableBoolean(false)
    var challengeCommunityOptionList = ObservableField<List<IdNameData>>()
    var selectedChallengeCommunityOption = ObservableField<IdNameData>()
    var selectedCompetitor = ObservableField<List<Organizer>>()
    var selectedCompetitorCommunityNames = ObservableField<String>("")
    var selectedDuration = ObservableField<IdNameData>()
    var selectedChallengeTypesTargetUnit = ObservableField<IdNameData>()

    var challengesOptionsData = ObservableField<ChallengesOptionsData>()
    var organizerList = ObservableField<List<Organizer>>()
    var competitorCommunityList = ObservableField<List<Organizer>>()
    var activityTypesList = ObservableField<List<ActivityTypes>>()
    var selectedChallengeTypesTargetUnitList = ObservableField<List<IdNameData>>()
    var photoList = ObservableField<List<Uri>>()
    var photoURIList = ObservableField<List<Uri>>()

    fun updateChallenge(
        updateChallengeRequest: HashMap<String, Any>,
        photoList: ArrayList<File>
    ) {
        getNavigator()?.showLoading()
        getNavigator()?.log("updateChallenge ${updateChallengeRequest}")
        val disposable: Disposable = getDataManager()
            .updateChallenge(updateChallengeRequest, photoList)
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.ui())
            .subscribe({ response ->
                if (response != null) {
                    getNavigator()?.log(
                        "updateChallenge response>>> ${
                            Gson().toJson(
                                response
                            )
                        }"
                    )
                    if (response.isSuccess()) {
                        getNavigator()?.updateChallengeSuccess(response.data)
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

    fun getCompetitorCommunityAPI(organizer: Organizer) {
        getNavigator()?.showLoading()
        getNavigator()?.log(
            "getCompetitorCommunityAPI request>>> ${
                Gson().toJson(organizer)
            }"
        )
        val disposable: Disposable = getDataManager()
            .getCompetitorCommunityAPI(organizer)
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.ui())
            .subscribe({ response ->
                if (response != null) {
                    getNavigator()?.log(
                        "getCompetitorCommunityAPI response>>> ${
                            Gson().toJson(
                                response
                            )
                        }"
                    )
                    getNavigator()?.competitorCommunityFetchSuccess(response.data)
                    if (response.isSuccess()) {
                    } else {
                        getNavigator()?.showMessage(response.getMessage())
                    }
                }
                getNavigator()?.hideLoading()
            }, { throwable ->
                getNavigator()?.competitorCommunityFetchSuccess(ArrayList())
                getNavigator()?.hideLoading()
                getNavigator()?.handleError(throwable)
            })
        mCompositeDisposable.add(disposable)
    }
}