package com.frami.ui.challenges.fragment

import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.viewModels
import com.frami.BR
import com.frami.R
import com.frami.data.model.explore.ChallengesData
import com.frami.data.model.explore.ViewTypes
import com.frami.databinding.FragmentChallengesBinding
import com.frami.ui.base.BaseFragment
import com.frami.ui.challenges.details.ChallengeDetailsDialog
import com.frami.ui.dashboard.explore.adapter.ChallengesAdapter
import com.frami.utils.extensions.hide
import com.frami.utils.extensions.visible


class ChallengesFragment :
    BaseFragment<FragmentChallengesBinding, ChallengesFragmentViewModel>(),
    ChallengesFragmentNavigator, ChallengesAdapter.OnItemClickListener,
    ChallengeDetailsDialog.OnDialogActionListener {

    private val viewModelInstance: ChallengesFragmentViewModel by viewModels {
        viewModeFactory
    }
    private var mViewBinding: FragmentChallengesBinding? = null
    override fun getBindingVariable(): Int = BR.challengeFragmentViewModel

    override fun getLayoutId(): Int = R.layout.fragment_challenges

    override fun getViewModel(): ChallengesFragmentViewModel = viewModelInstance

    private lateinit var challengesAdapter: ChallengesAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setStatusBarColor(R.color.lightBg)
        mViewBinding = getViewDataBinding()
        viewModelInstance.setNavigator(this)

        toolbar()
        handleBackPress()
        clickListener()
        init()
    }

    private fun init() {
        challengesAdapter =
            ChallengesAdapter(
                ArrayList<ChallengesData>(),
                this,
                true
            )
        mViewBinding!!.run {
            recyclerView.adapter = challengesAdapter

            swipeRefreshLayout.setOnRefreshListener {
                setRefreshEnableDisable(true)
                callAPI()
            }
        }
        setRefreshEnableDisable(true)
        callAPI()

//        mViewBinding!!.etSearchView.addTextChangedListener(object : TextWatcher {
//            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
//            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
//            override fun afterTextChanged(editable: Editable) {
//                filter(editable.toString())
//            }
//        })
    }

    private fun setRefreshEnableDisable(isRefreshing: Boolean) {
        mViewBinding!!.swipeRefreshLayout.isRefreshing = isRefreshing
        getViewModel().isRefreshing.set(isRefreshing)
    }

    private fun callAPI() {
        getViewModel().getRecommendedActiveChallengesAPI()
    }

    private fun filter(searchString: String) {
        challengesAdapter.filter.filter(searchString)
    }

    private fun toolbar() {
        mViewBinding!!.toolBarLayout.tvTitle.hide()
        mViewBinding!!.toolBarLayout.toolBar.setNavigationOnClickListener { v -> onBack() }
        mViewBinding!!.toolBarLayout.cvSearch.hide()//TODO
        mViewBinding!!.toolBarLayout.cvSearch.setOnClickListener {
            mNavController!!.navigate(R.id.toChallengeSearchFragment)
        }
        mViewBinding!!.toolBarLayout.cvNotification.hide()
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
//        requireActivity().finish()
        mNavController!!.popBackStack()
    }

    private fun clickListener() {
        mViewBinding!!.btnCreateNewChallenges.setOnClickListener {
            mNavController!!.navigate(R.id.toCreateChallengeStep1Fragment)
        }
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
        getViewModel().changeParticipantStatusChallenge(data.challengeId, participantStatus)
    }

    override fun changeChallengeParticipantStatusSuccess(challengeId: String, participantStatus: String) {
        setRefreshEnableDisable(true)
        callAPI()
    }

    override fun onChallengeDetailsPress(data: ChallengesData) {
        navigateToChallengeDetails(data.challengeId)
    }

    override fun challengesDataFetchSuccess(list: List<ChallengesData>?) {
        setRefreshEnableDisable(false)
        getViewModel().isDataEmpty.set(list?.isEmpty() == true)
        if (list == null) return
        challengesAdapter.data = list
    }
}