package com.frami.ui.challenges.competitors

import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import com.frami.data.DataManager
import com.frami.data.model.explore.ChallengesData
import com.frami.data.model.explore.CommunityData
import com.frami.ui.base.BaseViewModel
import com.frami.utils.rx.SchedulerProvider
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

class CompetitorFragmentViewModel @Inject constructor(
    dataManager: DataManager,
    schedulerProvider: SchedulerProvider,
    mCompositeDisposable: CompositeDisposable
) : BaseViewModel<CompetitorFragmentNavigator>(
    dataManager,
    schedulerProvider,
    mCompositeDisposable
) {
    var isLoggedInUser = ObservableBoolean(false)
    var challengesId = ObservableField<String>()
    var challengesData = ObservableField<ChallengesData>()
    var isDataEmpty = ObservableBoolean(false)
    var communityId = ObservableField<String>()
    var communityData = ObservableField<CommunityData>()
}