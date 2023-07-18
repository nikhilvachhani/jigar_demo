package com.frami.ui.settings.help

import android.app.Activity
import com.frami.R
import com.frami.data.DataManager
import com.frami.data.model.common.IdNameData
import com.frami.ui.base.BaseViewModel
import com.frami.utils.rx.SchedulerProvider
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

class HelpFragmentViewModel @Inject constructor(
    dataManager: DataManager,
    schedulerProvider: SchedulerProvider,
    mCompositeDisposable: CompositeDisposable
) : BaseViewModel<HelpFragmentNavigator>(
    dataManager,
    schedulerProvider,
    mCompositeDisposable
) {
    fun getHelpMenu(activity: Activity): List<IdNameData> {
        val list: MutableList<IdNameData> = ArrayList()
        list.add(
            IdNameData(
                value = activity.getString(R.string.about_us),
                isShowForward = true
            )
        )
        list.add(IdNameData(value = activity.getString(R.string.faq), isShowForward = true))
        list.add(IdNameData(value = activity.getString(R.string.tnc), isShowForward = true))
        list.add(
            IdNameData(
                value = activity.getString(R.string.privacy_policy),
                isShowForward = true
            )
        )
        list.add(
            IdNameData(
                value = activity.getString(R.string.contact_us),
                isShowForward = true
            )
        )
        return list
    }
}