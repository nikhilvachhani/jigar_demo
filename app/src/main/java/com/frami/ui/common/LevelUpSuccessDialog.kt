package com.frami.ui.common

import android.app.Activity
import android.os.Handler
import android.os.Looper
import android.widget.FrameLayout
import androidx.databinding.DataBindingUtil
import com.frami.R
import com.frami.data.model.challenge.competitor.RelatedItemData
import com.frami.data.model.rewards.Organizer
import com.frami.databinding.DialogLevelUpBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog

class LevelUpSuccessDialog(
    private val mActivity: Activity,
    private val relatedItemData: RelatedItemData? = null
) : BottomSheetDialog(
    mActivity, R.style.BottomSheetDialogStyle
) {
    private var bindingSheet: DialogLevelUpBinding? = null
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
        this.bindingSheet = DataBindingUtil.inflate<DialogLevelUpBinding>(
            this.layoutInflater,
            R.layout.dialog_level_up,
            null,
            false
        )
        this.setContentView(bindingSheet!!.root)
        setData()

        Handler(Looper.getMainLooper()).postDelayed({
            this.dismiss()
        }, 5000)
    }

    private fun setData() {
        relatedItemData?.let {
            bindingSheet?.tvNewLevelAchieved?.setText(
                String.format(
                    mActivity.getString(
                        R.string.level_achieved,
                        it.level
                    )
                )
            )
        }
    }

    interface OnDialogActionListener {
        fun onItemSelect(data: Organizer)
    }
}