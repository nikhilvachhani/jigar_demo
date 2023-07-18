package com.frami.ui.dashboard.rewards.details

import android.app.Activity
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context.CLIPBOARD_SERVICE
import android.util.DisplayMetrics
import android.widget.FrameLayout
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.frami.R
import com.frami.data.model.rewards.RewardsDetails
import com.frami.databinding.DialogActivateRewardCodeSuccessBinding
import com.frami.utils.CommonUtils
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog


class ActivateRewardCodeSuccessDialog(
    private val mActivity: Activity,
    private val rewardsData: RewardsDetails
) : BottomSheetDialog(
    mActivity, R.style.BottomSheetDialogStyle
) {
    private var bindingSheet: DialogActivateRewardCodeSuccessBinding? = null
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
        this.bindingSheet = DataBindingUtil.inflate<DialogActivateRewardCodeSuccessBinding>(
            this.layoutInflater,
            R.layout.dialog_activate_reward_code_success,
            null,
            false
        )
        this.setContentView(bindingSheet!!.root)
        bindingSheet!!.data = rewardsData
        mActivity.let {
            Glide.with(it).asGif().load(R.drawable.successfully).into(bindingSheet!!.ivSuccess)
        };
        setData()
    }

    private fun setData() {
        bindingSheet!!.tvCouponCode.setOnLongClickListener {
            val clipboard: ClipboardManager? =
                mActivity.getSystemService(CLIPBOARD_SERVICE) as ClipboardManager?
            val clip =
                ClipData.newPlainText("Frami Coupon Code", rewardsData.couponCode)
            clipboard?.setPrimaryClip(clip)
            if (onDialogActionListener != null) {
                onDialogActionListener?.onShowMessage("Coupon Code Copied to clipboard")
            }
            true
        }
        bindingSheet!!.btnClose.setOnClickListener {
            dismiss()
        }
        bindingSheet!!.btnVisitWebsite.setOnClickListener {
            rewardsData.let {
                CommonUtils.openUrl(mActivity, it.linkToWebsite)
            }
        }
    }


    interface OnDialogActionListener {
        fun onShowMessage(message: String)
    }
}