package com.frami.ui.dashboard

import android.app.Activity
import androidx.databinding.ObservableBoolean
import com.frami.R
import com.frami.data.DataManager
import com.frami.data.local.db.AppDatabase
import com.frami.data.model.common.IdNameData
import com.frami.ui.base.BaseViewModel
import com.frami.utils.AppConstants
import com.frami.utils.rx.SchedulerProvider
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

class DashboardViewModel @Inject constructor(
    dataManager: DataManager,
    schedulerProvider: SchedulerProvider,
    mCompositeDisposable: CompositeDisposable
) : BaseViewModel<DashboardNavigator>(dataManager, schedulerProvider, mCompositeDisposable) {
    var isProfileSelected = ObservableBoolean(false)
    var isBottomMenuVisible = ObservableBoolean(true)

    fun getCreateDialogList(activity: Activity): List<IdNameData> {
        val list: MutableList<IdNameData> = ArrayList()
        list.add(
            IdNameData(
                value = AppConstants.CREATE.ACTIVITY,
                icon = activity.getDrawable(R.drawable.ic_activity),
            )
        )
        list.add(
            IdNameData(
                value = AppConstants.CREATE.CHALLENGE,
                icon = activity.getDrawable(R.drawable.ic_challenge)
            )
        )
        val isCommunityAdmin =
            AppDatabase.db.userDao()
                .getById()?.userRoles?.filter { it.roleType == AppConstants.USER_ROLE.GlobalCommunityAdmin }
        if (isCommunityAdmin?.isNotEmpty() == true) {
            list.add(
                IdNameData(
                    value = AppConstants.CREATE.COMMUNITY,
                    icon = activity.getDrawable(R.drawable.ic_community)
                )
            )
        }

        list.add(
            IdNameData(
                value = AppConstants.CREATE.EVENTS,
                icon = activity.getDrawable(R.drawable.ic_event)
            )
        )
        val isRewardAdmin =
            AppDatabase.db.userDao()
                .getById()?.userRoles?.filter { it.roleType == AppConstants.USER_ROLE.PublicCommunityAdmin || it.roleType == AppConstants.USER_ROLE.GlobalCommunityAdmin }
        if (isRewardAdmin?.isNotEmpty() == true) {
            list.add(
                IdNameData(
                    value = AppConstants.CREATE.REWARD,
                    icon = activity.getDrawable(R.drawable.ic_reward_24)
                )
            )
        }
        return list
    }

    fun getCreateActivityList(activity: Activity): List<IdNameData> {
        val list: MutableList<IdNameData> = ArrayList()
        list.add(
            IdNameData(
                value = AppConstants.CREATE.MANUAL_INPUT,
                icon = activity.getDrawable(R.drawable.ic_activity),
            )
        )
        list.add(
            IdNameData(
                value = AppConstants.CREATE.RECORD_SESSION,
                icon = activity.getDrawable(R.drawable.ic_challenge)
            )
        )
        return list
    }
}