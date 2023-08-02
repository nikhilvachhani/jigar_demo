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
        with(list) {
            add(IdNameData(value = activity.getString(R.string.personal_info),icon = activity.getDrawable(R.drawable.ic_settings_personal_info), isShowForward = true))
            add(IdNameData(value = activity.getString(R.string.employer),icon = activity.getDrawable(R.drawable.ic_settings_employer), isShowForward = true))
            add(IdNameData(value = activity.getString(R.string.privacy),icon = activity.getDrawable(R.drawable.ic_settings_privacy), isShowForward = true))
            add(IdNameData(value = activity.getString(R.string.device_and_applications),icon = activity.getDrawable(R.drawable.ic_settings_wearable), isShowForward = true))
            add(IdNameData(value = activity.getString(R.string.notifications),icon = activity.getDrawable(R.drawable.ic_settings_notification), isShowForward = true))
            add(IdNameData(value = activity.getString(R.string.content_preferences),icon = activity.getDrawable(R.drawable.ic_settings_my_preferences), isShowForward = true))
            add(IdNameData(value = activity.getString(R.string.help),icon = activity.getDrawable(R.drawable.ic_settings_help), isShowForward = true))
        }
        return list
    }
}