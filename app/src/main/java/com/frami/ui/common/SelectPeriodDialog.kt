package com.frami.ui.common

import android.content.Context
import android.view.View
import android.widget.FrameLayout
import androidx.databinding.DataBindingUtil
import com.frami.R
import com.frami.data.model.home.Period
import com.frami.databinding.DialogListBinding
import com.frami.ui.common.adapter.PeriodListAdapter
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog

class SelectPeriodDialog(
    private val mActivity: Context,
    private val dataList: List<Period>
) : BottomSheetDialog(
    mActivity, R.style.BottomSheetDialogStyle
), PeriodListAdapter.OnItemClickListener {
    private var bindingSheet: DialogListBinding? = null
    var onDialogActionListener: OnDialogActionListener? = null
    fun setListener(listener: OnDialogActionListener) {
        this.onDialogActionListener = listener
    }

    private var listAdapter: PeriodListAdapter? = null
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
        bindingSheet!!.llTitle.visibility = View.GONE
        bindingSheet!!.cvSearch.visibility = View.GONE
        listAdapter = PeriodListAdapter(dataList, this)
        bindingSheet!!.rvList.adapter = listAdapter
    }


    override fun onPeriodItemPress(data: Period) {
        if (onDialogActionListener != null) {
            onDialogActionListener?.onPeriodSelect(data)
            dismiss()
        }
    }

    interface OnDialogActionListener {
        fun onPeriodSelect(data: Period)
    }

}