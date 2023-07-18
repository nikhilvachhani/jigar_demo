package com.frami.ui.settings.preferences.map.dialog

import android.content.Context
import android.widget.FrameLayout
import androidx.databinding.DataBindingUtil
import com.frami.R
import com.frami.databinding.DialogMvo1HideAfterApproximateDistanceBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog

class MapVisibilityOption1Dialog(
    mActivity: Context,
    private val onDialogActionListener: OnDialogActionListener? = null
) : BottomSheetDialog(
    mActivity, R.style.BottomSheetDialogStyle
) {
    private var bindingSheet: DialogMvo1HideAfterApproximateDistanceBinding? = null
    override fun show() {
        super.show()
        val bottomSheet =
            findViewById<FrameLayout>(com.google.android.material.R.id.design_bottom_sheet)
        if (bottomSheet != null) {
            BottomSheetBehavior.from(bottomSheet).state = BottomSheetBehavior.STATE_EXPANDED
        }
    }

    init {
        this.bindingSheet = DataBindingUtil.inflate<DialogMvo1HideAfterApproximateDistanceBinding>(
            this.layoutInflater,
            R.layout.dialog_mvo_1_hide_after_approximate_distance,
            null,
            false
        )
        this.setContentView(bindingSheet!!.root)
        setData()
        clickListener()
    }

    private fun clickListener() {
        bindingSheet!!.tvOff.setOnClickListener {
            setProgress(0)

        }
        bindingSheet!!.tv400.setOnClickListener {
            setProgress(400)
        }
        bindingSheet!!.tv800.setOnClickListener {
            setProgress(800)
        }
        bindingSheet!!.tv1200.setOnClickListener {
            setProgress(1200)
        }
        bindingSheet!!.tv1600.setOnClickListener {
            setProgress(1600)
        }
    }

    private fun setProgress(progress: Int) {
        bindingSheet!!.progress = progress
    }


    private fun setData() {
    }

    interface OnDialogActionListener {
        fun onLoginDialogClose()
    }
}