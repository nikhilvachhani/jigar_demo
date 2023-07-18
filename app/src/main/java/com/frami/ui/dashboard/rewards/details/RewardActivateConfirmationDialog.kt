package com.frami.ui.dashboard.rewards.details

import android.app.Activity
import android.widget.FrameLayout
import androidx.databinding.DataBindingUtil
import com.frami.R
import com.frami.data.model.rewards.RewardsDetails
import com.frami.databinding.DialogRewardActivateConfirmationBinding
import com.frami.utils.CommonUtils
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog

class RewardActivateConfirmationDialog(
    private val mActivity: Activity,
    private val rewardsData: RewardsDetails
) : BottomSheetDialog(
    mActivity, R.style.BottomSheetDialogStyle
) {
    private var bindingSheet: DialogRewardActivateConfirmationBinding? = null
    var onDialogActionListener: OnDialogActionListener? = null
    fun setListener(listener: OnDialogActionListener) {
        this.onDialogActionListener = listener
    }

    override fun show() {
        super.show()
        val bottomSheet =
            findViewById<FrameLayout>(com.google.android.material.R.id.design_bottom_sheet)
        if (bottomSheet != null) {
            BottomSheetBehavior.from(bottomSheet).state = BottomSheetBehavior.STATE_EXPANDED
        }
    }

    init {
//        val view = this.layoutInflater.inflate(R.layout.dialog_challenge_details, null)
//        this.setContentView(view)
        this.bindingSheet = DataBindingUtil.inflate<DialogRewardActivateConfirmationBinding>(
            this.layoutInflater,
            R.layout.dialog_reward_activate_confirmation,
            null,
            false
        )
        this.setContentView(bindingSheet!!.root)
        bindingSheet!!.data = rewardsData
        setData()
    }

    private fun setData() {
        bindingSheet!!.cvRewardsImage.setOnClickListener {
            rewardsData.let {
                if (it.rewardImagesUrl.isNotEmpty()) {
                    CommonUtils.showZoomImage(mActivity, it.rewardImagesUrl[0])
                }
            }
        }
        bindingSheet!!.btnActivate.setOnClickListener {
            if (onDialogActionListener != null) {
                rewardsData.let {
                    onDialogActionListener?.onActivateRewardPress(it)
                    dismiss()
                }
            }
        }
        bindingSheet!!.btnGoBack.setOnClickListener {
            dismiss()
        }
    }


    interface OnDialogActionListener {
        fun onActivateRewardPress(data: RewardsDetails)
    }
}