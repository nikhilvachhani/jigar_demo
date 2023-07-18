package com.frami.ui.dashboard.explore.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.frami.R
import com.frami.data.model.explore.ChallengesData
import com.frami.data.model.explore.ViewTypes
import com.frami.databinding.ListItemChallengesBinding
import com.frami.databinding.ListItemChallengesExploreBinding
import com.frami.utils.CommonUtils
import java.util.*

class ChallengesAdapter(
    private var listData: List<ChallengesData>,
    private val mListener: OnItemClickListener? = null,
    private var isItemFullWidth: Boolean,
    private var viewType: ViewTypes? = null,
) : RecyclerView.Adapter<RecyclerView.ViewHolder>(), Filterable {
    interface OnItemClickListener {
        fun showChallengePopup(data: ChallengesData)
        fun onViewAllPress(viewType: ViewTypes?)
        fun showChallengeDetails(data: ChallengesData)
    }

    private var listDataUpdate: List<ChallengesData>
    var data: List<ChallengesData>
        get() = listData
        set(data) {
            listData = data
            listDataUpdate = data
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
//        parent.layoutParams.width = ((parent.width * 0.95).roundToInt())
//        val layoutParams: ViewGroup.LayoutParams = parent.layoutParams
//        layoutParams.width = parent.width
//        parent.layoutParams = layoutParams

        val binding: ListItemChallengesBinding =
            DataBindingUtil.inflate(
                (LayoutInflater.from(parent.context))!!,
                R.layout.list_item_challenges,
                parent,
                false
            )
        val bindingExplore: ListItemChallengesExploreBinding =
            DataBindingUtil.inflate(
                (LayoutInflater.from(parent.context))!!,
                R.layout.list_item_challenges_explore,
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
        val newViewHolder: RecyclerView.ViewHolder
        if (isItemFullWidth) {
            newViewHolder = holder as ViewHolder
            newViewHolder.itemBinding.challengesData = data
        } else {
            newViewHolder = holder as ViewHolderExplore
            newViewHolder.itemBinding.challengesData = data
        }
        CommonUtils.log("isJoinStatusShow ${data.isJoinStatusShow()}")
    }

    override fun getItemCount(): Int {
        return listDataUpdate.size
    }

    override fun getItemId(position: Int): Long {
        return listDataUpdate[position].challengeId.toLong()
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(charSequence: CharSequence): FilterResults {
                val charString = charSequence.toString()
                if (charString.isEmpty()) {
                    listDataUpdate = listData
                } else {
                    val filteredList: MutableList<ChallengesData> = ArrayList()
                    for (row: ChallengesData in listData) {
                        if (row.challengeName.lowercase(Locale.getDefault())
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
                listDataUpdate = filterResults.values as ArrayList<ChallengesData>
                notifyDataSetChanged()
            }
        }
    }


    inner class ViewHolder(var itemBinding: ListItemChallengesBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
        init {
            itemView.setOnClickListener {
                if (listDataUpdate[adapterPosition].isAbleToShowPopup()) {
                    mListener?.showChallengePopup(listDataUpdate[adapterPosition])
                } else {
                    mListener?.showChallengeDetails(listDataUpdate[adapterPosition])
                }
            }
        }
    }

    inner class ViewHolderExplore(var itemBinding: ListItemChallengesExploreBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
        init {
            itemView.setOnClickListener {
                if (listDataUpdate[adapterPosition].challengeId.isNotEmpty()) {
                    if (listDataUpdate[adapterPosition].isAbleToShowPopup()) {
                        mListener?.showChallengePopup(listDataUpdate[adapterPosition])
                    } else {
                        mListener?.showChallengeDetails(listDataUpdate[adapterPosition])
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