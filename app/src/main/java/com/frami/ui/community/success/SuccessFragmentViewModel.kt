package com.frami.ui.community.success

import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import com.frami.data.DataManager
import com.frami.ui.base.BaseViewModel
import com.frami.utils.rx.SchedulerProvider
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

class SuccessFragmentViewModel @Inject constructor(
    dataManager: DataManager,
    schedulerProvider: SchedulerProvider,
    mCompositeDisposable: CompositeDisposable
) : BaseViewModel<SuccessFragmentNavigator>(
    dataManager,
    schedulerProvider,
    mCompositeDisposable
) {
//    var isFromDeleteActivity = ObservableBoolean(false)
    var trackActivity = ObservableBoolean(false)
    var activityId = ObservableField<String>()
    var challengeId = ObservableField<String>()
    var eventId = ObservableField<String>()
    var communityId = ObservableField<String>()
    var subCommunityId = ObservableField<String>()
    var isChildSubCommunity = ObservableBoolean(false)
    var rewardId = ObservableField<String>()
}