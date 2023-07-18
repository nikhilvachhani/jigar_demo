package com.frami.ui.dashboard.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.frami.R
import com.frami.data.model.home.ActivityTypes
import com.frami.databinding.ListItemActivityTypesBinding
import com.frami.utils.AppConstants
import com.frami.utils.CommonUtils
import com.google.gson.Gson

class ActivityTypeAdapter(
    private var listData: List<ActivityTypes>,
    private val mListener: OnItemClickListener? = null,
    private val isSelectable: Boolean = true
) : RecyclerView.Adapter<ActivityTypeAdapter.ViewHolder>() {
    interface OnItemClickListener {
        fun onActivityTypePress()
    }

    private var layoutInflater: LayoutInflater? = null
    var data: List<ActivityTypes>
        get() = listData
        set(data) {
            listData = data
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(parent.context)
        }
        val binding: ListItemActivityTypesBinding =
            DataBindingUtil.inflate(
                (layoutInflater)!!,
                R.layout.list_item_activity_types,
                parent,
                false
            )
        return ViewHolder(binding)
    }

    fun getSelectedActivityTypeCombinationNoList(): String {
        val selectedActivityType = listData.filter { it.isSelected }
        val isALL = selectedActivityType.find { it.key == AppConstants.KEYS.ALL }
        val selectedItemIds = ArrayList<Int>()
        if (isALL != null) {
            val listDataExceptAll = listData.filter { it.key != AppConstants.KEYS.ALL }
            listDataExceptAll.forEach { activityTypes -> selectedItemIds.add(activityTypes.combinationNo) }
        } else {
            selectedActivityType.forEach { activityTypes -> selectedItemIds.add(activityTypes.combinationNo) }
        }
        CommonUtils.log(
            "listOf(selectedItemIds).joinToString ${Gson().toJson(selectedActivityType)} ${
                Gson().toJson(
                    selectedItemIds.joinToString()
                )
            }"
        )
        return selectedItemIds.joinToString()
    }

    fun getSelectedActivityTypeKeyList(): String {
        val selectedActivityType = listData.filter { it.isSelected }
        val isALL = selectedActivityType.find { it.key == AppConstants.KEYS.ALL }
        val selectedItemIds = ArrayList<String>()
        if (isALL != null) {
            val listDataExceptAll = listData.filter { it.key != AppConstants.KEYS.ALL }
            listDataExceptAll.forEach { activityTypes -> selectedItemIds.add(activityTypes.key) }
        } else {
            selectedActivityType.forEach { activityTypes -> selectedItemIds.add(activityTypes.key) }
        }
        CommonUtils.log(
            "listOf(selectedItemIds).joinToString ${Gson().toJson(selectedActivityType)} ${
                Gson().toJson(
                    selectedItemIds.joinToString()
                )
            }"
        )
        return selectedItemIds.joinToString().replace(" ","")
    }

    fun isAllSelected(): Boolean {
        val listDataExceptAll = listData.filter { it.key != AppConstants.KEYS.ALL }
        val listDataExceptAllSelected = listDataExceptAll.filter { it.isSelected }

        return listDataExceptAll.size == listDataExceptAllSelected.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = listData[position]
        holder.itemBinding.activityTypes = data
        holder.itemBinding.isSelectable = isSelectable
    }

    override fun getItemCount(): Int {
        return listData.size
    }

    override fun getItemId(position: Int): Long {
        return listData[position].key.toLong()
    }

    inner class ViewHolder(var itemBinding: ListItemActivityTypesBinding) : RecyclerView.ViewHolder(
        itemBinding.root
    ) {
        init {
            itemView.setOnClickListener {
                val data = listData[adapterPosition]
                if (isSelectable) {
                    listData.forEachIndexed { index, activityTypes ->
                        activityTypes.isSelected = index == adapterPosition
                    }.apply {
                        notifyDataSetChanged()
                        mListener?.onActivityTypePress()
                    }
//                    if (adapterPosition == 0) {
//                        listData.forEachIndexed { index, activityTypes ->
//                            activityTypes.isSelected = activityTypes.key == AppConstants.KEYS.ALL
//                        }.apply {
//                            notifyDataSetChanged()
//                            mListener?.onActivityTypePress()
//                        }
//                    } else {
//                        listData.find { activityTypes -> activityTypes.key == AppConstants.KEYS.ALL }?.isSelected = false
//                        notifyItemChanged(0)
//                        data.isSelected = !data.isSelected
//                        notifyItemChanged(adapterPosition)
////                        if (isAllSelected()) {
////                            listData.forEachIndexed { index, activityTypes ->
////                                activityTypes.isSelected =
////                                    activityTypes.key == AppConstants.KEYS.ALL
////                            }.apply {
////                                notifyDataSetChanged()
////                                mListener?.onActivityTypePress()
////                            }
////                        } else {
////                        }
//                        mListener?.onActivityTypePress()
//                    }
                }
            }
        }
    }
}