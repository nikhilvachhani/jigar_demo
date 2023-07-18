package com.frami.ui.chat.message

import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.viewModels
import com.frami.BR
import com.frami.R
import com.frami.data.model.home.ChatListData
import com.frami.databinding.FragmentMessageBinding
import com.frami.ui.base.BaseFragment
import com.frami.ui.chat.message.adapter.MessageAdapter
import com.frami.ui.common.ImagePickerActivity
import com.frami.utils.AppConstants
import com.frami.utils.extensions.visible

class MessageFragment :
    BaseFragment<FragmentMessageBinding, MessageFragmentViewModel>(),
    MessageFragmentNavigator, ImagePickerActivity.PickerResultListener {

    private val viewModelInstanceCategory: MessageFragmentViewModel by viewModels {
        viewModeFactory
    }
    private var mViewBinding: FragmentMessageBinding? = null
    override fun getBindingVariable(): Int = BR.viewModel

    override fun getLayoutId(): Int = R.layout.fragment_message

    override fun getViewModel(): MessageFragmentViewModel = viewModelInstanceCategory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            getViewModel().data.set(
                requireArguments().getSerializable(
                    AppConstants.EXTRAS.CHAT
                ) as ChatListData
            )
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mViewBinding = getViewDataBinding()
        viewModelInstanceCategory.setNavigator(this)

        toolbar()
        handleBackPress()
        clickListener()
        init()
    }

    private fun init() {
        mViewBinding!!.recyclerView.adapter =
            getViewModel().data.get()?.messageList?.let {
                MessageAdapter(
                    it
                )
            }
    }

    private fun toolbar() {
        mViewBinding!!.toolBarChatLayout.toolBar.setNavigationOnClickListener { v -> onBack() }
        mViewBinding!!.toolBarChatLayout.cvBack.visible()
        mViewBinding!!.toolBarChatLayout.cvBack.setOnClickListener { onBack() }
        getViewModel().data.get()?.let {
//            mViewBinding!!.toolBarChatLayout.toolBar.tvTitle.text = it.senderName
//            mViewBinding!!.toolBarChatLayout.toolBar.tvLastSeen.text = it.messageTime
        }
    }

    private fun handleBackPress() {
        val callback: OnBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                onBack()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)
    }

    override fun onBack() {
        mNavController!!.navigateUp()
    }

    private fun clickListener() {
        mViewBinding!!.ivCamera.setOnClickListener {
            ImagePickerActivity.showImageChooser(requireActivity(), this)
        }
    }

    override fun onImageAvailable(imagePath: Uri?) {

    }

    override fun onError() {
    }
}