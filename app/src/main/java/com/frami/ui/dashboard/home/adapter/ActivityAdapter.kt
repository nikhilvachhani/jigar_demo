package com.frami.ui.dashboard.home.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.appcompat.widget.AppCompatImageView
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.frami.R
import com.frami.data.model.home.ActivityData
import com.frami.databinding.ListItemActivityProfileBinding
import com.frami.databinding.ListItemActivityTypeActivityBinding
import com.frami.databinding.ListItemActivityTypeMarathonBinding
import com.frami.databinding.ListItemHandleBinding
import com.frami.utils.AppConstants
import java.util.*

class ActivityAdapter(
    private var listData: List<ActivityData>,
    private val mListener: OnItemClickListener? = null,
    private var isRenderFromProfile: Boolean = false
) : RecyclerView.Adapter<RecyclerView.ViewHolder>(), Filterable {
    interface OnItemClickListener {
        fun onProfileIconPress(data: ActivityData)
        fun onApplauseIconPress(data: ActivityData, adapterPosition: Int)
        fun onApplauseCountPress(data: ActivityData)
        fun onCommentIconPress(data: ActivityData)
        fun onActivityItemPress(data: ActivityData)
    }

    private var listDataUpdate: List<ActivityData>
    private var layoutInflater: LayoutInflater? = null
    var data: List<ActivityData>
        get() = listData
        set(data) {
            listData = data
            listDataUpdate = data
            notifyDataSetChanged()
        }

    fun appendData(data: List<ActivityData>) {
        val existingList: MutableList<ActivityData> = ArrayList()
        existingList.addAll(listData)
        existingList.addAll(data)
        listData = existingList
        listDataUpdate = existingList
        notifyItemRangeInserted(listData.size, listData.size + data.size)
    }

    fun notifyItem(activityData: ActivityData, adapterPosition: Int) {
        val existingList: MutableList<ActivityData> = ArrayList()
        existingList.addAll(listData)
        if (existingList.size > adapterPosition) {
            existingList[adapterPosition] = activityData
            listData = existingList
            listDataUpdate = existingList
            notifyItemChanged(adapterPosition)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(parent.context)
        }
        val bindingFromProfileActivity: ListItemActivityProfileBinding =
            DataBindingUtil.inflate(
                (layoutInflater)!!,
                R.layout.list_item_activity_profile,
                parent,
                false
            )
        val bindingActivity: ListItemActivityTypeActivityBinding =
            DataBindingUtil.inflate(
                (layoutInflater)!!,
                R.layout.list_item_activity_type_activity,
                parent,
                false
            )
        val bindingMarathon: ListItemActivityTypeMarathonBinding =
            DataBindingUtil.inflate(
                (layoutInflater)!!,
                R.layout.list_item_activity_type_marathon,
                parent,
                false
            )
        val bindingHandle: ListItemHandleBinding =
            DataBindingUtil.inflate(
                (layoutInflater)!!,
                R.layout.list_item_handle,
                parent,
                false
            )
        return when (viewType) {
            AppConstants.HOME_VIEW_TYPE.ACTIVITY -> {
                if (isRenderFromProfile)
                    ViewHolderActivityFromProfile(bindingFromProfileActivity)
                else
                    ViewHolderActivity(bindingActivity)
            }
            AppConstants.HOME_VIEW_TYPE.MARATHON -> {
                ViewHolderMarathon(bindingMarathon)
            }
            else -> {
                ViewHolderHandle(bindingHandle)
            }
        }
//        return ViewHolderActivity(binding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder.itemViewType) {
            AppConstants.HOME_VIEW_TYPE.ACTIVITY -> {
                if (isRenderFromProfile) {
                    val holderActivityFromProfile = holder as ViewHolderActivityFromProfile
                    val data = listDataUpdate[position]
                    holderActivityFromProfile.itemBinding.data = data
                    holderActivityFromProfile.itemBinding.layoutActivity.rvActivityPhoto.adapter =
                        ActivityPhotoAdapter(data.photoList)

                    holderActivityFromProfile.itemBinding.layoutActivity.rvAttributes.adapter =
                        ActivityAttributeAdapter(data.attributes)
                } else {
                    val holderActivity = holder as ViewHolderActivity
                    val data = listDataUpdate[position]
                    holderActivity.itemBinding.data = data
                    holderActivity.itemBinding.layoutActivity.rvActivityPhoto.adapter =
                        ActivityPhotoAdapter(data.photoList)
                    holderActivity.itemBinding.layoutActivity.rvAttributes.adapter =
                        ActivityAttributeAdapter(data.attributes)
                }
            }
            AppConstants.HOME_VIEW_TYPE.MARATHON -> {
                val holderMarathon = holder as ViewHolderMarathon
                val data = listDataUpdate[position]
                holderMarathon.itemBinding.activityData = data

                holderMarathon.itemBinding.rvAttributes.adapter =
                    ActivityAttributeAdapter(data.attributes)
            }
            AppConstants.HOME_VIEW_TYPE.HANDLE -> {
                val holderHandle = holder as ViewHolderHandle
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return listDataUpdate[position].viewType
    }

    override fun getItemCount(): Int {
        return listDataUpdate.size
    }

    override fun getItemId(position: Int): Long {
        return listDataUpdate[position].activityId.toLong()
    }


    inner class ViewHolderActivityFromProfile(var itemBinding: ListItemActivityProfileBinding) :
        RecyclerView.ViewHolder(
            itemBinding.root
        ) {
        init {
            loadClappingHands(itemView.context, itemBinding.layoutActivity.ivClap)
            itemBinding.layoutActivity.ivPhoto.setOnClickListener {
                mListener?.onProfileIconPress(listDataUpdate[adapterPosition])
            }
            itemBinding.layoutActivity.cvLikeIcon.setOnClickListener {
                mListener?.onApplauseIconPress(listDataUpdate[adapterPosition], adapterPosition)
            }
            itemBinding.layoutActivity.tvLike.setOnClickListener {
                mListener?.onApplauseCountPress(listDataUpdate[adapterPosition])
            }
            itemBinding.layoutActivity.cvCommentIcon.setOnClickListener {
                mListener?.onCommentIconPress(listDataUpdate[adapterPosition])
            }
            itemView.setOnClickListener {
                mListener?.onActivityItemPress(listDataUpdate[adapterPosition])
            }
        }
    }

    inner class ViewHolderActivity(var itemBinding: ListItemActivityTypeActivityBinding) :
        RecyclerView.ViewHolder(
            itemBinding.root
        ) {
        init {
            loadClappingHands(itemView.context, itemBinding.layoutActivity.ivClap)
            itemBinding.layoutActivity.ivPhoto.setOnClickListener {
                mListener?.onProfileIconPress(listDataUpdate[adapterPosition])
            }
            itemBinding.layoutActivity.cvLikeIcon.setOnClickListener {
                mListener?.onApplauseIconPress(listDataUpdate[adapterPosition], adapterPosition)
            }
            itemBinding.layoutActivity.tvLike.setOnClickListener {
                mListener?.onApplauseCountPress(listDataUpdate[adapterPosition])
            }
            itemBinding.layoutActivity.cvCommentIcon.setOnClickListener {
                mListener?.onCommentIconPress(listDataUpdate[adapterPosition])
            }
            itemView.setOnClickListener {
                mListener?.onActivityItemPress(listDataUpdate[adapterPosition])
            }
        }
    }

    inner class ViewHolderMarathon(var itemBinding: ListItemActivityTypeMarathonBinding) :
        RecyclerView.ViewHolder(
            itemBinding.root
        ) {
        init {
            loadClappingHands(itemView.context, itemBinding.ivClap)
            itemBinding.ivPhoto.setOnClickListener {
                mListener?.onProfileIconPress(listDataUpdate[adapterPosition])
            }
            itemBinding.ivClap.setOnClickListener {
                mListener?.onApplauseIconPress(listDataUpdate[adapterPosition], adapterPosition)
            }
            itemBinding.tvLike.setOnClickListener {
                mListener?.onApplauseCountPress(listDataUpdate[adapterPosition])
            }
            itemBinding.cvCommentIcon.setOnClickListener {
                mListener?.onCommentIconPress(listDataUpdate[adapterPosition])
            }
            itemView.setOnClickListener {
                mListener?.onActivityItemPress(listDataUpdate[adapterPosition])
            }
        }
    }

    inner class ViewHolderHandle(var itemBinding: ListItemHandleBinding) :
        RecyclerView.ViewHolder(
            itemBinding.root
        )

    private fun loadClappingHands(context: Context?, ivClap: AppCompatImageView) {
        context?.let {
            Glide.with(it).asGif().load(R.drawable.clapping_hands)
                .into(ivClap)
        }
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(charSequence: CharSequence): FilterResults {
                val charString = charSequence.toString()
                if (charString.isEmpty()) {
                    listDataUpdate = listData
                } else {
//                    val filteredList: MutableList<ActivityData> = ArrayList()
                    val filteredList = listData.filter {
                        it.userName.lowercase(Locale.getDefault())
                            .contains(charString.lowercase(Locale.getDefault())) || it.activityTitle.lowercase(
                            Locale.getDefault()
                        ).contains(charString.lowercase(Locale.getDefault()))
                    }
//                    for (row: ActivityData in listData) {
//                        if (row.userName.lowercase(Locale.getDefault())
//                                .contains(charString.lowercase(Locale.getDefault()))
//                            || row.activityTitle.lowercase(Locale.getDefault())
//                                .contains(charString.lowercase(Locale.getDefault()))
//                        ) {
//                            filteredList.add(row)
//                        }
//                    }
                    listDataUpdate = filteredList
                }
                val filterResults = FilterResults()
                filterResults.values = listDataUpdate
                return filterResults
            }

            override fun publishResults(charSequence: CharSequence, filterResults: FilterResults) {
                listDataUpdate = filterResults.values as ArrayList<ActivityData>
                notifyDataSetChanged()
            }
        }
    }

    init {
        listDataUpdate = listData
    }
}