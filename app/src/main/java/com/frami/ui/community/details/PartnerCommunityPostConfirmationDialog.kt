package com.frami.ui.community.details

import android.content.Context
import android.widget.FrameLayout
import androidx.databinding.DataBindingUtil
import com.frami.R
import com.frami.data.model.post.MediaUrlsData
import com.frami.data.model.post.PostData
import com.frami.databinding.DialogPartnerCommunityPostDetailsBinding
import com.frami.ui.post.adapter.PostMediaAdapter
import com.frami.utils.AppConstants
import com.frami.utils.CommonUtils
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog

class PartnerCommunityPostConfirmationDialog(
    private val mActivity: Context,
    private val postData: PostData,
    private val relatedItemData: String?
) : BottomSheetDialog(
    mActivity, R.style.BottomSheetDialogStyle
), PostMediaAdapter.OnItemClickListener {
    private var bindingSheet: DialogPartnerCommunityPostDetailsBinding? = null
    var onDialogActionListener: OnDialogActionListener? = null
    fun setListener(listener: OnDialogActionListener) {
        this.onDialogActionListener = listener
    }

    override fun show() {
        super.show()
        val bottomSheet =
            findViewById<FrameLayout>(com.google.android.material.R.id.design_bottom_sheet)
        if (bottomSheet != null) {
            BottomSheetBehavior.from(bottomSheet).state = BottomSheetBehavior.STATE_EXPANDED
        }
    }

    init {
        this.bindingSheet = DataBindingUtil.inflate<DialogPartnerCommunityPostDetailsBinding>(
            this.layoutInflater,
            R.layout.dialog_partner_community_post_details,
            null,
            false
        )
        this.setContentView(bindingSheet!!.root)
        bindingSheet!!.data = postData
        bindingSheet!!.layoutPost.rvActivityPhoto.adapter =
            postData.mediaUrls?.let { PostMediaAdapter(it, this) }
        setData()
    }

    private fun setData() {
        bindingSheet!!.btnAccept.setOnClickListener {
            if (onDialogActionListener != null) {
                onDialogActionListener?.onAcceptPostPress(postData, relatedItemData)
                dismiss()
            }
        }
        bindingSheet!!.btnReject.setOnClickListener {
            if (onDialogActionListener != null) {
                onDialogActionListener?.onRejectPostPress(postData, relatedItemData)
                dismiss()
            }
        }
    }

    interface OnDialogActionListener {
        fun onAcceptPostPress(data: PostData, relatedItemData: String?)
        fun onRejectPostPress(data: PostData, relatedItemData: String?)
    }

    override fun onPhotoItemPress(data: MediaUrlsData) {
    }
}