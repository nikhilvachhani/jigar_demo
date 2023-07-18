package com.frami.ui.common

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.FrameLayout
import androidx.appcompat.widget.AppCompatEditText
import androidx.appcompat.widget.AppCompatTextView
import androidx.databinding.ObservableField
import androidx.recyclerview.widget.RecyclerView
import com.frami.R
import com.frami.data.model.lookup.CountryData
import com.frami.ui.common.adapter.CountryListAdapter
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.card.MaterialCardView

class SelectCountryDialog(
    private val mActivity: Context,
    private val dataList: List<CountryData>,
    private val isShowCountryCode: Boolean? = false
) : BottomSheetDialog(
    mActivity, R.style.BottomSheetDialogStyle
), CountryListAdapter.OnItemClickListener {
    var onDialogActionListener: OnDialogActionListener? = null
    fun setListener(listener: OnDialogActionListener) {
        this.onDialogActionListener = listener
    }

    private var listAdapter: CountryListAdapter? = null
    override fun show() {
        super.show()
        val bottomSheet =
            findViewById<FrameLayout>(com.google.android.material.R.id.design_bottom_sheet)
        if (bottomSheet != null) {
            BottomSheetBehavior.from(bottomSheet).state = BottomSheetBehavior.STATE_EXPANDED
        }
    }

    init {
        val view = this.layoutInflater.inflate(R.layout.dialog_list, null)
        this.setContentView(view)
        setData(view)
    }

    private fun setData(view: View) {
        val tvTitle: AppCompatTextView = view.findViewById(R.id.tvTitle)
        tvTitle.text = mActivity.getString(R.string.nationality)
        val cvSearch: MaterialCardView = view.findViewById(R.id.cvSearch)
        cvSearch.visibility = View.VISIBLE
        val etSearchView: AppCompatEditText = view.findViewById(R.id.etSearchView)
        val rvList: RecyclerView = view.findViewById(R.id.rvList)
        listAdapter = CountryListAdapter(dataList, this, isShowCountryCode == true)
        rvList.adapter = listAdapter
        etSearchView.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun afterTextChanged(editable: Editable) {
                filter(editable.toString())
            }
        })
    }

    private fun filter(searchString: String) {
        listAdapter!!.filter.filter(searchString)
    }

    override fun onCountryItemPress(data: CountryData) {
        if (onDialogActionListener != null) {
            onDialogActionListener?.onCountrySelect(data)
            dismiss()
        }
    }

    interface OnDialogActionListener {
        fun onCountrySelect(countryData: CountryData)
    }

}