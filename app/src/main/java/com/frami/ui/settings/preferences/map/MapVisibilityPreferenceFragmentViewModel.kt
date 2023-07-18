package com.frami.ui.settings.preferences.map

import android.app.Activity
import com.frami.R
import com.frami.data.DataManager
import com.frami.data.model.common.IdNameData
import com.frami.ui.base.BaseViewModel
import com.frami.utils.rx.SchedulerProvider
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

class MapVisibilityPreferenceFragmentViewModel @Inject constructor(
    dataManager: DataManager,
    schedulerProvider: SchedulerProvider,
    mCompositeDisposable: CompositeDisposable
) : BaseViewModel<MapVisibilityPreferenceFragmentNavigator>(
    dataManager,
    schedulerProvider,
    mCompositeDisposable
) {
    fun getMapVisibilityPreferenceMenu(activity: Activity): List<IdNameData> {
        val list: MutableList<IdNameData> = ArrayList()
        list.add(
            IdNameData(
                value = activity.getString(R.string.map_visibility_option_1),
                isShowForward = true
            )
        )
        list.add(
            IdNameData(
                value = activity.getString(R.string.map_visibility_option_2),
                isShowForward = true
            )
        )
        return list
    }
}