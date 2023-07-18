package com.frami.ui.common

import android.content.Context
import android.view.View
import android.widget.FrameLayout
import androidx.databinding.DataBindingUtil
import com.frami.R
import com.frami.data.model.common.IdNameData
import com.frami.databinding.DialogListBinding
import com.frami.ui.common.adapter.IdNameListAdapter
import com.frami.utils.AppConstants
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog

class CreateDialog(
    private val mActivity: Context,
    private val dataList: List<IdNameData>,
    private val from: String = AppConstants.FROM.CREATE
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
        bindingSheet!!.tvTitle.visibility = View.VISIBLE
        bindingSheet!!.tvTitle.text = when (from) {
            AppConstants.FROM.CREATE_ACTIVITY -> mActivity.getString(R.string.activity)
            else -> mActivity.getString(R.string.excellent_what_would_you_like_to_do)
        }
        bindingSheet!!.cvSearch.visibility = View.GONE
        listAdapter = IdNameListAdapter(dataList, this, true)
        bindingSheet!!.rvList.adapter = listAdapter
//        val mDividerItemDecoration = DividerItemDecoration(
//            rvList.context,
//            DividerItemDecoration.VERTICAL
//        )
//        rvList.addItemDecoration(mDividerItemDecoration)
    }


    override fun onItemPress(data: IdNameData) {
        if (onDialogActionListener != null) {
            onDialogActionListener?.onPeriodSelect(from, data)
            dismiss()
        }
    }

    interface OnDialogActionListener {
        fun onPeriodSelect(from: String, data: IdNameData)
    }

}