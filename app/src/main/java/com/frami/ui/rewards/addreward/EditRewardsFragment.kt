package com.frami.ui.rewards.addreward

import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Patterns
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.frami.BR
import com.frami.R
import com.frami.data.model.explore.ChallengesData
import com.frami.data.model.rewards.RewardsData
import com.frami.data.model.rewards.RewardsDetails
import com.frami.databinding.FragmentEditRewardsBinding
import com.frami.ui.base.BaseFragment
import com.frami.ui.common.DatePickerFragment
import com.frami.ui.common.ImagePickerActivity
import com.frami.utils.AppConstants
import com.frami.utils.DateTimeUtils
import com.frami.utils.extensions.hide
import com.frami.utils.extensions.visible
import java.io.File
import java.util.*


class EditRewardsFragment :
    BaseFragment<FragmentEditRewardsBinding, AddRewardsFragmentViewModel>(),
    AddRewardsFragmentNavigator, ImagePickerActivity.PickerResultListener,
    DatePickerFragment.DateSelectListener {

    private val viewModelInstance: AddRewardsFragmentViewModel by viewModels {
        viewModeFactory
    }
    private var mViewBinding: FragmentEditRewardsBinding? = null
    override fun getBindingVariable(): Int = BR.viewModel

    override fun getLayoutId(): Int = R.layout.fragment_edit_rewards

    override fun getViewModel(): AddRewardsFragmentViewModel = viewModelInstance

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            if (arguments?.containsKey(AppConstants.EXTRAS.REWARD_DETAILS) == true) {
                getViewModel().rewardsDetails.set(arguments?.getSerializable(AppConstants.EXTRAS.REWARD_DETAILS) as RewardsDetails)
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
        getViewModel().rewardsDetails.get()?.let {
            mViewBinding!!.etRewardTitle.setText(it.title.toString())
            mViewBinding!!.etRewardDescription.setText(it.description.toString())
            getViewModel().expiryDate.set(
                it.expiryDate?.let { it1 ->
                    DateTimeUtils.getDateFromServerDateFormatToTODateFormat(
                        it1,
                        DateTimeUtils.dateFormatDDMMMMYYYY,
                    )
                }
            )
            it.expiryDate?.let { it1 ->
                DateTimeUtils.getServerCalendarDateFromDateFormat(
                    it1
                )
            }.apply {
                getViewModel().startDateDay.set(this?.get(Calendar.DAY_OF_MONTH))
                getViewModel().startDateMonth.set(this?.get(Calendar.MONTH)?.plus(1))
                getViewModel().startDateYear.set(this?.get(Calendar.YEAR))
            }

            mViewBinding!!.etOtherImpInfo.setText(it.otherInfo.toString())
            mViewBinding!!.etLinkToWebsite.setText(it.linkToWebsite.toString())
        }
        activity?.let {
            Glide.with(it).asGif().load(R.drawable.successfully).into(mViewBinding!!.ivSuccess)
        }
    }

    private fun toolbar() {
        mViewBinding!!.toolBarLayout.tvTitle.hide()
        mViewBinding!!.toolBarLayout.toolBar.setNavigationOnClickListener { v -> onBack() }
        mViewBinding!!.toolBarLayout.cvSearch.hide()
        mViewBinding!!.toolBarLayout.cvNotification.hide()
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
//        requireActivity().finish()
        mNavController!!.navigateUp()
    }

    private fun clickListener() {
        mViewBinding!!.tvAddPhoto.setOnClickListener {
            ImagePickerActivity.showImageChooser(requireActivity(), this)
        }
        mViewBinding!!.ivAddReward.setOnClickListener {
            ImagePickerActivity.showImageChooser(requireActivity(), this)
        }
        mViewBinding!!.ivRewardPhoto.setOnClickListener {
            ImagePickerActivity.showImageChooser(requireActivity(), this)
        }
        mViewBinding!!.clExpiry.setOnClickListener {
            selectStartDate()
        }
        mViewBinding!!.btnCreate.setOnClickListener {
            validateDataAndCreateReward()
        }
    }

    private fun validateDataAndCreateReward() {
        hideKeyboard()
        val rewardPhoto = getViewModel().selectedRewardPhoto.get()
        val rewardTitle = mViewBinding!!.etRewardTitle.text.toString()
        val rewardDescription = mViewBinding!!.etRewardDescription.text.toString()
        val website = mViewBinding!!.etLinkToWebsite.text.toString()
        val expiryDate = getViewModel().expiryDate.get()?.let {
            DateTimeUtils.getDateFromDateFormatToTODateFormat(
                it,
                DateTimeUtils.dateFormatDDMMMMYYYY,
                DateTimeUtils.dateFormatServer_YYYY_MM_DD_T_HHMMSS
            )
        }
        val otherImpInfo = mViewBinding!!.etOtherImpInfo.text.toString()
        val rewardsDetails = getViewModel().rewardsDetails.get()
        if (rewardPhoto == null && rewardsDetails?.rewardImagesUrl?.isEmpty() == true) {
            showMessage("Please select reward image")
            return
        } else if (rewardTitle.toString().isEmpty()) {
            showMessage("Please enter reward title")
            return
        } else if (rewardDescription.toString().isEmpty()) {
            showMessage("Please enter reward description")
            return
        } else if (expiryDate.isNullOrEmpty()) {
            showMessage("Please select expiry date")
            return
        } else if (website.isNotEmpty()) {
            if (!Patterns.WEB_URL.matcher(website).matches()) {
                showMessage("Please enter valid link of website")
                return
            }
        }

        val params = HashMap<String, Any>()
        params["RewardId"] = rewardsDetails?.rewardId ?: ""
        params["Title"] = rewardTitle
        params["Description"] = rewardDescription
        params["ExpiryDate"] = expiryDate ?: ""
        params["OtherInfo"] = otherImpInfo ?: ""
        params["LinkToWebsite"] = website ?: ""

        val photoList = ArrayList<File>()
        if (rewardPhoto != null) {
            photoList.add(File(rewardPhoto.path!!))
        } else {
            params["RewardImagesUrl[0]"] = rewardsDetails?.rewardImagesUrl?.first() ?: ""
        }
        getViewModel().updateRewards(params, rewardPhotoList = photoList)
    }

    override fun onImageAvailable(imagePath: Uri?) {
        getViewModel().selectedRewardPhoto.set(imagePath)
//        mViewBinding!!.ivChallengePhoto.setImageURI(imagePath)
    }

    override fun onError() {
    }

    private fun selectStartDate() {
        val preSelectedCal = Calendar.getInstance()
        if (getViewModel().startDateYear.get() != null) {
            preSelectedCal[Calendar.YEAR] = getViewModel().startDateYear.get()!!
            preSelectedCal[Calendar.MONTH] = getViewModel().startDateMonth.get()!! - 1
            preSelectedCal[Calendar.DAY_OF_MONTH] = getViewModel().startDateDay.get()!!
        } else {
            preSelectedCal.add(Calendar.DAY_OF_MONTH, 1)
        }
        val newFragment: DialogFragment =
            DatePickerFragment(
                isSetMinDate = true,
                dateSelectListener = this,
                forWhom = AppConstants.FROM.START_DATE,
                preSelectedCal = preSelectedCal
            )
        newFragment.show(requireActivity().supportFragmentManager, "datePicker")
    }

    override fun onDateSet(year: Int, month: Int, day: Int, forWhom: String) {
        when (forWhom) {
            AppConstants.FROM.START_DATE -> {
                getViewModel().startDateYear.set(year)
                getViewModel().startDateMonth.set(month)
                getViewModel().startDateDay.set(day)
                getViewModel().expiryDate.set(
                    DateTimeUtils.getDateFromDateFormatToTODateFormat(
                        "$year-$month-$day",
                        DateTimeUtils.dateFormatYYYY_MM_DD,
                        DateTimeUtils.dateFormatDDMMMMYYYY
                    )
                )
            }
        }
    }

    override fun createRewardsSuccess(data: RewardsData?) {
//        getViewModel().rewardCreatedSuccess.set(true)
//        Handler(Looper.getMainLooper()).postDelayed({
//
//        }, 5000)
        onBack()
//        val bundle = Bundle()
//        bundle.putString(AppConstants.EXTRAS.REWARD_ID, data?.rewardId)
//        mNavController!!.navigate(R.id.toSuccessFragment, bundle)
    }

    override fun createChallengeSuccess(data: ChallengesData?) {
        onBack()
    }
}