package com.frami.ui.dashboard.rewards.details

import android.app.Activity
import android.util.DisplayMetrics
import android.widget.FrameLayout
import androidx.databinding.DataBindingUtil
import com.frami.R
import com.frami.data.model.rewards.RewardsData
import com.frami.data.model.rewards.RewardsDetails
import com.frami.databinding.DialogRewardDetailsBinding
import com.frami.utils.CommonUtils
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog


class RewardDetailsDialog(
    private val mActivity: Activity,
    private var rewardsData: RewardsDetails
) : BottomSheetDialog(
    mActivity, R.style.BottomSheetDialogStyle
) {
    private var bindingSheet: DialogRewardDetailsBinding? = null
    var onDialogActionListener: OnDialogActionListener? = null
    fun setListener(listener: OnDialogActionListener) {
        this.onDialogActionListener = listener
    }

    override fun show() {
        super.show()
        val bottomSheet =
            findViewById<FrameLayout>(com.google.android.material.R.id.design_bottom_sheet)
        if (bottomSheet != null) {
            val layoutParams = bottomSheet.layoutParams

            val windowHeight: Int = getWindowHeight()
            if (layoutParams != null) {
                layoutParams.height = windowHeight
            }
            bottomSheet.layoutParams = layoutParams
            BottomSheetBehavior.from(bottomSheet).state = BottomSheetBehavior.STATE_EXPANDED
        }
    }

    private fun getWindowHeight(): Int {
        // Calculate window height for fullscreen use
        val displayMetrics = DisplayMetrics()
        mActivity.windowManager.defaultDisplay.getMetrics(displayMetrics)
        return displayMetrics.heightPixels
    }

    init {
//        val view = this.layoutInflater.inflate(R.layout.dialog_challenge_details, null)
//        this.setContentView(view)
        this.bindingSheet = DataBindingUtil.inflate<DialogRewardDetailsBinding>(
            this.layoutInflater,
            R.layout.dialog_reward_details,
            null,
            false
        )
        this.setContentView(bindingSheet!!.root)
        setRewardData(rewardsData)
    }

    fun setRewardData(rewardsDetails: RewardsDetails) {
        this.rewardsData = rewardsDetails
        bindingSheet!!.data = rewardsDetails
//        mActivity.runOnUiThread {
//            bindingSheet!!.btnDetails.visible()
//            bindingSheet!!.clDetails.hide()
//        }
        setData()
    }

    private fun setData() {
//        bindingSheet!!.btnDetails.setOnClickListener {
//            mActivity.runOnUiThread {
//                bindingSheet!!.btnDetails.hide()
//                bindingSheet!!.clDetails.visible()
//            }
//        }
        bindingSheet!!.ivFavorite.setOnClickListener {
            if (onDialogActionListener != null) {
                rewardsData.isFavorite = !rewardsData.isFavorite
                onDialogActionListener?.onFavourite(rewardsData)
                bindingSheet!!.data = rewardsData
            }
        }
        bindingSheet!!.cvEdit.setOnClickListener {
            if (onDialogActionListener != null) {
                this.dismiss()
                onDialogActionListener?.onEditRewardPress(rewardsData)
            }
        }
        bindingSheet!!.btnUnlock.setOnClickListener {
            if (onDialogActionListener != null) {
                onDialogActionListener?.onUnlockReward(rewardsData)
            }
        }
        bindingSheet!!.btnActivateRewards.setOnClickListener {
            if (onDialogActionListener != null) {
                onDialogActionListener?.onActivateRewardConfirmationPress(rewardsData)
            }
        }
        bindingSheet!!.cvRewardsImage.setOnClickListener {
            rewardsData.let {
                if (it.rewardImagesUrl.isNotEmpty()) {
                    CommonUtils.showZoomImage(mActivity, it.rewardImagesUrl[0])
                }
            }
        }
        bindingSheet!!.tvViewChallenge.setOnClickListener {
            if (onDialogActionListener != null) {
                rewardsData.let {
                    if (it.isChallengeLinked && it.challengeId.isNotEmpty()) {
                        onDialogActionListener?.onChallengeDetailsPress(it)
                        dismiss()
                    }
                }
            }
        }
        bindingSheet!!.btnSeeRewardCode.setOnClickListener {
            if (onDialogActionListener != null) {
                rewardsData.let {
                    onDialogActionListener?.onSeeRewardCode(it)
                }
            }
        }
        bindingSheet!!.btnVisitWebsite.setOnClickListener {
            rewardsData.let {
                CommonUtils.openUrl(mActivity, it.linkToWebsite)
            }
        }
    }


    interface OnDialogActionListener {
        fun onUnlockReward(data: RewardsDetails)
        fun onActivateRewardConfirmationPress(data: RewardsDetails)
        fun onChallengeDetailsPress(data: RewardsDetails)
        fun onFavourite(data: RewardsData)
        fun onSeeRewardCode(data: RewardsDetails)
        fun onEditRewardPress(data: RewardsDetails)
    }
}