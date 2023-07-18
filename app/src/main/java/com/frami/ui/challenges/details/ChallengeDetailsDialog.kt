package com.frami.ui.challenges.details

import android.app.Activity
import android.widget.FrameLayout
import androidx.databinding.DataBindingUtil
import com.frami.R
import com.frami.data.model.explore.ChallengesData
import com.frami.databinding.DialogChallengeDetailsBinding
import com.frami.utils.AppConstants
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog

class ChallengeDetailsDialog(
    private val mActivity: Activity,
    private val challengesData: ChallengesData
) : BottomSheetDialog(
    mActivity, R.style.BottomSheetDialogStyle
) {
    private var bindingSheet: DialogChallengeDetailsBinding? = null
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
        this.bindingSheet = DataBindingUtil.inflate<DialogChallengeDetailsBinding>(
            this.layoutInflater,
            R.layout.dialog_challenge_details,
            null,
            false
        )
        this.setContentView(bindingSheet!!.root)
        bindingSheet!!.challengesData = challengesData
        setData()
    }

    private fun setData() {
        bindingSheet!!.btnDetails.setOnClickListener {
            if (onDialogActionListener != null) {
                onDialogActionListener?.onChallengeDetailsPress(challengesData)
                dismiss()
            }
        }
        bindingSheet!!.btnJoinChallenge.setOnClickListener {
            if (onDialogActionListener != null) {
                onDialogActionListener?.onChallengeParticipantStatusChange(challengesData, AppConstants.KEYS.Joined)
                dismiss()
            }
        }
        bindingSheet!!.btnRejectChallenge.setOnClickListener {
            if (onDialogActionListener != null) {
                onDialogActionListener?.onChallengeParticipantStatusChange(challengesData, AppConstants.KEYS.Rejected)
                dismiss()
            }
        }
    }


    interface OnDialogActionListener {
        fun onChallengeParticipantStatusChange(data: ChallengesData, participantStatus: String)
        fun onChallengeDetailsPress(data: ChallengesData)
    }
}