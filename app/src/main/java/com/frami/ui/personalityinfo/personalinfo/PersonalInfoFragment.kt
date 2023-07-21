package com.frami.ui.personalityinfo.personalinfo

import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.frami.BR
import com.frami.R
import com.frami.data.model.lookup.CountryData
import com.frami.data.model.user.User
import com.frami.data.model.user.UserRequest
import com.frami.databinding.FragmentPersonalityInfoBinding
import com.frami.ui.base.BaseFragment
import com.frami.ui.common.DatePickerFragment
import com.frami.ui.common.ImagePickerActivity
import com.frami.ui.common.SelectCountryDialog
import com.frami.utils.AppConstants
import com.frami.utils.DateTimeUtils
import com.frami.utils.extensions.hide
import com.frami.utils.extensions.loadCircleImage
import com.frami.utils.extensions.onClick
import com.frami.utils.extensions.visible
import java.io.File
import java.util.*

class PersonalInfoFragment :
    BaseFragment<FragmentPersonalityInfoBinding, PersonalInfoFragmentViewModel>(),
    PersonalInfoFragmentNavigator, View.OnClickListener,
    SelectCountryDialog.OnDialogActionListener, ImagePickerActivity.PickerResultListener {

    private val viewModelInstance: PersonalInfoFragmentViewModel by viewModels {
        viewModeFactory
    }
    private var mViewBinding: FragmentPersonalityInfoBinding? = null
    override fun getBindingVariable(): Int = BR.viewModel

    override fun getLayoutId(): Int = R.layout.fragment_personality_info

    override fun getViewModel(): PersonalInfoFragmentViewModel = viewModelInstance

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            if (arguments?.containsKey(AppConstants.FROM.EDIT) == true) {
                getViewModel().isFromEdit.set(arguments?.getBoolean(AppConstants.FROM.EDIT)!!)
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mViewBinding = getViewDataBinding()
        viewModelInstance.setNavigator(this)

        getViewModel().getCountry()
        toolbar()
        handleBackPress()
        clickListener()

//        Handler(Looper.getMainLooper()).postDelayed({
//
//        }, 400)
//        else {
//            getViewModel().getUserLiveData().observe(
//                viewLifecycleOwner,
//                androidx.lifecycle.Observer { user ->
//                    if (user != null && getViewModel().user.get() == null) {
//                        getViewModel().user.set(user)
//                        getViewModel().getUserInfo(getViewModel().user.get()?.userId!!)
//                    }
//                })
//        }
    }

    private fun setUserDetails(users: User? = null) {
        if (isAdded) {
            val user = users ?: getViewModel().user.get()
            if (user != null) {
                mViewBinding!!.etFN.setText(user.firstName.takeIf { s ->
                    !TextUtils.isEmpty(s)
                }
                    .toString())
                mViewBinding!!.etFN.text?.length?.let { mViewBinding!!.etFN.setSelection(it) }
                mViewBinding!!.etLN.setText(user.lastName.takeIf { s -> !TextUtils.isEmpty(s) }
                    .toString())
                mViewBinding!!.etLN.text?.length?.let { mViewBinding!!.etLN.setSelection(it) }
                if (user.gender != null) {
                    user.gender.let {
                        when (it) {
                            AppConstants.GENDER.MALE.type -> {
                                getViewModel().genderSelected.set(AppConstants.GENDER.MALE)
                            }
                            AppConstants.GENDER.FEMALE.type -> {
                                getViewModel().genderSelected.set(AppConstants.GENDER.FEMALE)
                            }
                            AppConstants.GENDER.OTHER.type -> {
                                getViewModel().genderSelected.set(AppConstants.GENDER.OTHER)
                            }
                        }
                    }
                }
                getViewModel().birthDate.set(user.birthDate)
                if (!TextUtils.isEmpty(user.birthDate)) {
                    getViewModel().birthDay.set(
                        user.birthDate?.let {
                            DateTimeUtils.getDateFromServerDateFormatToTODateFormat(
                                it,
                                DateTimeUtils.dateFormatDD
                            ).toInt()
                        }
                    )
                    getViewModel().birthMonth.set(
                        user.birthDate?.let {
                            DateTimeUtils.getDateFromServerDateFormatToTODateFormat(
                                it,
                                DateTimeUtils.dateFormatMM
                            ).toInt()
                        }
                    )
                    getViewModel().birthYear.set(
                        user.birthDate?.let {
                            DateTimeUtils.getDateFromServerDateFormatToTODateFormat(
                                it,
                                DateTimeUtils.dateFormatYYYY
                            ).toInt()
                        }
                    )
                }
//                log("COUNTRY>> ${Gson().toJson(user.nationality)}")
                if (getViewModel().countryDataList.get()?.isNotEmpty()!!) {
//                    log(
//                        "COUNTRY>> ${
//                            getViewModel().countryDataList.get()
//                                ?.find { it.code == user.nationality?.code }
//                        }"
//                    )
                    getViewModel().selectedCountry.set(
                        getViewModel().countryDataList.get()
                            ?.find { it.code == user.nationality?.code })
                }
            }
        }
    }

    private fun toolbar() {
        mViewBinding!!.toolBarLayout.cvBack.visible()
        mViewBinding!!.toolBarLayout.cvBack.setImageResource(R.drawable.ic_back_new)
        mViewBinding!!.toolBarLayout.tvTitle.hide()
        mViewBinding?.toolBarLayout?.cvBack?.setOnClickListener { logout() }
        if (getViewModel().isFromEdit.get()) {
            mViewBinding!!.toolBarLayout.cvDone.visible()
            mViewBinding!!.toolBarLayout.cvDone.setOnClickListener { validateDataAndCallAPI() }
        }else{
            mViewBinding?.btnNext?.onClick { validateDataAndCallAPI() }
        }
        mViewBinding!!.toolBarLayout.toolBar.setNavigationOnClickListener { v -> onBack() }
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
        mViewBinding!!.ivAddProfilePhoto.setOnClickListener(this)
        mViewBinding!!.linearAddPhoto.setOnClickListener(this)
        mViewBinding!!.linCountry.setOnClickListener(this)
        mViewBinding!!.btnNext.setOnClickListener(this)
    }

    //    lateinit var  singleBuilder: SingleDateAndTimePickerDialog.Builder
    override fun onClick(view: View?) {
        when (view) {
            mViewBinding!!.ivAddProfilePhoto, mViewBinding!!.linearAddPhoto -> {
                ImagePickerActivity.showImageChooser(requireActivity(), this)
            }
            mViewBinding!!.linCountry -> {
                showSelectCountryDialog()
            }
            mViewBinding!!.btnNext -> {
                validateDataAndCallAPI()
            }
        }
    }

    private fun validateDataAndCallAPI() {
        hideKeyboard()
        val firstName = mViewBinding!!.etFN.text.toString().trim()
        val lastName = mViewBinding!!.etLN.text.toString().trim()
        val country = getViewModel().selectedCountry.get()
//        if (getViewModel().selectedProfilePhoto.get() == null && (getViewModel().user.get() != null && TextUtils.isEmpty(
//                getViewModel().user.get()?.profilePhotoUrl
//            ))
//        ) {
//            showMessage("Please select profile photo")
//        } else
        if (TextUtils.isEmpty(firstName)) {
            showMessage("Please enter first name")
        } else if (TextUtils.isEmpty(lastName)) {
            showMessage("Please enter last name")
        } else if (country == null) {
            showMessage("Please select country")
        } else {
            val user = getViewModel().user.get()
            val userRequest = UserRequest(
                userName = "$firstName $lastName",
                firstName = firstName,
                lastName = lastName,
//                ProfilePhoto = File(getViewModel().selectedProfilePhoto.get()?.path),
//                gender = gender,
//                nationality = country,
                NationalityCode = country.code,
                NationalityName = country.name,
                UserId = user?.userId!!,
                identityProvider = if (user.identityProvider == null) "" else user.identityProvider!!,
                b2CFlow = if (user.b2CFlow == null) "" else user.b2CFlow!!,
                profilePhotoUrl = user.profilePhotoUrl,
//                birthDate = birthDate,
                emailAddress = if (user.emailAddress == null) "" else user.emailAddress!!,
                workEmailAddress = if (user.workEmailAddress == null) "" else user.workEmailAddress!!,
                isPersonalInfoCompleted = true,
                isContactInfoCompleted = user.isContactInfoCompleted,
                isPrivacyPolicyAgreed = true,
//                isPrivacyPolicyAgreed = user.isPrivacyPolicyAgreed,
                isSendNotification = user.isSendNotification,
                isDeviceConnected = user.isDeviceConnected,
                isWelcomeEmailSent = user.isWelcomeEmailSent,
                isVerificationEmailSent = user.isVerificationEmailSent,
                isEmailVerified = user.isEmailVerified,
                isWorkVerificationEmailSent = user.isWorkVerificationEmailSent,
                isWorkEmailVerified = user.isWorkEmailVerified,
                isPrivacySettingCompleted = user.isPrivacySettingCompleted,
//                userDevices = if (user.userDevices!=null) user.userDevices!! else ArrayList<UserDevices>()
            )

            if (getViewModel().isFromEdit.get()) {
                if (getViewModel().selectedProfilePhoto.get() != null && !getViewModel().selectedProfilePhoto.get()?.path.isNullOrEmpty()) {
                    getViewModel().updateUser(
                        userRequest,
                        File(getViewModel().selectedProfilePhoto.get()?.path ?: "")
                    )
                } else {
                    getViewModel().updateUser(userRequest)
                }
            } else {
                if (!user.isPersonalInfoCompleted) {
                    if (getViewModel().selectedProfilePhoto.get() != null && !getViewModel().selectedProfilePhoto.get()?.path.isNullOrEmpty()) {
                        getViewModel().createUser(
                            userRequest,
                            File(getViewModel().selectedProfilePhoto.get()?.path)
                        )
                    } else {
                        getViewModel().createUser(userRequest)
                    }
                } else {
                    if (getViewModel().selectedProfilePhoto.get() != null && !getViewModel().selectedProfilePhoto.get()?.path.isNullOrEmpty()) {
                        getViewModel().updateUser(
                            userRequest,
                            File(getViewModel().selectedProfilePhoto.get()?.path)
                        )
                    } else {
                        getViewModel().updateUser(userRequest)
                    }
                }
            }
        }
    }

    override fun onImageAvailable(imagePath: Uri?) {
        getViewModel().selectedProfilePhoto.set(imagePath)
        mViewBinding!!.ivAddProfilePhoto.visible()
        mViewBinding!!.linearAddPhoto.hide()
        mViewBinding!!.ivAddProfilePhoto.loadCircleImage(imagePath)
    }

    override fun onError() {
    }

    private fun showSelectCountryDialog() {
        val dialog =
            SelectCountryDialog(
                requireActivity(),
                getViewModel().countryDataList.get() as MutableList<CountryData>
            )
        dialog.setListener(this)
        dialog.show()
    }

    override fun onCountrySelect(countryData: CountryData) {
        getViewModel().selectedCountry.set(countryData)
    }

    override fun countryFetchSuccess() {
        if (getViewModel().countryDataList.get()
                ?.isNotEmpty()!! && getViewModel().user.get() == null
        ) {
            val selectedCountry =
                getViewModel().countryDataList.get()?.find { it.code == getDeviceCountryCode() }
            getViewModel().selectedCountry.set(selectedCountry)
        }
        if (getViewModel().isFromEdit.get()) {
            getViewModel().getUserInfo(true)
        } else {
            setUserDetails()
        }
    }

    override fun createOrUpdateUserSuccess(user: User?) {
        if (getViewModel().isFromEdit.get()) {
            onBack()
        } else {
            if (user != null) {
                authFlow(user, true, null, null)
            } else {
                navigateToLogin()
            }
        }
    }

    override fun countryLoadFromDatabaseSuccess(countryList: List<CountryData>) {
//        setUserDetails(countryList)
    }

    override fun userInfoFetchSuccess(user: User?) {
        setUserDetails(user)
        if (getViewModel().isEnableNavigationToForward.get()) {
            if (user != null) {
                authFlow(user, false, null, null)
            }
        }
    }
}