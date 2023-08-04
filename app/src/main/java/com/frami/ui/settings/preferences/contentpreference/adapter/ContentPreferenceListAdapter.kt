package com.frami.ui.settings.preferences.contentpreference.adapter

import android.util.Log
import android.view.ViewGroup
import androidx.core.text.HtmlCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.frami.R
import com.frami.data.model.home.ActivityTypes
import com.frami.data.model.lookup.ActivityTypesOption
import com.frami.databinding.ListItemMenuContentPrefrenceChildBinding
import com.frami.databinding.ListItemMenuContentPrefrenceParentBinding
import com.frami.utils.extensions.hide
import com.frami.utils.extensions.layoutInflater
import com.frami.utils.extensions.onClick
import com.frami.utils.extensions.visible
import com.google.gson.Gson

class ContentPreferenceListAdapter(
    private var listData: List<ActivityTypesOption>,
    private val mListener: OnItemClickListener? = null
) : RecyclerView.Adapter<ContentPreferenceListAdapter.ViewHolder>() {
    interface OnChildItemClickListener {
        fun onItemPressChild(parentPosition : Int, childPosition : Int)
    }
    interface OnItemClickListener {
        fun onItemPress(data: ActivityTypesOption)
    }

    var data: List<ActivityTypesOption>
        get() = listData
        set(data) {
            listData = data
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ListItemMenuContentPrefrenceParentBinding.inflate(
            parent.context.layoutInflater,
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val dataModel = listData[position]
        with(holder.itemBinding){
            data = dataModel
            val contentPreferenceListChildAdapter = ContentPreferenceListChildAdapter(dataModel.value,position,object : OnChildItemClickListener{
                override fun onItemPressChild(parentPosition: Int, childPosition: Int) {
                    Log.e("jigarLogs","icon = "+listData[parentPosition].value[childPosition].icon)
                    listData[parentPosition].value.filter { it.isSelected }.also {
                        if (it.isNullOrEmpty() || (it.isNotEmpty() && it.size != dataModel.value.size)){ // not any selected
                            isSelectShow = true
                            if (it.isNullOrEmpty()){
                                ivCheckmark.setImageResource(R.drawable.ic_select_uncheck)
                            }else{
                                ivCheckmark.setImageResource(R.drawable.ic_select_check_partially)
                            }
                        }else {
                            isSelectShow = false
                            ivCheckmark.setImageResource(R.drawable.ic_select_checked)
                        }
                    }
                }
            })
            recyclerViewChild.adapter = contentPreferenceListChildAdapter
            ivArrow.onClick {
                dataModel.isChildOpen = !dataModel.isChildOpen
                data = dataModel
            }
            dataModel.value.filter { it.isSelected }.also {
                if (it.isNullOrEmpty() || (it.isNotEmpty() && it.size != dataModel.value.size)){ // not any selected
                    isSelectShow = true
                    if (it.isNullOrEmpty()){
                        ivCheckmark.setImageResource(R.drawable.ic_select_uncheck)
                    }else{
                        ivCheckmark.setImageResource(R.drawable.ic_select_check_partially)
                    }
                }else {
                    isSelectShow = false
                    ivCheckmark.setImageResource(R.drawable.ic_select_checked)
                }
            }
            ivCheckmark.onClick {
                if (tvSelect.isVisible){
                    tvSelect.performClick()
                }else{
                    tvDeSelect.performClick()
                }
            }
            tvSelect.onClick {
                listData[position].value.map {
                    it.isSelected = true
                }
                isSelectShow = false
                ivCheckmark.setImageResource(R.drawable.ic_select_checked)
                contentPreferenceListChildAdapter.data = listData[position].value
            }
            tvDeSelect.onClick {
                listData[position].value.map {
                    it.isSelected = false
                }
                isSelectShow = true
                ivCheckmark.setImageResource(R.drawable.ic_select_uncheck)
                contentPreferenceListChildAdapter.data = listData[position].value
            }
        }
    }

    override fun getItemCount(): Int {
        return listData.size
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }


    inner class ViewHolder(var itemBinding: ListItemMenuContentPrefrenceParentBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
        init {
            itemView.setOnClickListener {
                mListener?.onItemPress(listData[adapterPosition])
            }
        }
    }

    class ContentPreferenceListChildAdapter(
        private var listData: List<ActivityTypes>,
        val parentPosition : Int,
        private val mListener: OnChildItemClickListener? = null
    ) : RecyclerView.Adapter<ContentPreferenceListChildAdapter.ViewHolder>() {

        var data: List<ActivityTypes>
            get() = listData
            set(data) {
                listData = data
                notifyDataSetChanged()
            }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val binding = ListItemMenuContentPrefrenceChildBinding.inflate(parent.context.layoutInflater,parent,false)
            return ViewHolder(binding)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val dataModel = listData[position]
            with(holder.itemBinding) {
                data = dataModel
                ivCheckmark.onClick {
                    dataModel.isSelected = !dataModel.isSelected
                    data = dataModel
                    mListener?.onItemPressChild(parentPosition,position)
                }
            }

        }

        override fun getItemCount(): Int {
            return listData.size
        }

        override fun getItemId(position: Int): Long {
            return listData[position].key.toLong()
        }


        inner class ViewHolder(var itemBinding: ListItemMenuContentPrefrenceChildBinding) :
            RecyclerView.ViewHolder(itemBinding.root)
    }
}