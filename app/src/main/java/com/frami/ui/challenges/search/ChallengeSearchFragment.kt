package com.frami.ui.challenges.search

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.viewModels
import com.frami.BR
import com.frami.R
import com.frami.data.model.explore.ChallengesData
import com.frami.data.model.explore.ViewTypes
import com.frami.databinding.FragmentChallengeSearchBinding
import com.frami.ui.base.BaseFragment
import com.frami.ui.challenges.details.ChallengeDetailsDialog
import com.frami.ui.dashboard.explore.adapter.ChallengesAdapter
import com.frami.utils.AppConstants
import com.frami.utils.extensions.visible


class ChallengeSearchFragment :
    BaseFragment<FragmentChallengeSearchBinding, ChallengeSearchFragmentViewModel>(),
    ChallengeSearchFragmentNavigator, ChallengesAdapter.OnItemClickListener,
    ChallengeDetailsDialog.OnDialogActionListener {

    private val viewModelInstanceCategory: ChallengeSearchFragmentViewModel by viewModels {
        viewModeFactory
    }
    private var mViewBinding: FragmentChallengeSearchBinding? = null
    override fun getBindingVariable(): Int = BR.viewModel

    override fun getLayoutId(): Int = R.layout.fragment_challenge_search

    override fun getViewModel(): ChallengeSearchFragmentViewModel = viewModelInstanceCategory

    private var listAdapter = ChallengesAdapter(
        ArrayList(),
        this,
        true
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            getViewModel().viewTypes.set(
                requireArguments().getSerializable(
                    AppConstants.EXPLORE_VIEW_TYPE.CHALLENGES.toString()
                ) as ViewTypes
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
        mViewBinding!!.recyclerView.adapter = listAdapter
        listAdapter.data =
            getViewModel().getExploreChallengesList(requireActivity()) as MutableList<ChallengesData>

        mViewBinding!!.searchLayout.etSearchView.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun afterTextChanged(editable: Editable) {
                filter(editable.toString())
            }
        })
    }

    private fun filter(searchString: String) {
        listAdapter.filter.filter(searchString)
    }


    private fun toolbar() {
        mViewBinding!!.toolBarLayout.tvTitle.text = requireActivity().getString(R.string.challenge)
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
        hideKeyboard()
        mNavController!!.navigateUp()
    }

    private fun clickListener() {
    }

    override fun showChallengePopup(data: ChallengesData) {
        val dialog = ChallengeDetailsDialog(requireActivity(), data)
        dialog.setListener(this)
        dialog.show()
    }

    override fun showChallengeDetails(data: ChallengesData) {
        navigateToChallengeDetails(data.challengeId)
    }

    override fun onViewAllPress(viewType: ViewTypes?) {

    }

    override fun onChallengeParticipantStatusChange(data: ChallengesData, participantStatus: String) {

    }

    override fun onChallengeDetailsPress(data: ChallengesData) {
        navigateToChallengeDetails(data.challengeId)
    }

}