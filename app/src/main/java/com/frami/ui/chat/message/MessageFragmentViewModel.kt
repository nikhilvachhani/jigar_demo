package com.frami.ui.chat.message

import androidx.databinding.ObservableField
import com.frami.data.DataManager
import com.frami.data.model.home.ActivityData
import com.frami.data.model.home.ChatListData
import com.frami.ui.base.BaseViewModel
import com.frami.utils.AppConstants
import com.frami.utils.rx.SchedulerProvider
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

class MessageFragmentViewModel @Inject constructor(
    dataManager: DataManager,
    schedulerProvider: SchedulerProvider,
    mCompositeDisposable: CompositeDisposable
) : BaseViewModel<MessageFragmentNavigator>(
    dataManager,
    schedulerProvider,
    mCompositeDisposable
) {
    var data = ObservableField<ChatListData>()
}