package com.frami.ui.settings.preferences.privacycontrol

import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.viewModels
import com.frami.BR
import com.frami.R
import com.frami.data.model.common.IdNameData
import com.frami.data.model.home.ActivityTypes
import com.frami.data.model.lookup.user.UserOptionsResponseData
import com.frami.data.model.settings.privacypreference.PrivacyPreferenceResponseData
import com.frami.databinding.FragmentPrivacyControlBinding
import com.frami.ui.base.BaseFragment
import com.frami.ui.common.SelectActivityTypesDialog
import com.frami.ui.common.SelectIdNameDialog
import com.frami.utils.AppConstants
import com.frami.utils.extensions.hide
import com.frami.utils.extensions.visible


class OldPrivacyControlFragment :
    BaseFragment<FragmentPrivacyControlBinding, PrivacyControlFragmentViewModel>(),
    PrivacyControlFragmentNavigator, SelectActivityTypesDialog.OnDialogActionListener,
    SelectIdNameDialog.OnDialogActionListener {

    private val viewModelInstance: PrivacyControlFragmentViewModel by viewModels {
        viewModeFactory
    }
    private var mViewBinding: FragmentPrivacyControlBinding? = null
    override fun getBindingVariable(): Int = BR.viewModel

    override fun getLayoutId(): Int = R.layout.fragment_privacy_control

    override fun getViewModel(): PrivacyControlFragmentViewModel = viewModelInstance

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setStatusBarColor(R.color.lightBg)
        mViewBinding = getViewDataBinding()
        viewModelInstance.setNavigator(this)

        toolbar()
        handleBackPress()
        clickListener()
        init()
    }

    private fun init() {
//        if (getViewModel().getActivityTypeList().isNotEmpty()) {
//            getViewModel().selectedActivityTypes.set(
//                getViewModel().getActivityTypeList()[0]
//            )
//        }
        getViewModel().getUserOptionsAPI()
    }

    private fun toolbar() {
        mViewBinding!!.toolBarLayout.tvTitle.hide()
        mViewBinding!!.toolBarLayout.toolBar.setNavigationOnClickListener { v -> onBack() }
        mViewBinding!!.toolBarLayout.cvNotification.hide()
        mViewBinding!!.toolBarLayout.cvSearch.hide()
        mViewBinding!!.toolBarLayout.cvBack.visible()
        mViewBinding!!.toolBarLayout.cvBack.setOnClickListener { onBack() }
    }

    private fun handleBackPress() {
        val callback: OnBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                onBack()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)
    }

    override fun onBack() {
        mNavController!!.navigateUp()
    }

    private fun clickListener() {
//        mViewBinding!!.clProfile.setOnClickListener { showProfilePrivacyDialog() }
//        mViewBinding!!.clActivities.setOnClickListener { showActivitiesPrivacyDialog() }
//        mViewBinding!!.clActivityType.setOnClickListener { showActivityTypeDialog() }
//        mViewBinding!!.clMap.setOnClickListener {
//            mNavController!!.navigate(R.id.toMapVisibilityPreferenceFragment)
//        }
    }

