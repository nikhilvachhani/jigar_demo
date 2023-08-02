package com.frami.ui.settings.preferences.contentpreference

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.viewModels
import com.frami.BR
import com.frami.R
import com.frami.data.model.content.ContentPreferenceResponseData
import com.frami.data.model.home.ActivityTypes
import com.frami.data.model.lookup.ActivityTypesOption
import com.frami.databinding.FragmentContentPreferenceBinding
import com.frami.ui.base.BaseFragment
import com.frami.ui.settings.preferences.contentpreference.adapter.ContentPreferenceListAdapter
import com.frami.utils.extensions.hide
import com.frami.utils.extensions.onClick
import com.frami.utils.extensions.visible
import com.google.gson.Gson


class ContentPreferenceFragment :
    BaseFragment<FragmentContentPreferenceBinding, ContentPreferenceFragmentViewModel>(),
    ContentPreferenceFragmentNavigator,
    ContentPreferenceListAdapter.OnItemClickListener {

    private val viewModelInstance: ContentPreferenceFragmentViewModel by viewModels {
        viewModeFactory
    }
    private var mViewBinding: FragmentContentPreferenceBinding? = null
    override fun getBindingVariable(): Int = BR.viewModel

    override fun getLayoutId(): Int = R.layout.fragment_content_preference

    override fun getViewModel(): ContentPreferenceFragmentViewModel = viewModelInstance
    private lateinit var contentPreferenceListAdapter : ContentPreferenceListAdapter

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
        contentPreferenceListAdapter = ContentPreferenceListAdapter(arrayListOf(),this)
        mViewBinding?.recyclerView?.adapter = contentPreferenceListAdapter
        getViewModel().getAactivityTypesContentPrefrencesAPI()
    }

    private fun toolbar() {
        mViewBinding!!.toolBarLayout.tvTitle.hide()
        mViewBinding!!.toolBarLayout.cvNotification.hide()
        mViewBinding!!.toolBarLayout.cvSearch.hide()

        mViewBinding?.toolBarLayout?.cvBack?.visible()
        mViewBinding?.toolBarLayout?.cvBack?.setImageResource(R.drawable.ic_back_new)
        mViewBinding?.toolBarLayout?.cvBack?.onClick { onBack() }
        mViewBinding?.toolBarLayout?.toolBar?.setNavigationOnClickListener { v -> onBack() }

        mViewBinding?.toolBarLayout?.tvSave?.visible()
        mViewBinding?.toolBarLayout?.tvSave?.setOnClickListener {
            callUpdateAPI()
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
        mNavController!!.navigateUp()
    }

    private fun clickListener() {

    }


//    override fun onItemSelect(data: ActivityTypes) {
////        getViewModel().selectedActivityTypes.set(data)
//    }
//
//    override fun onSelectedItems(data: List<ActivityTypes>) {
//        getViewModel().selectedActivityNames.set(getSelectedActivityTypeName(data))
//        getViewModel().selectedActivityTypes.set(getSelectedActivityTypeIcon(data))
//        callUpdateAPI()
//    }
//    override fun activityTypesFetchSuccessfully(list: List<ActivityTypes>) {
//        val activityTypesList = ArrayList<ActivityTypes>()
//        activityTypesList.add(getViewModel().getActivityTypeAll())
//        activityTypesList.addAll(list)
//        getViewModel().activityTypesList.set(activityTypesList)
//        getViewModel().getContentPreferenceAPI()
//    }
    override fun contentPreferenceDataFetchSuccess(data: ContentPreferenceResponseData?) {
//        getViewModel().contentPreferenceResponseData.set(data)
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

    override fun activityTypesContentPrefrencesFetchSuccessfully(data: List<ActivityTypesOption>) {
        contentPreferenceListAdapter.data = data
    }

    private fun callUpdateAPI() {
        val contentPreferenceResponseData = ContentPreferenceResponseData()
//        getViewModel().contentPreferenceResponseData.get()?.let {
//            contentPreferenceResponseData.id = it.id
//            contentPreferenceResponseData.userId = it.userId
//        }
        val list : ArrayList<ActivityTypes> = arrayListOf()
        contentPreferenceListAdapter.data.map {
            it.value.filter { it.isSelected }.also {
                if (!it.isNullOrEmpty()){
                    list.addAll(it)
                }
            }
        }

        Log.e("jigarLogs","list = "+Gson().toJson(list))
        if (list.isNotEmpty()){
//        getViewModel().updateContentPreferenceAPI(contentPreferenceResponseData)
        }

    }

    override fun onItemPress(data: ActivityTypesOption) {

    }
}