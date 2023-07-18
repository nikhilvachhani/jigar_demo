package com.frami.ui.chat.list

import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.viewModels
import com.frami.BR
import com.frami.R
import com.frami.data.model.home.ChatListData
import com.frami.databinding.FragmentChatListBinding
import com.frami.ui.base.BaseFragment
import com.frami.ui.chat.list.adapter.ChatListAdapter
import com.frami.utils.extensions.hide
import com.frami.utils.extensions.visible

class ChatListFragment :
    BaseFragment<FragmentChatListBinding, ChatListFragmentViewModel>(),
    ChatListFragmentNavigator, ChatListAdapter.OnItemClickListener {

    private val viewModelInstanceCategory: ChatListFragmentViewModel by viewModels {
        viewModeFactory
    }
    private var mViewBinding: FragmentChatListBinding? = null
    override fun getBindingVariable(): Int = BR.viewModel

    override fun getLayoutId(): Int = R.layout.fragment_chat_list

    override fun getViewModel(): ChatListFragmentViewModel = viewModelInstanceCategory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
            ChatListAdapter(
                getViewModel().getChatList(requireActivity()), this
            )
    }

    private fun toolbar() {
        mViewBinding!!.toolBarLayout.tvTitle.hide()
        mViewBinding!!.toolBarLayout.toolBar.setNavigationOnClickListener { v -> onBack() }
        mViewBinding!!.toolBarLayout.cvBack.visible()
        mViewBinding!!.toolBarLayout.cvBack.setOnClickListener { onBack() }
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
    }

    override fun onChatPress(data: ChatListData) {
        navigateToChat(data)
    }
}