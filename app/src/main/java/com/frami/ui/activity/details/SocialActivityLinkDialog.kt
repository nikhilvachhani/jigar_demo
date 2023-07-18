package com.frami.ui.activity.details

import android.content.Context
import android.widget.FrameLayout
import androidx.databinding.DataBindingUtil
import com.frami.R
import com.frami.data.model.common.IdNameData
import com.frami.data.model.home.ActivityData
import com.frami.databinding.DialogSocialActivityLinkBinding
import com.frami.ui.common.adapter.IdNameListAdapter
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog

class SocialActivityLinkDialog(
    private val mActivity: Context,
    private val activityData: ActivityData
) : BottomSheetDialog(
    mActivity, R.style.BottomSheetDialogStyle
), IdNameListAdapter.OnItemClickListener {
    private var bindingSheet: DialogSocialActivityLinkBinding? = null
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
        this.bindingSheet = DataBindingUtil.inflate<DialogSocialActivityLinkBinding>(
            this.layoutInflater,
            R.layout.dialog_social_activity_link,
            null,
            false
        )
        this.setContentView(bindingSheet!!.root)
        setData()
    }

    private fun setData() {
        bindingSheet?.data = activityData
        bindingSheet?.btnNew!!.setOnClickListener {
            if (onDialogActionListener != null) {
                onDialogActionListener?.onNewActivityPress(activityData)
                dismiss()
            }
        }
        bindingSheet?.btnLink!!.setOnClickListener {
            if (onDialogActionListener != null) {
                onDialogActionListener?.onLinkActivityPress(activityData)
                dismiss()
            }
        }
    }


    override fun onItemPress(data: IdNameData) {
        if (onDialogActionListener != null) {
//            onDialogActionListener?.onPeriodSelect(from, data)
            dismiss()
        }
    }

    interface OnDialogActionListener {
        fun onNewActivityPress(data: ActivityData)
        fun onLinkActivityPress(data: ActivityData)
    }

}