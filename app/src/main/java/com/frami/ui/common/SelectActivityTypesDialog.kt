package com.frami.ui.common

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.widget.FrameLayout
import androidx.databinding.DataBindingUtil
import com.frami.R
import com.frami.data.model.home.ActivityTypes
import com.frami.databinding.DialogListBinding
import com.frami.ui.common.adapter.ActivityTypeWithNameAdapter
import com.frami.utils.extensions.hide
import com.frami.utils.extensions.visible
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog

class SelectActivityTypesDialog(
    private val mActivity: Context,
    private val dataList: List<ActivityTypes>,
    private val isMultiSelection: Boolean = false
) : BottomSheetDialog(
    mActivity, R.style.BottomSheetDialogStyle
), ActivityTypeWithNameAdapter.OnItemClickListener {
    private var bindingSheet: DialogListBinding? = null
    var onDialogActionListener: OnDialogActionListener? = null
    fun setListener(listener: OnDialogActionListener) {
        this.onDialogActionListener = listener
    }

    private var listAdapter: ActivityTypeWithNameAdapter? = null
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

    override fun dismiss() {
//        listAdapter?.data?.let { onDialogActionListener?.onSelectedItems(it) }
        super.dismiss()
    }

    private fun setData() {
        bindingSheet!!.llTitle.hide()
        listAdapter = ActivityTypeWithNameAdapter(dataList, this, isMultiSelection)
        bindingSheet!!.rvList.adapter = listAdapter
        bindingSheet!!.cvSearch.visible()
        bindingSheet!!.etSearchView.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun afterTextChanged(editable: Editable) {
                filter(editable.toString())
            }
        })
        if (isMultiSelection) {
            bindingSheet!!.btnDone.visible()
            bindingSheet!!.btnDone.setOnClickListener {
                if (onDialogActionListener != null) {
                    listAdapter?.data?.let { onDialogActionListener?.onSelectedItems(it) }
                    dismiss()
                }
            }
        }
    }

    private fun filter(searchString: String) {
        listAdapter!!.filter.filter(searchString)
    }

    override fun onActivityTypePress(data: ActivityTypes) {
        if (onDialogActionListener != null) {
//            if (isMultiSelection) {
//                listAdapter?.data?.let { onDialogActionListener?.onSelectedItems(it) }
//            } else {
            onDialogActionListener?.onItemSelect(data)
//            }
            dismiss()
        }
    }

    interface OnDialogActionListener {
        fun onItemSelect(data: ActivityTypes)
        fun onSelectedItems(data: List<ActivityTypes>)
    }
}