package com.frami.ui.dashboard.explore.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.frami.R
import com.frami.data.model.explore.EventsData
import com.frami.data.model.explore.ViewTypes
import com.frami.databinding.ListItemEventsBinding
import com.frami.databinding.ListItemEventsExploreBinding
import java.util.*

class EventsAdapter(
    private var listData: List<EventsData>,
    private val mListener: OnItemClickListener? = null,
    private var isItemFullWidth: Boolean = true,
    private var viewType: ViewTypes? = null,
) : RecyclerView.Adapter<RecyclerView.ViewHolder>(), Filterable {
    interface OnItemClickListener {
        fun showEventPopUp(data: EventsData)
        fun showEventDetails(data: EventsData)
        fun onViewAllPress(viewType: ViewTypes?)
    }

    private var listDataUpdate: List<EventsData>
    var data: List<EventsData>
        get() = listData
        set(data) {
            listData = data
            listDataUpdate = data
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding: ListItemEventsBinding =
            DataBindingUtil.inflate(
                (LayoutInflater.from(parent.context))!!,
                R.layout.list_item_events,
                parent,
                false
            )
        val bindingExplore: ListItemEventsExploreBinding =
            DataBindingUtil.inflate(
                (LayoutInflater.from(parent.context))!!,
                R.layout.list_item_events_explore,
                parent,
                false
            )
        return if (isItemFullWidth)
            ViewHolder(binding)
        else
            ViewHolderExplore(bindingExplore)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val data = listDataUpdate[position]
        var newViewHolder = holder
        if (isItemFullWidth) {
            newViewHolder = holder as ViewHolder
            newViewHolder.itemBinding.eventsData = data
        } else {
            newViewHolder = holder as ViewHolderExplore
            newViewHolder.itemBinding.eventsData = data
        }
    }

    override fun getItemCount(): Int {
        return listDataUpdate.size
    }

    override fun getItemId(position: Int): Long {
        return listDataUpdate[position].eventId.toLong()
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(charSequence: CharSequence): FilterResults {
                val charString = charSequence.toString()
                if (charString.isEmpty()) {
                    listDataUpdate = listData
                } else {
                    val filteredList: MutableList<EventsData> = ArrayList()
                    for (row: EventsData in listData) {
                        if (row.eventName.lowercase(Locale.getDefault())
                                .contains(charString.lowercase(Locale.getDefault())) //                                || row.getCode().contains(charSequence)
                        ) {
                            filteredList.add(row)
                        }
                    }
                    listDataUpdate = filteredList
                }
                val filterResults = FilterResults()
                filterResults.values = listDataUpdate
                return filterResults
            }

            override fun publishResults(charSequence: CharSequence, filterResults: FilterResults) {
                listDataUpdate = filterResults.values as ArrayList<EventsData>
                notifyDataSetChanged()
            }
        }
    }

    inner class ViewHolder(var itemBinding: ListItemEventsBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
        init {
            itemView.setOnClickListener {
                if (listDataUpdate[adapterPosition].isAbleToShowPopup()) {
                    mListener?.showEventPopUp(listDataUpdate[adapterPosition])
                } else {
                    mListener?.showEventDetails(listDataUpdate[adapterPosition])
                }
            }
        }
    }

    inner class ViewHolderExplore(var itemBinding: ListItemEventsExploreBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
        init {
            itemView.setOnClickListener {
                if (listData[adapterPosition].eventId.isNotEmpty()) {
                    if (listDataUpdate[adapterPosition].isAbleToShowPopup()) {
                        mListener?.showEventPopUp(listDataUpdate[adapterPosition])
                    } else {
                        mListener?.showEventDetails(listDataUpdate[adapterPosition])
                    }
                } else {
                    mListener?.onViewAllPress(viewType)
                }
            }
        }
    }

    init {
        listDataUpdate = listData
    }
}