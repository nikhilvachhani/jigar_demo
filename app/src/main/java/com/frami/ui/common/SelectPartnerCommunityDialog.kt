package com.frami.ui.common

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.widget.FrameLayout
import androidx.databinding.DataBindingUtil
import com.frami.R
import com.frami.data.model.challenge.CompetitorData
import com.frami.data.model.rewards.Organizer
import com.frami.databinding.DialogListBinding
import com.frami.ui.challenges.details.adapter.CompetitorAdapter
import com.frami.ui.common.adapter.OrganizerListAdapter
import com.frami.utils.AppConstants
import com.frami.utils.extensions.hide
import com.frami.utils.extensions.visible
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog

class SelectPartnerCommunityDialog(
        private val mActivity: Context,
        private val dataList: List<CompetitorData>,
        private val isMultiSelection: Boolean = false
) : BottomSheetDialog(
        mActivity, R.style.BottomSheetDialogStyle
), CompetitorAdapter.OnItemClickListener {
    private var bindingSheet: DialogListBinding? = null
    var onDialogActionListener: OnDialogActionListener? = null
    fun setListener(listener: OnDialogActionListener) {
        this.onDialogActionListener = listener
    }

    private var listAdapter: CompetitorAdapter? = null
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
        listAdapter = CompetitorAdapter(dataList, this, AppConstants.COMPETITORS_ITEM_VIEW_TYPE.SELECTION)
        bindingSheet!!.rvList.adapter = listAdapter
        bindingSheet!!.cvSearch.visible()
        bindingSheet!!.etSearchView.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun afterTextChanged(editable: Editable) {
                filter(editable.toString())
            }
        })
        bindingSheet!!.btnDone.visible()
        bindingSheet!!.btnDone.setOnClickListener {
            if (onDialogActionListener != null) {
                listAdapter?.getSelectedCommunity()?.let { onDialogActionListener?.onSelectedCompetitorCommunityItems(it) }
                dismiss()
            }
        }
    }

    private fun filter(searchString: String) {
        listAdapter!!.filter.filter(searchString)
    }

    override fun onCompetitorPress(data: CompetitorData) {
//        if (onDialogActionListener != null) {
//            onDialogActionListener?.onItemSelect(data)
//            dismiss()
//        }
    }

    interface OnDialogActionListener {
        fun onItemSelect(data: CompetitorData)
        fun onSelectedCompetitorCommunityItems(data: List<CompetitorData>)
    }
}