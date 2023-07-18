package com.frami.ui.community.details

import android.content.Context
import android.widget.FrameLayout
import androidx.databinding.DataBindingUtil
import com.frami.R
import com.frami.data.model.explore.CommunityData
import com.frami.data.model.invite.ParticipantData
import com.frami.databinding.DialogCommunityDetailsBinding
import com.frami.ui.challenges.details.adapter.ParticipantAdapter
import com.frami.ui.dashboard.home.adapter.ActivityAttributeAdapter
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog

class CommunityDetailsDialog(
    private val mActivity: Context,
    private val communityData: CommunityData
) : BottomSheetDialog(
    mActivity, R.style.BottomSheetDialogStyle
), ParticipantAdapter.OnItemClickListener {
    private var bindingSheet: DialogCommunityDetailsBinding? = null
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
        this.bindingSheet = DataBindingUtil.inflate<DialogCommunityDetailsBinding>(
            this.layoutInflater,
            R.layout.dialog_community_details,
            null,
            false
        )
        this.setContentView(bindingSheet!!.root)
        bindingSheet!!.data = communityData
        setData()
    }

    private fun setData() {
        bindingSheet!!.layoutCommunityDetails.rvAttributes.adapter =
            communityData.attributes?.let { ActivityAttributeAdapter(it) }
        bindingSheet!!.layoutCommunityDetails.rvParticipant.adapter = ParticipantAdapter(
            communityData.invitedPeoples, this
        )
        bindingSheet!!.btnJoinCommunity.setOnClickListener {
            if (onDialogActionListener != null) {
                onDialogActionListener?.onCommunityJoinPress(communityData)
                dismiss()
            }
        }
    }

    interface OnDialogActionListener {
        fun onCommunityJoinPress(data: CommunityData)
        fun onParticipantPress(data: CommunityData)
    }

    override fun onParticipantPress(data: ParticipantData) {
        this.dismiss()
        onDialogActionListener?.onParticipantPress(communityData)
    }
}