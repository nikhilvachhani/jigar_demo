package com.frami.ui.common

import android.content.Context
import android.text.TextUtils
import android.widget.FrameLayout
import androidx.appcompat.content.res.AppCompatResources
import androidx.databinding.DataBindingUtil
import com.frami.R
import com.frami.data.model.privacycontrol.PrivacyControlData
import com.frami.databinding.DialogListBinding
import com.frami.ui.common.adapter.PrivacyControlListAdapter
import com.frami.utils.extensions.visible
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog

class SelectPrivacyControlDialog(
    private val mActivity: Context,
    private val dataList: List<PrivacyControlData>,
    private var forWhom: String = "",
    private var guide: String? = null,
) : BottomSheetDialog(
    mActivity, R.style.BottomSheetDialogStyle
), PrivacyControlListAdapter.OnItemClickListener {
    private var bindingSheet: DialogListBinding? = null
    var onDialogActionListener: OnDialogActionListener? = null
    fun setListener(listener: OnDialogActionListener) {
        this.onDialogActionListener = listener
    }

    private var listAdapter: PrivacyControlListAdapter? = null
    override fun show() {
        super.show()
        val bottomSheet =
            findViewById<FrameLayout>(com.google.android.material.R.id.design_bottom_sheet)
        if (bottomSheet != null) {
            BottomSheetBehavior.from(bottomSheet).state = BottomSheetBehavior.STATE_EXPANDED
        }
    }

    init {
        this.bindingSheet = DataBindingUtil.inflate<DialogListBinding>(
            this.layoutInflater,
            R.layout.dialog_list,
            null,
            false
        )
        this.setContentView(bindingSheet!!.root)
        setData()
    }

    private fun setData() {
        bindingSheet!!.clBottomSheet.background =
            AppCompatResources.getDrawable(mActivity, R.drawable.bg_bottom_sheet_light_bg)
        listAdapter = PrivacyControlListAdapter(dataList, this, isShowDivider = true)
        bindingSheet!!.rvList.adapter = listAdapter
        bindingSheet!!.llTitle.visible()
        if (!TextUtils.isEmpty(guide)) {
            bindingSheet!!.tvTitle.visible()
            bindingSheet!!.tvTitle.text = guide
        }
    }

    override fun onItemPress(data: PrivacyControlData) {
        if (onDialogActionListener != null) {
            onDialogActionListener?.onItemSelect(data, forWhom)
            dismiss()
        }
    }

    interface OnDialogActionListener {
        fun onItemSelect(data: PrivacyControlData, forWhom: String)
    }

}