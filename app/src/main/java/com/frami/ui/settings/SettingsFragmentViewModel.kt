package com.frami.ui.settings

import android.app.Activity
import androidx.databinding.ObservableField
import com.frami.R
import com.frami.data.DataManager
import com.frami.data.model.common.IdNameData
import com.frami.data.model.profile.UserProfileData
import com.frami.ui.base.BaseViewModel
import com.frami.utils.rx.SchedulerProvider
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

class SettingsFragmentViewModel @Inject constructor(
    dataManager: DataManager,
    schedulerProvider: SchedulerProvider,
    mCompositeDisposable: CompositeDisposable
) : BaseViewModel<SettingsFragmentNavigator>(
    dataManager,
    schedulerProvider,
    mCompositeDisposable
) {
    var userProfileData = ObservableField<UserProfileData>()

    fun getSettingsMenu(activity: Activity): List<IdNameData> {
        val list: MutableList<IdNameData> = ArrayList()
        list.add(
            IdNameData(
                value = activity.getString(R.string.personal_info),
                icon = activity.getDrawable(R.drawable.ic_settings_personal_info),
            )
        )
        list.add(
            IdNameData(
                value = activity.getString(R.string.contact_info),
                icon = activity.getDrawable(R.drawable.ic_settings_contact_info),
            )
        )
        list.add(
            IdNameData(
                value = activity.getString(R.string.wearables),
                icon = activity.getDrawable(R.drawable.ic_settings_wearable),
            )
        )
        list.add(
            IdNameData(
                value = activity.getString(R.string.my_preferences),
                icon = activity.getDrawable(R.drawable.ic_settings_my_preferences),
            )
        )
        list.add(
            IdNameData(
                value = activity.getString(R.string.help),
                icon = activity.getDrawable(R.drawable.ic_settings_help),
            )
        )
        return list
    }
}