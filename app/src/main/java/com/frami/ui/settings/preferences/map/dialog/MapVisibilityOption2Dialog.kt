package com.frami.ui.settings.preferences.map.dialog

import android.content.Context
import android.widget.FrameLayout
import androidx.databinding.DataBindingUtil
import com.frami.R
import com.frami.databinding.DialogMvo2HideMapsBinding
import com.frami.utils.AppConstants
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog

class MapVisibilityOption2Dialog(
    mActivity: Context,
    private val onDialogActionListener: OnDialogActionListener? = null
) : BottomSheetDialog(
    mActivity, R.style.BottomSheetDialogStyle
) {
    private var bindingSheet: DialogMvo2HideMapsBinding? = null
    override fun show() {
        super.show()
        val bottomSheet =
            findViewById<FrameLayout>(com.google.android.material.R.id.design_bottom_sheet)
        if (bottomSheet != null) {
            BottomSheetBehavior.from(bottomSheet).state = BottomSheetBehavior.STATE_EXPANDED
        }
    }

    init {
        this.bindingSheet = DataBindingUtil.inflate<DialogMvo2HideMapsBinding>(
            this.layoutInflater,
            R.layout.dialog_mvo_2_hide_maps,
            null,
            false
        )
        this.setContentView(bindingSheet!!.root)
        setData()
        clickListener()
    }

    private fun clickListener() {
        bindingSheet!!.tvYes.setOnClickListener {
            bindingSheet!!.isYes = AppConstants.ISYES.YES
        }
        bindingSheet!!.tvNo.setOnClickListener {
            bindingSheet!!.isYes = AppConstants.ISYES.NO
        }
    }

    private fun setData() {
    }

    interface OnDialogActionListener {
        fun onLoginDialogClose()
    }
}