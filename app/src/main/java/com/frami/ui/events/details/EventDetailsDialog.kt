package com.frami.ui.events.details

import android.content.Context
import android.widget.FrameLayout
import androidx.databinding.DataBindingUtil
import com.frami.R
import com.frami.data.model.explore.EventsData
import com.frami.databinding.DialogEventDetailsBinding
import com.frami.utils.AppConstants
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog

class EventDetailsDialog(
    private val mActivity: Context,
    private val eventsData: EventsData
) : BottomSheetDialog(
    mActivity, R.style.BottomSheetDialogStyle
) {
    private var bindingSheet: DialogEventDetailsBinding? = null
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
        this.bindingSheet = DataBindingUtil.inflate<DialogEventDetailsBinding>(
            this.layoutInflater,
            R.layout.dialog_event_details,
            null,
            false
        )
        this.setContentView(bindingSheet!!.root)
        bindingSheet!!.data = eventsData
        setData()
    }

    private fun setData() {
        bindingSheet!!.btnDetails.setOnClickListener {
            if (onDialogActionListener != null) {
                onDialogActionListener?.onViewEventPress(eventsData)
                dismiss()
            }
        }
        bindingSheet!!.btnJoin.setOnClickListener {
            if (onDialogActionListener != null) {
                onDialogActionListener?.onEventParticipantStatusChange(
                    eventsData,
                    AppConstants.KEYS.Accepted
                )
                dismiss()
            }
        }
        bindingSheet!!.btnMayBe.setOnClickListener {
            if (onDialogActionListener != null) {
                onDialogActionListener?.onEventParticipantStatusChange(
                    eventsData,
                    AppConstants.KEYS.Maybe
                )
                dismiss()
            }
        }
        bindingSheet!!.btnRejectEvent.setOnClickListener {
            if (onDialogActionListener != null) {
                onDialogActionListener?.onEventParticipantStatusChange(
                    eventsData,
                    AppConstants.KEYS.Rejected
                )
                dismiss()
            }
        }
    }

    interface OnDialogActionListener {
        fun onEventParticipantStatusChange(data: EventsData, participantStatus: String)
        fun onViewEventPress(data: EventsData)
    }
}