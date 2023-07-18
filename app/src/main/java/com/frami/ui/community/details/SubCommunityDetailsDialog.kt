package com.frami.ui.community.details

import android.content.Context
import android.widget.FrameLayout
import androidx.databinding.DataBindingUtil
import com.frami.R
import com.frami.data.model.community.subcommunity.SubCommunityData
import com.frami.data.model.invite.ParticipantData
import com.frami.databinding.DialogSubCommunityDetailsBinding
import com.frami.ui.challenges.details.adapter.ParticipantAdapter
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog

class SubCommunityDetailsDialog(
    private val mActivity: Context,
    private val subCommunityData: SubCommunityData
) : BottomSheetDialog(
    mActivity, R.style.BottomSheetDialogStyle
), ParticipantAdapter.OnItemClickListener {
    private var bindingSheet: DialogSubCommunityDetailsBinding? = null
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
        this.bindingSheet = DataBindingUtil.inflate<DialogSubCommunityDetailsBinding>(
            this.layoutInflater,
            R.layout.dialog_sub_community_details,
            null,
            false
        )
        this.setContentView(bindingSheet!!.root)
        bindingSheet!!.data = subCommunityData
        setData()
    }

    private fun setData() {
        bindingSheet!!.layoutCommunityDetails.rvParticipant.adapter =
            ParticipantAdapter(subCommunityData.invitedPeoples, this)
        bindingSheet!!.btnJoinCommunity.setOnClickListener {
            if (onDialogActionListener != null) {
                onDialogActionListener?.onSubCommunityJoinPress(subCommunityData)
                dismiss()
            }
        }
    }

    interface OnDialogActionListener {
        fun onSubCommunityJoinPress(data: SubCommunityData)
        fun onParticipantPress(data: SubCommunityData)
    }

    override fun onParticipantPress(data: ParticipantData) {
        this.dismiss()
        onDialogActionListener?.onParticipantPress(subCommunityData)
    }
}