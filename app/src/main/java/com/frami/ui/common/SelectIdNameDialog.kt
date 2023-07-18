package com.frami.ui.common

import android.content.Context
import android.text.TextUtils
import android.widget.FrameLayout
import androidx.databinding.DataBindingUtil
import com.frami.R
import com.frami.data.model.common.IdNameData
import com.frami.databinding.DialogListBinding
import com.frami.ui.common.adapter.IdNameListAdapter
import com.frami.utils.extensions.hide
import com.frami.utils.extensions.visible
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog

class SelectIdNameDialog(
    private val mActivity: Context,
    private val dataList: List<IdNameData>,
    private var forWhom: String = "",
    private var guide: String? = null,
) : BottomSheetDialog(
    mActivity, R.style.BottomSheetDialogStyle
), IdNameListAdapter.OnItemClickListener {
    private var bindingSheet: DialogListBinding? = null
    var onDialogActionListener: OnDialogActionListener? = null
    fun setListener(listener: OnDialogActionListener) {
        this.onDialogActionListener = listener
    }

    private var listAdapter: IdNameListAdapter? = null
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
        bindingSheet!!.llTitle.hide()
        listAdapter = IdNameListAdapter(dataList, this)
        bindingSheet!!.rvList.adapter = listAdapter
        if (!TextUtils.isEmpty(guide)) {
            bindingSheet!!.tvGuide.visible()
            bindingSheet!!.tvGuide.text = guide
        }
    }

    override fun onItemPress(data: IdNameData) {
        if (onDialogActionListener != null) {
            onDialogActionListener?.onItemSelect(data, forWhom)
            dismiss()
        }
    }

    interface OnDialogActionListener {
        fun onItemSelect(data: IdNameData, forWhom: String)
    }

}