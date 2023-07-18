package com.frami.ui.followers.requestdailog

import android.app.Activity
import android.widget.FrameLayout
import androidx.databinding.DataBindingUtil
import com.frami.R
import com.frami.data.model.follower.FollowerData
import com.frami.databinding.DialogSendFollowRequestConfirmationBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog

class FollowRequestConfirmationDialog(
    private val mActivity: Activity,
    private val followerData: FollowerData,
    private val followersList: List<FollowerData> = ArrayList()
) : BottomSheetDialog(
    mActivity, R.style.BottomSheetDialogStyle
) {
    private var bindingSheet: DialogSendFollowRequestConfirmationBinding? = null
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
        this.bindingSheet = DataBindingUtil.inflate<DialogSendFollowRequestConfirmationBinding>(
            this.layoutInflater,
            R.layout.dialog_send_follow_request_confirmation,
            null,
            false
        )
        this.setContentView(bindingSheet!!.root)
        bindingSheet!!.data = followerData
        bindingSheet!!.isAcceptReject = false
        setData()
    }

    private fun setData() {
        bindingSheet!!.btnYes.setOnClickListener {
            if (onDialogActionListener != null) {
                followerData.let {
                    onDialogActionListener?.onSendFollowRequestYesPress(it)
                    dismiss()
                }
            }
        }
        bindingSheet!!.btnNo.setOnClickListener {
            onDialogActionListener?.onSendFollowRequestNoPress(followersList)
            dismiss()
        }
    }

    interface OnDialogActionListener {
        fun onSendFollowRequestYesPress(data: FollowerData)
        fun onSendFollowRequestNoPress(followersList: List<FollowerData>)
    }
}