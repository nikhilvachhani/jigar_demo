package com.frami.ui.settings.preferences.privacycontrol

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.viewModels
import com.frami.BR
import com.frami.R
import com.frami.data.model.home.ActivityTypes
import com.frami.data.model.lookup.user.SubSectionData
import com.frami.data.model.lookup.user.UserOptionsResponseData
import com.frami.data.model.privacycontrol.PrivacyControlData
import com.frami.data.model.settings.privacypreference.PrivacyPreferenceResponseData
import com.frami.data.model.settings.privacypreference.SectionValuesData
import com.frami.data.model.settings.pushnotificationmenu.notificationdetails.PushNotificationsOnResponseData
import com.frami.databinding.FragmentPrivacyControlBinding
import com.frami.ui.base.BaseFragment
import com.frami.ui.common.SelectActivityTypesDialog
import com.frami.ui.common.SelectPrivacyControlDialog
import com.frami.ui.settings.preferences.notificationpreference.adapter.PushNotificationParentAdapter
import com.frami.ui.settings.preferences.privacycontrol.adapter.PrivacySettingParentAdapter
import com.frami.utils.AppConstants
import com.frami.utils.extensions.hide
import com.frami.utils.extensions.onClick
import com.frami.utils.extensions.visible

class PrivacyControlFragment :
    BaseFragment<FragmentPrivacyControlBinding, PrivacyControlFragmentViewModel>(),
    PrivacyControlFragmentNavigator,
    PrivacySettingParentAdapter.OnItemClickListener {

    private val viewModelInstance: PrivacyControlFragmentViewModel by viewModels {
        viewModeFactory
    }
    private var mViewBinding: FragmentPrivacyControlBinding? = null
    override fun getBindingVariable(): Int = BR.viewModel

    override fun getLayoutId(): Int = R.layout.fragment_privacy_control

    override fun getViewModel(): PrivacyControlFragmentViewModel = viewModelInstance

    private lateinit var privacySettingParentAdapter: PrivacySettingParentAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            if (arguments?.containsKey(AppConstants.FROM.LOGIN) == true) {
                getViewModel().isFromLogin.set(arguments?.getBoolean(AppConstants.FROM.LOGIN)!!)
            }
        }
    }

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
        privacySettingParentAdapter = PrivacySettingParentAdapter(ArrayList(), this)
        mViewBinding?.recyclerView?.adapter = privacySettingParentAdapter
        getViewModel().getUserOptionsAPI()
    }

    private fun toolbar() {
        mViewBinding!!.toolBarLayout.tvTitle.hide()
        mViewBinding!!.toolBarLayout.toolBar.setNavigationOnClickListener { v -> onBack() }
        mViewBinding!!.toolBarLayout.cvNotification.hide()
        mViewBinding!!.toolBarLayout.cvSearch.hide()
        mViewBinding!!.toolBarLayout.cvBack.visible()
        mViewBinding!!.toolBarLayout.cvBack.setImageResource(R.drawable.ic_back_new)
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
        mViewBinding?.btnNext?.onClick {
            var isValid = true
            privacySettingParentAdapter.data.map {
                val find = it.subsectionList.find { it.isSelected }
                if (find == null){
                    isValid = false
                    showMessage("please select ${it.sectionTitle}")
                }
            }
            if (isValid){
                callUpdateAPI()
            }
        }
    }


    private fun callUpdateAPI() {
        val user = getViewModel().user.get()

        val privacyPreferenceResponseData = PrivacyPreferenceResponseData()
        privacyPreferenceResponseData.userId = user?.userId?:""
//        getViewModel().privacyPreferenceResponseData.get().let {
//            privacyPreferenceResponseData.id = it?.id!!
//            privacyPreferenceResponseData.userId = it.userId
//        }

        val sectionValues: ArrayList<SectionValuesData> = ArrayList()
        privacySettingParentAdapter.data.map {
            val find = it.subsectionList.find { it.isSelected }
            if (find != null){
                Log.e("jigarLogs","selected key = "+find.key)
                sectionValues.add(SectionValuesData(it.sectionKey,find.key))
            }
        }
        privacyPreferenceResponseData.sectionValues = sectionValues
        getViewModel().updatePrivacyPreferenceAPI(privacyPreferenceResponseData)
    }

    override fun userOptionsDataFetchSuccess(data: List<UserOptionsResponseData>?) {
        data?.let {
            privacySettingParentAdapter.data = it
        }
    }

//    override fun privacyPreferenceDataFetchSuccess(data: PrivacyPreferenceResponseData?) {
//
//    }

    override fun privacyPreferenceDataFetchSuccess(data: PrivacyPreferenceResponseData?) {
        getViewModel().privacyPreferenceResponseData.set(data)

//        val profilePrivacyList = ArrayList<PrivacyControlData>()
//        getViewModel().profilePagePrivacyList.get()?.forEachIndexed { index, idNameData ->
//            if (idNameData.value == data?.profile) {
//                idNameData.isSelected = true
//                getViewModel().selectedProfilePagePrivacy.set(idNameData)
//            }
//            profilePrivacyList.add(idNameData)
//        }
//        getViewModel().profilePagePrivacyList.set(profilePrivacyList)
//
//        val activityPrivacyList = ArrayList<PrivacyControlData>()
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
    override fun onUpdatePreferenceItem(parentPosition: Int,data: List<SubSectionData>,adapterPosition: Int) {
        privacySettingParentAdapter.notifySubItem(parentPosition,adapterPosition,true)
    }
}