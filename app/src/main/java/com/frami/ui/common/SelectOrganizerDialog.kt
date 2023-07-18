package com.frami.ui.common

import android.content.Context
import android.widget.FrameLayout
import androidx.databinding.DataBindingUtil
import com.frami.R
import com.frami.data.model.rewards.Organizer
import com.frami.databinding.DialogListBinding
import com.frami.ui.common.adapter.OrganizerListAdapter
import com.frami.utils.extensions.hide
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog

class SelectOrganizerDialog(
    private val mActivity: Context,
    private val dataList: List<Organizer>
) : BottomSheetDialog(
    mActivity, R.style.BottomSheetDialogStyle
), OrganizerListAdapter.OnItemClickListener {
    private var bindingSheet: DialogListBinding? = null
    var onDialogActionListener: OnDialogActionListener? = null
    fun setListener(listener: OnDialogActionListener) {
        this.onDialogActionListener = listener
    }

    private var listAdapter: OrganizerListAdapter? = null
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
        listAdapter = OrganizerListAdapter(dataList, this)
        bindingSheet!!.rvList.adapter = listAdapter
    }

    override fun onItemPress(data: Organizer) {
        if (onDialogActionListener != null) {
            onDialogActionListener?.onItemSelect(data)
            dismiss()
        }
    }

    interface OnDialogActionListener {
        fun onItemSelect(data: Organizer)
    }

}