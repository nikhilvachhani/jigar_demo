package com.frami.ui.videoplayer.fragment

import androidx.databinding.ObservableField
import com.frami.data.DataManager
import com.frami.ui.base.BaseViewModel
import com.frami.utils.rx.SchedulerProvider
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

class ExoVideoPlayerFragmentViewModel @Inject constructor(
    dataManager: DataManager,
    schedulerProvider: SchedulerProvider,
    mCompositeDisposable: CompositeDisposable
) : BaseViewModel<ExoVideoPlayerFragmentNavigator>(
    dataManager,
    schedulerProvider,
    mCompositeDisposable
) {
    val videoUrl = ObservableField<String>("")
}