package com.frami.ui.intro.fragment

import android.app.Activity
import androidx.databinding.ObservableBoolean
import com.frami.R
import com.frami.data.DataManager
import com.frami.data.model.intro.IntroModel
import com.frami.ui.base.BaseViewModel
import com.frami.utils.rx.SchedulerProvider
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

class IntroFragmentViewModel @Inject constructor(
    dataManager: DataManager,
    schedulerProvider: SchedulerProvider,
    mCompositeDisposable: CompositeDisposable
) : BaseViewModel<IntroFragmentNavigator>(dataManager, schedulerProvider, mCompositeDisposable) {

    val isLastPosition = ObservableBoolean(false)
    fun getInfoModels(mActivity: Activity): List<IntroModel> {
        val introModelList: MutableList<IntroModel> = ArrayList<IntroModel>()
        introModelList.add(
            getInfoModel(
                mActivity.getString(R.string.intro_title_1),
                mActivity.getString(R.string.intro_text_1),
                R.drawable.ic_intro_1
            )
        )
        introModelList.add(
            getInfoModel(
                mActivity.getString(R.string.intro_title_2),
                mActivity.getString(R.string.intro_text_2),
                R.drawable.ic_intro_2
            )
        )
        introModelList.add(
            getInfoModel(
                mActivity.getString(R.string.intro_title_3),
                mActivity.getString(R.string.intro_text_3),
                R.drawable.ic_intro_3
            )
        )
        introModelList.add(
            getInfoModel(
                mActivity.getString(R.string.intro_title_4),
                mActivity.getString(R.string.intro_text_4),
                R.drawable.ic_intro_4
            )
        )
        introModelList.add(
            getInfoModel(
                mActivity.getString(R.string.intro_title_5),
                mActivity.getString(R.string.intro_text_5),
                R.drawable.ic_intro_5
            )
        )
        return introModelList
    }

    private fun getInfoModel(title: String, info: String, image: Int): IntroModel {
        return IntroModel(title, info, image)
    }
}