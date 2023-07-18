package com.frami.ui.comingsoon

import androidx.databinding.ObservableField
import com.frami.data.DataManager
import com.frami.data.model.community.subcommunity.SubCommunityData
import com.frami.data.model.explore.CommunityData
import com.frami.data.model.explore.ViewTypes
import com.frami.ui.base.BaseViewModel
import com.frami.utils.AppConstants
import com.frami.utils.rx.SchedulerProvider
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

class ComingSoonFragmentViewModel @Inject constructor(
    dataManager: DataManager,
    schedulerProvider: SchedulerProvider,
    mCompositeDisposable: CompositeDisposable
) : BaseViewModel<ComingSoonFragmentNavigator>(
    dataManager,
    schedulerProvider,
    mCompositeDisposable
) {
    var title = ObservableField<String>("")
}