//    private fun showProfilePrivacyDialog() {
//        val dialog =
//            getViewModel().profilePagePrivacyList.get()?.let {
//                it.forEach { it1 ->
//                    it1.isSelected = (it1.key === getViewModel().selectedProfilePagePrivacy.get()?.key)
//                }
//                SelectIdNameDialog(
//                    requireActivity(),
//                    it,
//                    forWhom = AppConstants.FROM.PRIVACY_CONTROL_PROFILE,
//                    getString(R.string.hint_control_profile)
//                )
//            }
//        dialog?.setListener(this)
//        dialog?.show()
//    }
//
//    private fun showActivitiesPrivacyDialog() {
//        val dialog =
//            getViewModel().activityPrivacyList.get()?.let {
//                it.forEach { it1 ->
//                    it1.isSelected =
//                        (it1.key === getViewModel().selectedActivitiesPrivacy.get()?.key)
//                }
//                SelectIdNameDialog(
//                    requireActivity(),
//                    it,
//                    forWhom = AppConstants.FROM.PRIVACY_CONTROL_ACTIVITIES,
//                    getString(R.string.hint_control_activity)
//                )
//            }
//        dialog?.setListener(this)
//        dialog?.show()
//    }
//
//    private fun showMapPrivacyDialog() {
//        val dialog =
//            getViewModel().activityPrivacyList.get()?.let {
//                SelectIdNameDialog(
//                    requireActivity(),
//                    it,
//                    forWhom = AppConstants.FROM.PRIVACY_CONTROL_MAP
//                )
//            }
//        dialog?.setListener(this)
//        dialog?.show()
//    }
//
//    private fun showActivityTypeDialog() {
//        val dialog =
//            getViewModel().activityTypesList.get()?.let {
//                SelectActivityTypesDialog(
//                    requireActivity(),
//                    it,
//                    true
//                )
//            }
//        dialog?.setListener(this)
//        dialog?.show()
//    }

    override fun onItemSelect(data: ActivityTypes) {
    }

    override fun onSelectedItems(data: List<ActivityTypes>) {
        getViewModel().activityTypesList.set(data)
        getViewModel().selectedActivityNames.set(getSelectedActivityTypeName(data))
        getViewModel().selectedActivityTypes.set(getSelectedActivityTypeIcon(data))
        callUpdateAPI()
    }

    override fun onItemSelect(data: IdNameData, forWhom: String) {
//        when (forWhom) {
//            AppConstants.FROM.PRIVACY_CONTROL_PROFILE -> {
//                getViewModel().selectedProfilePagePrivacy.set(data).apply {
//                    callUpdateAPI()
//                }
//            }
//            AppConstants.FROM.PRIVACY_CONTROL_ACTIVITIES -> {
//                getViewModel().selectedActivitiesPrivacy.set(data).apply {
//                    callUpdateAPI()
//                }
//            }
//        }
    }

    private fun callUpdateAPI() {
        val privacyPreferenceResponseData = PrivacyPreferenceResponseData()
        getViewModel().privacyPreferenceResponseData.get().let {
            privacyPreferenceResponseData.id = it?.id!!
            privacyPreferenceResponseData.userId = it.userId
        }
        privacyPreferenceResponseData.profile =
            if (getViewModel().selectedProfilePagePrivacy.get() != null) getViewModel().selectedProfilePagePrivacy.get()?.value!! else ""
        privacyPreferenceResponseData.activity =
            if (getViewModel().selectedActivitiesPrivacy.get() != null) getViewModel().selectedActivitiesPrivacy.get()?.value!! else ""
        val selectedActivityTypeList =
            getViewModel().activityTypesList.get()?.filter { it.isSelected }
        if (selectedActivityTypeList != null) {
            privacyPreferenceResponseData.activityType = selectedActivityTypeList
        }
        getViewModel().updatePrivacyPreferenceAPI(privacyPreferenceResponseData)
    }

    override fun userOptionsDataFetchSuccess(data: UserOptionsResponseData?) {
        getViewModel().profilePagePrivacyList.set(data?.profileList)
        getViewModel().activityPrivacyList.set(data?.activityList)
        getViewModel().getActivityTypesAPI()
    }

    override fun activityTypesFetchSuccessfully(list: List<ActivityTypes>) {
        val activityTypesList = ArrayList<ActivityTypes>()
        activityTypesList.add(getViewModel().getActivityTypeAll())
        activityTypesList.addAll(list)
        getViewModel().activityTypesList.set(activityTypesList)
        getViewModel().getPrivacyPreferenceAPI()
    }

    override fun privacyPreferenceDataFetchSuccess(data: PrivacyPreferenceResponseData?) {
//        getViewModel().privacyPreferenceResponseData.set(data)
//        val profilePrivacyList = ArrayList<IdNameData>()
//        getViewModel().profilePagePrivacyList.get()?.forEachIndexed { index, idNameData ->
//            if (idNameData.value == data?.profile) {
//                idNameData.isSelected = true
//                getViewModel().selectedProfilePagePrivacy.set(idNameData)
//            }
//            profilePrivacyList.add(idNameData)
//        }
//        getViewModel().profilePagePrivacyList.set(profilePrivacyList)
//
//        val activityPrivacyList = ArrayList<IdNameData>()
//        getViewModel().activityPrivacyList.get()?.forEachIndexed { index, idNameData ->
//            if (idNameData.value == data?.activity) {
//                idNameData.isSelected = true
//                getViewModel().selectedActivitiesPrivacy.set(idNameData)
//            }
//            activityPrivacyList.add(idNameData)
//        }
//        getViewModel().activityPrivacyList.set(activityPrivacyList)
//
//        val activityTypeList = ArrayList<ActivityTypes>()
//        getViewModel().activityTypesList.get()?.forEachIndexed { index, activityTypes ->
//            val filter = data?.activityType?.find { it.key == activityTypes.key }
//            activityTypes.isSelected = filter != null
//            activityTypeList.add(activityTypes)
//        }.apply {
//            getViewModel().activityTypesList.set(activityTypeList)
//            getViewModel().selectedActivityNames.set(getSelectedActivityTypeName(activityTypeList))
//            getViewModel().selectedActivityTypes.set(getSelectedActivityTypeIcon(activityTypeList))
//        }
    }
}