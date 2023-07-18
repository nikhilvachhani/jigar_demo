package com.frami.ui.community.create.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.frami.R
import com.frami.data.model.community.request.ApplicableCodeData
import com.frami.data.model.community.request.DomainData
import com.frami.databinding.ListItemApplauseBinding
import com.frami.databinding.ListItemApplicableCodeBinding
import com.frami.databinding.ListItemDomainBinding

class ApplicableCodeAdapter(
    private var listData: List<ApplicableCodeData>,
    private var mListener: OnItemClickListener? = null,
) : RecyclerView.Adapter<ApplicableCodeAdapter.ViewHolder>() {
    interface OnItemClickListener {
        fun onRemoveCodeItemPress(position: Int)
    }

    private var layoutInflater: LayoutInflater? = null
    var data: List<ApplicableCodeData>
        get() = listData
        set(data) {
            listData = data
            notifyDataSetChanged()
        }

    fun addData(domainData: ApplicableCodeData) {
        val domainList: MutableList<ApplicableCodeData> = ArrayList()
        domainList.addAll(data)
        domainList.add(domainData)
        data = domainList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ApplicableCodeAdapter.ViewHolder {
        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(parent.context)
        }
        val binding: ListItemApplicableCodeBinding =
            DataBindingUtil.inflate(
                (layoutInflater)!!,
                R.layout.list_item_applicable_code,
                parent,
                false
            )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ApplicableCodeAdapter.ViewHolder, position: Int) {
        val data = listData[position]
        holder.itemBinding.data = data
    }

    override fun getItemCount(): Int {
        return listData.size
    }

    override fun getItemId(position: Int): Long {
        return listData[position].communityCode.toLong()
    }


    inner class ViewHolder(var itemBinding: ListItemApplicableCodeBinding) :
        RecyclerView.ViewHolder(
            itemBinding.root
        ) {
        init {
            itemBinding.ivClose.setOnClickListener {
                mListener?.onRemoveCodeItemPress(adapterPosition)
            }
        }
    }
}