package com.frami.ui.challenges.details.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.frami.R
import com.frami.data.model.invite.ParticipantData
import com.frami.databinding.ListItemParticipantBinding
import com.frami.databinding.ListItemParticipantInviteBinding
import com.frami.databinding.ListItemParticipantWithNameBinding
import com.frami.utils.AppConstants

class ParticipantAdapter(
    private var listData: List<ParticipantData>,
    private val mListener: OnItemClickListener? = null,
    private val viewType: Int = AppConstants.PARTICIPANT_ITEM_VIEW_TYPE.IMAGE,
    private val viewFrom: Int? = null,
    private val adminIds: List<String>? = ArrayList(),
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    interface OnItemClickListener {
        fun onParticipantPress(data: ParticipantData)
    }

    private var layoutInflater: LayoutInflater? = null
    private var isSpecificInviteeForChallenge: Boolean = false
    var data: List<ParticipantData>
        get() = listData
        set(data) {
            listData = data
            notifyDataSetChanged()
        }

    fun setSpecificInviteeChallenge(isSpecific: Boolean) {
        isSpecificInviteeForChallenge = isSpecific
    }

    fun getSelectedParticipantExceptLoggedInUser(): List<ParticipantData> {
        val selectedData = listData.filter { it.isSelected || it.isLoggedInUser() }
        selectedData.forEach {
            it.participantStatus =
                if (it.participantStatus == AppConstants.KEYS.NotParticipated) AppConstants.KEYS.NoResponse else it.participantStatus
            it.memberStatus =
                if (it.memberStatus == AppConstants.KEYS.NotParticipated) AppConstants.KEYS.NoResponse else it.memberStatus
        }
        return selectedData
    }

    fun getIsSelected(): List<ParticipantData> {
        return listData.filter { it.isSelected }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder {
        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(parent.context)
        }
        val bindingImage: ListItemParticipantBinding =
            DataBindingUtil.inflate(
                (layoutInflater)!!,
                R.layout.list_item_participant,
                parent,
                false
            )
        val bindingWithName: ListItemParticipantWithNameBinding =
            DataBindingUtil.inflate(
                (layoutInflater)!!,
                R.layout.list_item_participant_with_name,
                parent,
                false
            )
        val bindingInvite: ListItemParticipantInviteBinding =
            DataBindingUtil.inflate(
                (layoutInflater)!!,
                R.layout.list_item_participant_invite,
                parent,
                false
            )

        return when (this.viewType) {
            AppConstants.PARTICIPANT_ITEM_VIEW_TYPE.NAME -> ViewHolderName(bindingWithName)
            AppConstants.PARTICIPANT_ITEM_VIEW_TYPE.IMAGE -> ViewHolderImage(bindingImage)
            else -> ViewHolderInvite(bindingInvite)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val data = listData[position]
        var newHolder = holder
        if (newHolder is ViewHolderImage) {
            newHolder = holder as ViewHolderImage
            newHolder.itemBinding.data = data
            newHolder.itemBinding.size = listData.size
            newHolder.itemBinding.position = position
        } else if (newHolder is ViewHolderName) {
            newHolder = holder as ViewHolderName
            newHolder.itemBinding.data = data
            newHolder.itemBinding.size = listData.size
            newHolder.itemBinding.position = position
            newHolder.itemBinding.isFrom = viewFrom
            newHolder.itemBinding.isAdmin =
                if (adminIds.isNullOrEmpty()) false else adminIds.find { it.trim() == data.userId?.trim() } != null
//            CommonUtils.log("adminIds.find { it == data.userId.trim() } != null adminIds ${data.userId} ${adminIds?.find { it == data.userId?.trim() } != null}")
//                if (adminIds.isNullOrEmpty()) false else (data.userId == adminIds)
        } else if (newHolder is ViewHolderInvite) {
            newHolder = holder as ViewHolderInvite
            newHolder.itemBinding.data = data
            newHolder.itemBinding.size = listData.size
            newHolder.itemBinding.position = position
        }
    }

    override fun getItemCount(): Int {
        return if (viewType == AppConstants.PARTICIPANT_ITEM_VIEW_TYPE.IMAGE) if (listData.size >= 6) 6 else listData.size else listData.size
    }

    override fun getItemId(position: Int): Long {
        return listData[position].userId?.toLong() ?: 0
    }


    inner class ViewHolderImage(var itemBinding: ListItemParticipantBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
        init {
            itemBinding.btnViewAll.setOnClickListener {
                mListener?.onParticipantPress(listData[adapterPosition])
            }
            itemView.setOnClickListener {
                mListener?.onParticipantPress(listData[adapterPosition])
            }
        }
    }

    inner class ViewHolderInvite(var itemBinding: ListItemParticipantInviteBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
        init {
            itemBinding.btnInvite.setOnClickListener {
                if (!isSpecificInviteeForChallenge) {

                } else if (isSpecificInviteeForChallenge && getIsSelected().size == listData.size - 1 && !listData[adapterPosition].isSelected) {
                    Toast.makeText(
                        itemBinding.btnInvite.context,
                        "You cannot select all the users. Please remove anyone and try again",
//                        "You cannot select all the invitee while Community or Challenge Privacy is Private",//TODO add or change message
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    listData[adapterPosition].isSelected = !listData[adapterPosition].isSelected
                    notifyItemChanged(adapterPosition)
                }
            }
        }
    }

    inner class ViewHolderName(var itemBinding: ListItemParticipantWithNameBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
        init {
            itemView.setOnClickListener {
                mListener?.onParticipantPress(listData[adapterPosition])
            }
        }
    }
}