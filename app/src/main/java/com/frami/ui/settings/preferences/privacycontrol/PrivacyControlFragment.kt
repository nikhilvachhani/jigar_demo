package com.frami.ui.settings.preferences.privacycontrol

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.frami.BR
import com.frami.R
import com.frami.data.model.lookup.user.SubSectionData
import com.frami.data.model.lookup.user.UserOptionsResponseData
import com.frami.data.model.settings.privacypreference.PrivacyPreferenceData
import com.frami.data.model.settings.privacypreference.SectionValuesData
import com.frami.databinding.FragmentPrivacyControlBinding
import com.frami.ui.base.BaseFragment
import com.frami.ui.settings.preferences.privacycontrol.adapter.PrivacySettingParentAdapter
import com.frami.utils.AppConstants
import com.frami.utils.extensions.hide
import com.frami.utils.extensions.onClick
import com.frami.utils.extensions.visible
import com.google.gson.Gson

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
    // default privacy data
    private var privacyPreferenceDefaultData: PrivacyPreferenceData? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            if (arguments?.containsKey(AppConstants.EXTRAS.IS_PERSONAL_INFO_COMPLETED) == true) {
                getViewModel().isFromLogin.set(true)
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
        privacySettingParentAdapter = PrivacySettingParentAdapter(ArrayList(), this)
        mViewBinding?.recyclerView?.adapter = privacySettingParentAdapter
        getViewModel().getUserPrivacyAPI()

    }

    private fun toolbar() {
        mViewBinding!!.toolBarLayout.tvTitle.hide()
        mViewBinding!!.toolBarLayout.toolBar.setNavigationOnClickListener { v -> onBack() }
        mViewBinding!!.toolBarLayout.cvNotification.hide()
        mViewBinding!!.toolBarLayout.cvSearch.hide()
        mViewBinding!!.toolBarLayout.cvBack.visible()
        mViewBinding!!.toolBarLayout.cvBack.setImageResource(R.drawable.ic_back_new)
        mViewBinding?.toolBarLayout?.cvBack?.onClick { onBack() }
        mViewBinding?.toolBarLayout?.tvSave?.onClick {
            mViewBinding?.btnNext?.performClick()
        }

        if (getViewModel().isFromLogin.get()) {
            mViewBinding?.btnNext?.visible()
            mViewBinding?.toolBarLayout?.tvSave?.hide()
        }else{
            mViewBinding?.btnNext?.hide()
            mViewBinding?.toolBarLayout?.tvSave?.visible()
        }
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
        if (getViewModel().isFromLogin.get()) {
            requireActivity().finish()
        } else {
            mNavController!!.navigateUp()
        }
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
        val privacyPreferenceResponseData = PrivacyPreferenceData()
        privacyPreferenceResponseData.id = privacyPreferenceDefaultData?.id?:""
        privacyPreferenceResponseData.userId = privacyPreferenceDefaultData?.userId?:""

        val sectionValues: ArrayList<SectionValuesData> = ArrayList()
        privacySettingParentAdapter.data.map {
            val find = it.subsectionList.find { it.isSelected }
            if (find != null){
                sectionValues.add(SectionValuesData(it.sectionKey,find.key))
            }
        }
        privacyPreferenceResponseData.sectionValues = sectionValues
        getViewModel().updatePrivacyPreferenceAPI(privacyPreferenceResponseData)
    }

    override fun userPrivacyDataFetchSuccess(data: PrivacyPreferenceData?) {
        privacyPreferenceDefaultData = data
        getViewModel().getUserOptionsAPI()
    }
    override fun userOptionsDataFetchSuccess(data: List<UserOptionsResponseData>?) {
        data?.let { it1 ->
            privacyPreferenceDefaultData?.sectionValues?.map { default ->
                val index = it1.indexOfFirst { it.sectionKey.equals(default.sectionKey,true) }
                if (index > -1){
                    val indexSub = it1[index].subsectionList.indexOfFirst { it.key.equals(default.key,true) }
                    if (indexSub > -1){
                        it1[index].subsectionList[indexSub].isSelected = true
                    }
                }
            }
            privacySettingParentAdapter.data = it1
        }
    }

    // You can do the assignment inside onAttach or onCreate, i.e, before the activity is displayed
    var wearableDeviceActivityResultLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        getViewModel().getUserLiveData().observe(
            viewLifecycleOwner,
            Observer { user ->
                if (user != null) {
                    authFlow(user, false, null, null)
                }
            })
    }

    override fun privacyPreferenceDataFetchSuccess(data: PrivacyPreferenceData?) {
        getViewModel().privacyPreferenceResponseData.set(data)
        if (getViewModel().isFromLogin.get()) {
            val user = getViewModel().user.get()
            user?.isPrivacySettingCompleted = true
            authFlow(user!!, false, wearableDeviceActivityResultLauncher, null)
        } else {
            mNavController?.navigateUp()
        }

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