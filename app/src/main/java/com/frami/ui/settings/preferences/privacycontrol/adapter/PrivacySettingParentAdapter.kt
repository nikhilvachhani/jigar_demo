package com.frami.ui.settings.preferences.privacycontrol.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.frami.R
import com.frami.data.model.lookup.user.SubSectionData
import com.frami.data.model.lookup.user.UserOptionsResponseData
import com.frami.databinding.ListItemPrivacySettingsChildBinding
import com.frami.databinding.ListItemPrivacySettingsParentBinding
import com.frami.utils.extensions.layoutInflater
import com.frami.utils.extensions.onClick

class PrivacySettingParentAdapter(
    private var listData: List<UserOptionsResponseData>,
    private val mListener: OnItemClickListener? = null
) : RecyclerView.Adapter<PrivacySettingParentAdapter.ViewHolder>() {
    interface OnItemClickListener {
        fun onUpdatePreferenceItem(parentPosition: Int,data: List<SubSectionData>,adapterPosition: Int)
    }

    var data: List<UserOptionsResponseData>
        get() = listData
        set(data) {
            listData = data
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ListItemPrivacySettingsParentBinding.inflate(parent.context.layoutInflater,parent,false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = listData[position]
        holder.itemBinding.data = data
        data.subsectionList.let {
            val privacySettingChildAdapter = PrivacySettingChildAdapter(position, it, mListener)
            holder.itemBinding.recyclerView.adapter = privacySettingChildAdapter
        }
    }

    override fun getItemCount(): Int {
        return listData.size
    }

    override fun getItemId(position: Int): Long {
        return listData[position].sectionKey?.toLong() ?: 0L
    }


    inner class ViewHolder(var itemBinding: ListItemPrivacySettingsParentBinding) : RecyclerView.ViewHolder(itemBinding.root)

    fun notifySubItem(parentPosition: Int, position: Int, checked: Boolean) {
        data[parentPosition].subsectionList.map {
            it.isSelected = false
        }
        data[parentPosition].subsectionList[position].isSelected = checked
    }

    class PrivacySettingChildAdapter(
        private val parentPosition: Int,
        private var listData: List<SubSectionData>,
        private val mListener: OnItemClickListener? = null
    ) : RecyclerView.Adapter<PrivacySettingChildAdapter.ViewHolder>() {

        var data: List<SubSectionData>
            get() = listData
            set(data) {
                listData = data
                notifyDataSetChanged()
            }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val binding = ListItemPrivacySettingsChildBinding.inflate(parent.context.layoutInflater,parent,false)
            return ViewHolder(binding)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val model = listData[position]
            with(holder.itemBinding){
                data = model
                if (model.isSelected){
                    imgRB.setImageResource(R.drawable.ic_radio_check)
                }else{
                    imgRB.setImageResource(R.drawable.ic_radio_uncheck)
                }
            }
        }

        override fun getItemCount(): Int {
            return listData.size
        }

        override fun getItemId(position: Int): Long {
            return listData[position].key.toLong()
        }


        inner class ViewHolder(var itemBinding: ListItemPrivacySettingsChildBinding) :
            RecyclerView.ViewHolder(
                itemBinding.root
            ) {
            init {
                itemBinding.root.onClick {
                    val find = listData.indexOfFirst { it.isSelected }
                    if (find > -1){
                        listData[find].isSelected = false
                        notifyItemChanged(find)
                    }
                    listData[adapterPosition].isSelected = true
                    notifyItemChanged(adapterPosition)
                    mListener?.onUpdatePreferenceItem(parentPosition,listData,adapterPosition)
                }
            }
        }
    }
}