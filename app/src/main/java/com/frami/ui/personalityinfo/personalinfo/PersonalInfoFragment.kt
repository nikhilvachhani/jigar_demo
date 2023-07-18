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
import com.frami.utils.extensions.visible
import java.io.File
import java.util.*

class PersonalInfoFragment :
    BaseFragment<FragmentPersonalityInfoBinding, PersonalInfoFragmentViewModel>(),
    PersonalInfoFragmentNavigator, View.OnClickListener, DatePickerFragment.DateSelectListener,
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
        if (getViewModel().isFromEdit.get()) {
            mViewBinding!!.toolBarLayout.tvTitle.hide()
            mViewBinding!!.toolBarLayout.cvDone.visible()
            mViewBinding!!.toolBarLayout.cvDone.setOnClickListener { validateDataAndCallAPI() }
            mViewBinding!!.toolBarLayout.cvBack.visible()
            mViewBinding!!.toolBarLayout.cvBack.setOnClickListener { onBack() }
        } else {
            mViewBinding!!.toolBarLayout.tvTitle.text = getString(R.string.personal_info)
            mViewBinding!!.toolBarLayout.tvGoBack.visible()
            mViewBinding!!.toolBarLayout.tvGoBack.setOnClickListener { logout() }
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
        if (getViewModel().isFromEdit.get()) {
            mNavController!!.navigateUp()
        } else {
            requireActivity().finish()
        }
    }

    private fun clickListener() {
        mViewBinding!!.ivAddProfilePhoto.setOnClickListener(this)
        mViewBinding!!.tvAddProfilePhoto.setOnClickListener(this)
        mViewBinding!!.cvDD.setOnClickListener(this)
        mViewBinding!!.cvMM.setOnClickListener(this)
        mViewBinding!!.cvYYYY.setOnClickListener(this)
        mViewBinding!!.llMale.setOnClickListener(this)
        mViewBinding!!.llFeMale.setOnClickListener(this)
        mViewBinding!!.llOther.setOnClickListener(this)
        mViewBinding!!.clCountry.setOnClickListener(this)
        mViewBinding!!.btnNext.setOnClickListener(this)
    }

    //    lateinit var  singleBuilder: SingleDateAndTimePickerDialog.Builder
    override fun onClick(view: View?) {
        when (view) {
            mViewBinding!!.ivAddProfilePhoto, mViewBinding!!.tvAddProfilePhoto -> {
                ImagePickerActivity.showImageChooser(requireActivity(), this)
            }
            mViewBinding!!.llMale -> {
                getViewModel().genderSelected.set(AppConstants.GENDER.MALE)
            }
            mViewBinding!!.llFeMale -> {
                getViewModel().genderSelected.set(AppConstants.GENDER.FEMALE)
            }
            mViewBinding!!.llOther -> {
                getViewModel().genderSelected.set(AppConstants.GENDER.OTHER)
            }
            mViewBinding!!.cvDD, mViewBinding!!.cvMM, mViewBinding!!.cvYYYY -> {
                hideKeyboard()
                selectDateOfBirth()
//                val calendar = Calendar.getInstance()
//                val defaultDate = calendar.time
//
//                singleBuilder =
//                    SingleDateAndTimePickerDialog.Builder(
//                        requireContext()
//                    )
//                        .setTimeZone(TimeZone.getDefault())
//                        .bottomSheet()
//                        .curved() //.titleTextColor(Color.GREEN)
//                        //.backgroundColor(Color.BLACK)
//                        //.mainColor(Color.GREEN)
//                        .displayHours(true)
//                        .displayMinutes(true)
//                        .displayDays(true)
//                        .displayListener(object :
//                            SingleDateAndTimePickerDialog.DisplayListener {
//                            override fun onDisplayed(picker: SingleDateAndTimePicker) {
//                                Log.d(
//                                    "",
//                                    "Dialog displayed"
//                                )
//                            }
//
//                            override fun onClosed(picker: SingleDateAndTimePicker) {
//                                Log.d(
//                                    "",
//                                    "Dialog closed"
//                                )
//                            }
//                        })
//                        .title("")
//                        .listener { date ->
//                            log(
//                                "DATE>> ",
//                                date.toString()
//                            )
//                        }
//                singleBuilder.display()
            }
            mViewBinding!!.clCountry -> {
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
        val gender = getViewModel().genderSelected.get()?.type
        val birthDate = getViewModel().birthDate.get()
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
        } else if (gender == null) {
            showMessage("Please select gender")
        } else if (birthDate == null) {
            showMessage("Please select date of birth")
        } else if (country == null) {
            showMessage("Please select nationality")
        } else {
            val user = getViewModel().user.get()
            val userRequest = UserRequest(
                userName = "$firstName $lastName",
                firstName = firstName,
                lastName = lastName,
//                ProfilePhoto = File(getViewModel().selectedProfilePhoto.get()?.path),
                gender = gender,
//                nationality = country,
                NationalityCode = country.code,
                NationalityName = country.name,
                UserId = user?.userId!!,
                identityProvider = if (user.identityProvider == null) "" else user.identityProvider!!,
                b2CFlow = if (user.b2CFlow == null) "" else user.b2CFlow!!,
                profilePhotoUrl = user.profilePhotoUrl,
                birthDate = birthDate,
                emailAddress = if (user.emailAddress == null) "" else user.emailAddress!!,
                workEmailAddress = if (user.workEmailAddress == null) "" else user.workEmailAddress!!,
                isPersonalInfoCompleted = true,
                isContactInfoCompleted = user.isContactInfoCompleted,
                isPrivacyPolicyAgreed = user.isPrivacyPolicyAgreed,
                isSendNotification = user.isSendNotification,
                isDeviceConnected = user.isDeviceConnected,
                isWelcomeEmailSent = user.isWelcomeEmailSent,
                isVerificationEmailSent = user.isVerificationEmailSent,
                isEmailVerified = user.isEmailVerified,
                isWorkVerificationEmailSent = user.isWorkVerificationEmailSent,
                isWorkEmailVerified = user.isWorkEmailVerified,
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
                if (!user.isPersonalInfoCompleted && !user.isContactInfoCompleted) {
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
        mViewBinding!!.ivAddProfilePhoto.loadCircleImage(imagePath)
    }

    override fun onError() {
    }

    private fun selectDateOfBirth() {
        val preSelectedCal = Calendar.getInstance()
        if (getViewModel().birthYear.get() != null) {
            preSelectedCal[Calendar.YEAR] = getViewModel().birthYear.get()!!
            preSelectedCal[Calendar.MONTH] = getViewModel().birthMonth.get()!! - 1
            preSelectedCal[Calendar.DAY_OF_MONTH] = getViewModel().birthDay.get()!!
        } else{
            preSelectedCal[Calendar.YEAR] = preSelectedCal[Calendar.YEAR]-18
        }
        val newFragment: DialogFragment =
            DatePickerFragment(
                isSetMinDate = false,
                isSetCurrentDateMaxDate = true,
                isMin18YearsOld = false,
                dateSelectListener = this,
                preSelectedCal = preSelectedCal
            )
        newFragment.show(requireActivity().supportFragmentManager, "datePicker")
    }

    override fun onDateSet(year: Int, month: Int, day: Int, forWhom: String) {
        getViewModel().birthYear.set(year)
        getViewModel().birthMonth.set(month)
        getViewModel().birthDay.set(day)
        getViewModel().birthDate.set(
            DateTimeUtils.getDateFromDate(
                "$year $month $day",
                DateTimeUtils.dateFormatYYYY_MM_DD
            )
        )
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
                authFlow(user, true, wearableDeviceActivityResultLauncher, null)
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

    // You can do the assignment inside onAttach or onCreate, i.e, before the activity is displayed
    var wearableDeviceActivityResultLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult(),
        ActivityResultCallback<ActivityResult> { result ->
            getViewModel().isEnableNavigationToForward.set(true)
            getViewModel().getUserInfo(true)
//            getViewModel().getUserLiveData().observe(
//                viewLifecycleOwner,
//                Observer { user ->
//                    if (user != null) {
//                        authFlow(user, false, null)
//                    }
//                })
        })
}