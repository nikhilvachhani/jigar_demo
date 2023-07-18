package com.frami.ui.events.category.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.frami.R
import com.frami.data.model.explore.EventsData
import com.frami.data.model.explore.ViewTypes
import com.frami.databinding.ListItemViewTypesBinding
import com.frami.ui.dashboard.explore.adapter.EventsAdapter
import com.frami.utils.AppConstants


class EventCategoryListAdapter(
    private var listData: List<ViewTypes>,
    private val mListener: OnItemClickListener? = null
) : RecyclerView.Adapter<EventCategoryListAdapter.ViewHolderViewTypes>(),
    EventsAdapter.OnItemClickListener {
    interface OnItemClickListener {
        fun onEmptyPreferencesPress()
        fun onShowAllPress(data: ViewTypes)
        fun showEventPopUp(data: EventsData)
        fun showEventDetails(data: EventsData)
    }

    private var layoutInflater: LayoutInflater? = null
    var data: List<ViewTypes>
        get() = listData
        set(data) {
            listData = data
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderViewTypes {
        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(parent.context)
        }
        val bindingHandle: ListItemViewTypesBinding =
            DataBindingUtil.inflate(
                (layoutInflater)!!,
                R.layout.list_item_view_types,
                parent,
                false
            )
        return ViewHolderViewTypes(bindingHandle)
    }

    override fun onBindViewHolder(holder: ViewHolderViewTypes, position: Int) {
        when (holder.itemViewType) {
            AppConstants.EXPLORE_VIEW_TYPE.EVENTS -> {
                val viewHolderHeader = holder as ViewHolderViewTypes
                val data = listData[position]
                viewHolderHeader.itemBinding.viewTypes = data
                viewHolderHeader.itemBinding.recyclerView.adapter =
                    EventsAdapter(data.data as List<EventsData>, this, false, data)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return listData[position].viewType
    }

    override fun getItemCount(): Int {
        return listData.size
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    inner class ViewHolderViewTypes(var itemBinding: ListItemViewTypesBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
        init {
            itemBinding.tvShowAll.setOnClickListener(View.OnClickListener {
                mListener?.onShowAllPress(listData[adapterPosition])
            })
            itemBinding.emptyView.tvClickable.setOnClickListener {
                mListener?.onEmptyPreferencesPress()
            }
        }
    }

    override fun showEventPopUp(data: EventsData) {
        mListener?.showEventPopUp(data)
    }

    override fun showEventDetails(data: EventsData) {
        mListener?.showEventDetails(data)
    }

    override fun onViewAllPress(viewType: ViewTypes?) {
        viewType?.let { mListener?.onShowAllPress(it) }
    }
}