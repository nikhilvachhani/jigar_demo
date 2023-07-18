package com.frami.ui.community.create

import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.TextView.OnEditorActionListener
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.viewModels
import com.frami.BR
import com.frami.R
import com.frami.data.local.db.AppDatabase
import com.frami.data.model.common.IdNameData
import com.frami.data.model.community.request.ApplicableCodeData
import com.frami.data.model.community.request.ApplicableCodeRequest
import com.frami.data.model.community.request.CreateCommunityRequest
import com.frami.data.model.community.request.DomainData
import com.frami.data.model.explore.CommunityData
import com.frami.data.model.home.ActivityTypes
import com.frami.data.model.lookup.community.CommunityOptionsData
import com.frami.databinding.FragmentCreateCommunityStep2Binding
import com.frami.ui.base.BaseFragment
import com.frami.ui.common.SelectActivityTypesDialog
import com.frami.ui.common.SelectIdNameDialog
import com.frami.ui.community.create.adapter.ApplicableCodeAdapter
import com.frami.ui.community.create.adapter.DomainAdapter
import com.frami.utils.AppConstants
import com.frami.utils.CommonUtils
import com.frami.utils.extensions.hide
import com.frami.utils.extensions.visible
import java.io.File


class CreateCommunityStep2Fragment :
    BaseFragment<FragmentCreateCommunityStep2Binding, CreateCommunityFragmentViewModel>(),
    CreateCommunityFragmentNavigator,
    SelectActivityTypesDialog.OnDialogActionListener,
    SelectIdNameDialog.OnDialogActionListener, DomainAdapter.OnItemClickListener,
    ApplicableCodeAdapter.OnItemClickListener {

    private val viewModelInstance: CreateCommunityFragmentViewModel by viewModels {
        viewModeFactory
    }
    private var mViewBinding: FragmentCreateCommunityStep2Binding? = null
    override fun getBindingVariable(): Int = BR.ccViewModel

    override fun getLayoutId(): Int = R.layout.fragment_create_community_step2

    override fun getViewModel(): CreateCommunityFragmentViewModel = viewModelInstance

    private lateinit var domainAdapter: DomainAdapter
    private lateinit var applicableCodeAdapter: ApplicableCodeAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            if (arguments?.containsKey(AppConstants.EXTRAS.CREATE_REQUEST) == true) {
                getViewModel().createCommunityRequest.set(arguments?.getSerializable(AppConstants.EXTRAS.CREATE_REQUEST) as CreateCommunityRequest)
            }
            if (arguments?.containsKey(AppConstants.EXTRAS.COMMUNITY_PHOTO_LIST) == true) {
                getViewModel().photoList.set(arguments?.getSerializable(AppConstants.EXTRAS.COMMUNITY_PHOTO_LIST) as List<Uri>)
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mViewBinding = getViewDataBinding()
        viewModelInstance.setNavigator(this)

        toolbar()
        handleBackPress()
        clickListener()
        init()
    }

    private fun init() {
        getViewModel().getActivityTypesAPI()
        domainAdapter = DomainAdapter(ArrayList(), this)
        mViewBinding!!.recyclerViewDomain.adapter = domainAdapter
        applicableCodeAdapter = ApplicableCodeAdapter(ArrayList(), this)
        mViewBinding!!.recyclerViewApplicableCode.adapter = applicableCodeAdapter

        mViewBinding!!.etDomain.setOnEditorActionListener(OnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                validateDomainAndAdd()
                return@OnEditorActionListener true
            }
            false
        })

        mViewBinding!!.etDomain.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun afterTextChanged(editable: Editable) {
                if (editable.contains(",")) {
                    val temp = mViewBinding!!.etDomain.text.toString().replace(",", "").trim()
                    mViewBinding!!.etDomain.setText(temp).also {
                        validateDomainAndAdd()
                    }
                }
            }
        })
        mViewBinding!!.etApplicableCode.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun afterTextChanged(editable: Editable) {
                if (editable.isEmpty()) {
                    getViewModel().codeErrorMessage.set("")
                    getViewModel().isCodeError.set(false)
                    getViewModel().isCodeAvailable.set(applicableCodeAdapter.data.isNotEmpty())
                }
            }
        })

        mViewBinding!!.etApplicableCode.setOnEditorActionListener(OnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                validateCodeAndAdd(true)
                return@OnEditorActionListener true
            }
            false
        })

        mViewBinding!!.tvVerifyCode.setOnClickListener {
            validateCodeAndAdd(true)
        }
    }

    private fun validateDomainAndAdd() {
        val organisationDomain = mViewBinding!!.etDomain.text.toString().replace(",", "").trim()
        if (CommonUtils.isValidDomain(organisationDomain)) {
            val filterData =
                domainAdapter.data.filter {
                    it.domain.lowercase() == organisationDomain
                        .lowercase()
                }
            if (filterData.isEmpty()) {
                mViewBinding!!.etDomain.setText("")
                domainAdapter.addData(DomainData(organisationDomain))
            } else {
                mViewBinding!!.etDomain.setSelection(organisationDomain.length)
                showMessage(R.string.this_domain_already_added)
            }
        } else {
            showMessage(R.string.please_enter_valid_community_domain)
        }
    }

    private fun validateCodeAndAdd(isCallVerifyApi: Boolean) {
        val applicableCode = mViewBinding!!.etApplicableCode.text.toString().trim()
        if (applicableCode.isNotEmpty()) {
            val filterData =
                applicableCodeAdapter.data.filter {
                    it.communityCode.toString().lowercase() == applicableCode.toString()
                        .lowercase()
                }
            if (filterData.isEmpty()) {
//                mViewBinding!!.etApplicableCode.setText("")
//                applicableCodeAdapter.addData(ApplicableCodeData(applicableCode)).also {
//                }
                if (isCallVerifyApi) {
                    verifyCodeAPI()
                }
            } else {
                mViewBinding!!.etApplicableCode.setSelection(applicableCode.length)
                showMessage(R.string.this_code_already_added)
            }
        } else {
            if (isCallVerifyApi && applicableCodeAdapter.data.isNotEmpty()) {
                verifyCodeAPI()
            } else {
                showMessage(R.string.please_enter_code)
            }
        }
    }

    private fun toolbar() {
        mViewBinding!!.toolBarLayout.tvTitle.hide()
        mViewBinding!!.toolBarLayout.toolBar.setNavigationOnClickListener { v -> onBack() }
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
        mViewBinding!!.clPrivacy.setOnClickListener {
            showSelectPrivacyDialog()
        }
        mViewBinding!!.clCommunityCategory.setOnClickListener {
            showSelectCategoryDialog()
        }
        mViewBinding!!.clActivityType.setOnClickListener {
            showActivityTypeDialog()
        }
        mViewBinding!!.btnNext.setOnClickListener {
            if (getViewModel().selectedPrivacy.get()?.key == AppConstants.KEYS.Private || getViewModel().selectedPrivacy.get()?.key == AppConstants.KEYS.Global) {
                validateDataAndCreateCommunity()
            } else {
                validateDateAndGoToInviteParticipant()
            }
        }
    }

    private fun verifyCodeAPI() {
//        val codes = applicableCodeAdapter.data
//        val codeList: MutableList<String> = ArrayList()
//        if (codes.isNotEmpty()) {
//            codes.forEachIndexed { index, domains ->
//                codeList.add(domains.communityCode)
//            }.also {
//                getViewModel().verifyCommunityCode(ApplicableCodeRequest(codeList))
//            }
//        } else {
//            showMessage(R.string.please_enter_code)
//        }

        val codes = mViewBinding!!.etApplicableCode.text.toString()
        if (codes.isNotEmpty()) {
            getViewModel().verifyCommunityCode(codes)
        } else {
            showMessage(R.string.please_enter_code)
        }
    }

    override fun validateCodeSuccess(data: String?) {
        mViewBinding!!.etApplicableCode.setText("")
        data?.let { ApplicableCodeData(it) }?.let {
            applicableCodeAdapter.addData(it)
        }
    }

    private fun validateDateAndGoToInviteParticipant() {
        hideKeyboard()
        val privacy = getViewModel().selectedPrivacy.get()
        val communityCategory = getViewModel().selectedCommunityCategory.get()
//        val activityTypes = getViewModel().selectedActivityTypes.get()
        var selectedActivityTypeList =
            getViewModel().activityTypesList.get()?.filter { it.isSelected }
        val allSelectedActivityType =
            selectedActivityTypeList?.filter { it.key == AppConstants.KEYS.ALL }
        selectedActivityTypeList = if (allSelectedActivityType?.isEmpty() == false) {
            allSelectedActivityType
        } else {
            selectedActivityTypeList?.filter { it.key != AppConstants.KEYS.ALL }
        }
//        val organisationDomain = mViewBinding!!.etDomain.text
        if (privacy == null) {
            showMessage("Please select privacy")
            return
        } else if (communityCategory == null) {
            showMessage("Please select community category")
            return
        } else if (selectedActivityTypeList.isNullOrEmpty()) {
            showMessage("Please select activity types")
            return
        }
//        else if (privacy.key != AppConstants.KEYS.Public && organisationDomain.isNullOrEmpty()) {
//            showMessage("Please select organization domain")
//            return
//        }

        val createRequest = getViewModel().createCommunityRequest.get()
        val createCommunityRequest = createRequest?.let {
            CreateCommunityRequest(
                communityName = it.communityName,
                description = it.description,
                privacyType = privacy.key,
                communityCategory = communityCategory.key,
                organizationDomain = ""
            )
        }
        val bundle = Bundle()
        bundle.putSerializable(AppConstants.EXTRAS.CREATE_REQUEST, createCommunityRequest)
        bundle.putInt(AppConstants.EXTRAS.FROM, AppConstants.IS_FROM.COMMUNITY)

        val uriList = ArrayList<Uri>()
        getViewModel().photoList.get()?.forEach { uriList.add(it) }
        bundle.putSerializable(AppConstants.EXTRAS.COMMUNITY_PHOTO_LIST, uriList)
        val activityTypesList = ArrayList<ActivityTypes>()
//        if (getViewModel().selectedActivityTypes.get()?.key == AppConstants.KEYS.All) {
//            getViewModel().activityTypesList.get()
//                ?.forEach { if (it.key != AppConstants.KEYS.All) activityTypesList.add(it) }
//        } else {
//        }
//        getViewModel().selectedActivityTypes.get()?.let {
//            activityTypesList.add(it)
//        }
        selectedActivityTypeList.filter { it.isSelected }
            .let { activityTypesList.addAll(it) }.also {
                bundle.putSerializable(
                    AppConstants.EXTRAS.COMMUNITY_ACTIVITY_TYPE_LIST,
                    activityTypesList
                )
                mNavController!!.navigate(R.id.toInviteParticipantFragment, bundle)
            }
    }

    private fun validateDataAndCreateCommunity() {
        hideKeyboard()
        val isPrivateCommunity =
            getViewModel().selectedPrivacy.get()?.key == AppConstants.KEYS.Private
        val privacy = getViewModel().selectedPrivacy.get()
        val communityCategory = getViewModel().selectedCommunityCategory.get()
        val communityCodes = applicableCodeAdapter.data
        val isAvailableCode = getViewModel().isCodeAvailable.get()
        var selectedActivityTypeList =
            getViewModel().activityTypesList.get()?.filter { it.isSelected }
        val allSelectedActivityType =
            selectedActivityTypeList?.filter { it.key == AppConstants.KEYS.ALL }
        selectedActivityTypeList = if (allSelectedActivityType?.isEmpty() == false) {
            allSelectedActivityType
        } else {
            selectedActivityTypeList?.filter { it.key != AppConstants.KEYS.ALL }
        }
//        val activityTypes = getViewModel().selectedActivityTypes.get()
//        val organisationDomain = mViewBinding!!.etDomain.text
        val organisationDomain = domainAdapter.data
        if (privacy == null) {
            showMessage("Please select privacy")
            return
        } else if (communityCategory == null) {
            showMessage("Please select community category")
            return
        } else if (selectedActivityTypeList.isNullOrEmpty()) {
            showMessage("Please select activity types")
            return
        } else if (isPrivateCommunity && communityCodes.isEmpty() && communityCategory.key == AppConstants.KEYS.Membership) {
            showMessage("Please enter community code")
            return
        } else if (isPrivateCommunity && !isAvailableCode && communityCategory.key == AppConstants.KEYS.Membership) {
            showMessage("Please enter valid community code")
            return
        } else if (isPrivateCommunity && organisationDomain.isEmpty() && communityCategory.key == AppConstants.KEYS.Employer) {
            showMessage("Please enter organization domain")
            return
        }
        val communityRequest = getViewModel().createCommunityRequest.get()
        val user = AppDatabase.db.userDao().getById()
        val params = HashMap<String, Any>()

//        params["CommunityAdmin.UserId"] = user?.userId ?: ""
//        params["CommunityAdmin.UserName"] = user?.userName ?: ""
//        params["CommunityAdmin.ProfilePhotoUrl"] = user?.profilePhotoUrl ?: ""

        params["CommunityAdmins[0].UserId"] = user?.userId ?: ""
        params["CommunityAdmins[0].UserName"] = user?.userName ?: ""
        params["CommunityAdmins[0].ProfilePhotoUrl"] = user?.profilePhotoUrl ?: ""
        if (isPrivateCommunity) {
            organisationDomain.forEachIndexed { index, domains ->
                params["OrganizationDomains[${index}]"] = domains.domain
            }
            communityCodes.forEachIndexed { index, applicableCodeData ->
                params["CommunityCode[${index}]"] = applicableCodeData.communityCode
            }
        }
        params["CommunityPrivacy"] = privacy.key ?: ""
        params["CommunityCategory"] = communityCategory.key ?: ""
        communityRequest?.let {
            params["CommunityName"] = it.communityName
            params["Description"] = it.description
        }

//        if (getViewModel().selectedActivityTypes.get()?.key == AppConstants.KEYS.All) {
//            getViewModel().activityTypesList.get()
//                ?.forEachIndexed { index, activityType ->
//                    if (activityType.key != AppConstants.KEYS.All) {
//                        params["ActivityTypes[${index}].Key"] = activityType.key ?: ""
//                        params["ActivityTypes[${index}].Name"] = activityType.name ?: ""
//                        params["ActivityTypes[${index}].Icon"] = activityType.icon ?: ""
//                        params["ActivityTypes[${index}].Color"] = activityType.color ?: ""
//                        params["ActivityTypes[${index}].CombinationNo"] =
//                            activityType.combinationNo ?: 0
//                    }
//                }.also {
//                    val photoList = ArrayList<File>()
//                    getViewModel().photoList.get()?.forEach { photoList.add(File(it.path!!)) }
//                        .also {
//                            getViewModel().createCommunity(
//                                params,
//                                if (photoList.isNotEmpty()) photoList[0] else null
//                            )
//                        }
//                }
//        } else {
//        }
//        getViewModel().activityTypesList.get()?.let { activityTypes ->
//            if (activityTypes.isNotEmpty()) {
        selectedActivityTypeList//.filter { it.key != AppConstants.KEYS.ALL }
            .forEachIndexed { index, activityType ->
                params["ActivityTypes[${index}].Key"] = activityType.key ?: ""
                params["ActivityTypes[${index}].Name"] = activityType.name ?: ""
                params["ActivityTypes[${index}].Icon"] = activityType.icon ?: ""
                params["ActivityTypes[${index}].Color"] = activityType.color ?: ""
                params["ActivityTypes[${index}].CombinationNo"] =
                    activityType.combinationNo ?: 0
//                }
//            }
            }.also {
                val photoList = ArrayList<File>()
                getViewModel().photoList.get()?.forEach { photoList.add(File(it.path!!)) }
                    .also {
                        getViewModel().createCommunity(
                            params,
                            if (photoList.isNotEmpty()) photoList[0] else null
                        )
                    }
            }

    }

    private fun showSelectPrivacyDialog() {
        val dialog =
            getViewModel().privacyList.get()?.let {
                it.forEach { it1 ->
                    it1.isSelected = (it1.key === getViewModel().selectedPrivacy.get()?.key)
                }
                SelectIdNameDialog(
                    requireActivity(),
                    it,
                    forWhom = AppConstants.FROM.PRIVACY
                )
            }
        dialog?.setListener(this)
        dialog?.show()
    }

    private fun showSelectCategoryDialog() {
        val dialog =
            getViewModel().filteredCommunityCategoryList.get()?.let {
                it.forEach { it1 ->
                    it1.isSelected =
                        (it1.key === getViewModel().selectedCommunityCategory.get()?.key)
                }
                SelectIdNameDialog(
                    requireActivity(),
                    it,
                    forWhom = AppConstants.FROM.COMMUNITY_CATEGORY
                )
            }
        dialog?.setListener(this)
        dialog?.show()
    }

    private fun showActivityTypeDialog() {
        val dialog =
            getViewModel().activityTypesList.get()?.let {
//                it.forEach { it1 ->
//                    it1.isSelected = (it1.name === getViewModel().selectedActivityTypes.get()?.name)
//                }
                SelectActivityTypesDialog(
                    requireActivity(),
                    it,
                    true
                )
            }
        dialog?.setListener(this)
        dialog?.show()
    }

    override fun onItemSelect(data: ActivityTypes) {
//        getViewModel().selectedActivityTypes.set(data)
    }

    override fun onSelectedItems(data: List<ActivityTypes>) {
        getViewModel().selectedActivityNames.set(getSelectedActivityTypeName(data))
        getViewModel().selectedActivityTypes.set(getSelectedActivityTypeIcon(data))
    }

    override fun onItemSelect(data: IdNameData, forWhom: String) {
        when (forWhom) {
            AppConstants.FROM.PRIVACY -> {
                getViewModel().selectedPrivacy.set(data).also {
                    setCommunityCategory()
                }
                log("ccViewModel.selectedPrivacy.key == AppConstants.KEYS.Public ${data.key == AppConstants.KEYS.Public}")
            }
            AppConstants.FROM.COMMUNITY_CATEGORY -> {
                getViewModel().selectedCommunityCategory.set(data)
            }
        }
    }

    override fun activityTypesFetchSuccessfully(list: List<ActivityTypes>) {
        if (list.isNotEmpty()) {
            val activityTypesList = ArrayList<ActivityTypes>()
            activityTypesList.add(getViewModel().getActivityTypeAllSelected())
            activityTypesList.addAll(list)
            getViewModel().selectedActivityTypes.set(activityTypesList[0])
            getViewModel().activityTypesList.set(activityTypesList)
        }
        getViewModel().getCommunityOptionsAPI()
    }

    override fun communityOptionsFetchSuccessfully(communityOptionsData: CommunityOptionsData?) {
        communityOptionsData?.let { data ->
            getViewModel().privacyList.set(data.privacy)
            getViewModel().communityCategoryList.set(data.category)
            if (data.privacy.isNotEmpty()) {
                getViewModel().selectedPrivacy.set(data.privacy[0]).also {
                    setCommunityCategory()
                }
            }
        }
    }

    private fun setCommunityCategory() {
        getViewModel().selectedPrivacy.get()?.let { privacy ->
            val filter =
                getViewModel().communityCategoryList.get()
                    ?.filter { if (privacy.key == AppConstants.KEYS.Private) it.key != AppConstants.KEYS.Partner else it.key == AppConstants.KEYS.Partner }
            getViewModel().filteredCommunityCategoryList.set(filter).also {
                filter?.let {
                    if (it.isNotEmpty()) {
                        getViewModel().selectedCommunityCategory.set(it[0])
                    }
                }
            }
        }
    }

    override fun createCommunitySuccess(data: CommunityData?) {
        val bundle = Bundle()
        bundle.putString(AppConstants.EXTRAS.COMMUNITY_ID, data?.communityId)
        mNavController!!.navigate(R.id.toSuccessFragment, bundle)
    }

    override fun updateCommunitySuccess(data: CommunityData?) {

    }

    override fun onRemoveItemPress(position: Int) {
        val dataList: MutableList<DomainData> = ArrayList()
        dataList.addAll(domainAdapter.data)
        dataList.removeAt(position)
        domainAdapter.data = dataList
        domainAdapter.notifyDataSetChanged()
    }

    override fun onRemoveCodeItemPress(position: Int) {
        val dataList: MutableList<ApplicableCodeData> = ArrayList()
        dataList.addAll(applicableCodeAdapter.data)
        dataList.removeAt(position)
        applicableCodeAdapter.data = dataList
        applicableCodeAdapter.notifyDataSetChanged()
        getViewModel().isCodeError.set(false)
        getViewModel().isCodeAvailable.set(false)
        getViewModel().codeErrorMessage.set("")
    }
}