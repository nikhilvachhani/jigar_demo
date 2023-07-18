package com.frami.ui.challenges.participants

import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.viewModels
import com.frami.BR
import com.frami.R
import com.frami.data.model.explore.ChallengesData
import com.frami.data.model.invite.ParticipantData
import com.frami.databinding.FragmentParticipantBinding
import com.frami.ui.base.BaseFragment
import com.frami.ui.challenges.details.adapter.ParticipantAdapter
import com.frami.utils.AppConstants
import com.frami.utils.extensions.hide
import com.frami.utils.extensions.visible


class ParticipantFragment :
    BaseFragment<FragmentParticipantBinding, ParticipantFragmentViewModel>(),
    ParticipantFragmentNavigator, ParticipantAdapter.OnItemClickListener {

    private val viewModelInstanceCategory: ParticipantFragmentViewModel by viewModels {
        viewModeFactory
    }
    private var mViewBinding: FragmentParticipantBinding? = null
    override fun getBindingVariable(): Int = BR.viewModel

    override fun getLayoutId(): Int = R.layout.fragment_participant

    override fun getViewModel(): ParticipantFragmentViewModel = viewModelInstanceCategory

    private lateinit var participantAdapter: ParticipantAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            val isFrom = arguments?.getInt(
                AppConstants.EXTRAS.TYPE,
                AppConstants.IS_FROM.CHALLENGE
            )!!
            getViewModel().type.set(isFrom)
            if (arguments?.containsKey(AppConstants.EXTRAS.IS_LOGGED_IN_USER) == true) {
                getViewModel().isLoggedInUser.set(
                    arguments?.getBoolean(AppConstants.EXTRAS.IS_LOGGED_IN_USER, false) ?: false
                )
            }

            if (arguments?.containsKey(AppConstants.EXTRAS.CHALLENGE_ID) == true) {
                getViewModel().challengesId.set(arguments?.getString(AppConstants.EXTRAS.CHALLENGE_ID))
            }
            if (arguments?.containsKey(AppConstants.EXTRAS.CHALLENGES_DETAILS) == true) {
                getViewModel().challengeDetails.set(arguments?.getSerializable(AppConstants.EXTRAS.CHALLENGES_DETAILS) as ChallengesData)
            }
            if (arguments?.containsKey(AppConstants.EXTRAS.EVENT_ID) == true) {
                getViewModel().eventId.set(arguments?.getString(AppConstants.EXTRAS.EVENT_ID))
            }
            if (isFrom == AppConstants.IS_FROM.COMMUNITY) {
                if (arguments?.containsKey(AppConstants.EXTRAS.COMMUNITY_ID) == true) {
                    getViewModel().communityId.set(arguments?.getString(AppConstants.EXTRAS.COMMUNITY_ID))
                }
                if (arguments?.containsKey(AppConstants.EXTRAS.COMMUNITY_PRIVACY) == true) {
                    getViewModel().communityPrivacy.set(arguments?.getString(AppConstants.EXTRAS.COMMUNITY_PRIVACY))
                }
            } else if (isFrom == AppConstants.IS_FROM.SUB_COMMUNITY) {
                if (arguments?.containsKey(AppConstants.EXTRAS.SUB_COMMUNITY_ID) == true) {
                    getViewModel().subCommunityId.set(arguments?.getString(AppConstants.EXTRAS.SUB_COMMUNITY_ID))
                }
                if (arguments?.containsKey(AppConstants.EXTRAS.COMMUNITY_ID) == true) {
                    getViewModel().communityId.set(arguments?.getString(AppConstants.EXTRAS.COMMUNITY_ID))
                }
                if (arguments?.containsKey(AppConstants.EXTRAS.IS_CHILD_SUB_COMMUNITY) == true) {
                    getViewModel().isChildSubCommunity.set(arguments?.getBoolean(AppConstants.EXTRAS.IS_CHILD_SUB_COMMUNITY)!!)
                }
            }
            if (arguments?.containsKey(AppConstants.EXTRAS.HIDE_MORE_PARTICIPANT_ADD) == true) {
                getViewModel().hideMoreParticipantInvite.set(arguments?.getBoolean(AppConstants.EXTRAS.HIDE_MORE_PARTICIPANT_ADD) == true)
            }
            if (arguments?.containsKey(AppConstants.EXTRAS.ADMIN_USER_ID) == true) {
                getViewModel().adminUserIds.set(arguments?.getString(AppConstants.EXTRAS.ADMIN_USER_ID))
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setStatusBarColor(R.color.lightBg)
        mViewBinding = getViewDataBinding()
        viewModelInstanceCategory.setNavigator(this)

        toolbar()
        handleBackPress()
        clickListener()
        init()
    }

    private fun init() {
        val adminUserIdList: List<String>? = getViewModel().adminUserIds.get()?.split(",")?.toList()
        log("adminUserIdList>>>> ${adminUserIdList}")
        participantAdapter =
            ParticipantAdapter(
                ArrayList(),
                this,
                AppConstants.PARTICIPANT_ITEM_VIEW_TYPE.NAME,
                getViewModel().type.get(),
                adminUserIdList
            )
        mViewBinding!!.recyclerView.adapter = participantAdapter
        mViewBinding!!.run {
            swipeRefreshLayout.setOnRefreshListener {
                callAPI()
            }
        }
        callAPI()
    }

    private fun callAPI() {
        setRefreshEnableDisable(true)
        if (getViewModel().type.get() == AppConstants.IS_FROM.COMMUNITY) {
            getViewModel().getCommunityMemberAPI()
        } else if (getViewModel().type.get() == AppConstants.IS_FROM.SUB_COMMUNITY) {
            if (getViewModel().isChildSubCommunity.get()) {
                getViewModel().getChildSubCommunityMemberAPI()
            } else {
                getViewModel().getSubCommunityMemberAPI()
            }
        } else {
            if (getViewModel().participantHeader.get() == AppConstants.PARTICIPANT_HEADER.ACCEPTED) {
                if (getViewModel().type.get() == AppConstants.IS_FROM.CHALLENGE) {
                    getViewModel().getChallengeAcceptedParticipantAPI()
                } else if (getViewModel().type.get() == AppConstants.IS_FROM.EVENT) {
                    getViewModel().getEventsAcceptedParticipantAPI()
                }
            } else {
                if (getViewModel().type.get() == AppConstants.IS_FROM.CHALLENGE) {
                    getViewModel().getNoResponseParticipantAPI()
                } else if (getViewModel().type.get() == AppConstants.IS_FROM.EVENT) {
                    getViewModel().getEventsMaybeParticipantAPI()
                }
            }
        }
    }

    private fun setRefreshEnableDisable(isRefreshing: Boolean) {
        mViewBinding!!.swipeRefreshLayout.isRefreshing = isRefreshing
        getViewModel().isRefreshing.set(isRefreshing)
    }

    private fun toolbar() {
        mViewBinding!!.toolBarLayout.tvTitle.hide()
        mViewBinding!!.toolBarLayout.toolBar.setNavigationOnClickListener { v -> onBack() }
        mViewBinding!!.toolBarLayout.cvSearch.hide()
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
        mNavController!!.navigateUp()
    }

    private fun clickListener() {
        mViewBinding!!.tvAccepted.setOnClickListener {
            getViewModel().participantHeader.set(AppConstants.PARTICIPANT_HEADER.ACCEPTED)
                .also { callAPI() }
        }
        mViewBinding!!.tvMaybe.setOnClickListener {
            getViewModel().participantHeader.set(AppConstants.PARTICIPANT_HEADER.NO_RESPONSE_OR_MAYBE)
                .also { callAPI() }
        }
        mViewBinding!!.cvAddMoreParticipant.setOnClickListener {
            val bundle = Bundle()
            bundle.putInt(
                AppConstants.EXTRAS.FROM,
                getViewModel().type.get() ?: AppConstants.IS_FROM.CHALLENGE
            )
            if (getViewModel().type.get() == AppConstants.IS_FROM.CHALLENGE) {
                bundle.putString(
                    AppConstants.EXTRAS.CHALLENGE_ID,
                    getViewModel().challengesId.get()
                )
                bundle.putSerializable(
                    AppConstants.EXTRAS.CHALLENGES_DETAILS,
                    getViewModel().challengeDetails.get()
                )
            } else if (getViewModel().type.get() == AppConstants.IS_FROM.EVENT) {
                bundle.putString(AppConstants.EXTRAS.EVENT_ID, getViewModel().eventId.get())
            } else if (getViewModel().type.get() == AppConstants.IS_FROM.COMMUNITY) {
                bundle.putString(AppConstants.EXTRAS.COMMUNITY_ID, getViewModel().communityId.get())
                bundle.putString(
                    AppConstants.EXTRAS.COMMUNITY_PRIVACY,
                    getViewModel().communityPrivacy.get()
                )
            } else if (getViewModel().type.get() == AppConstants.IS_FROM.SUB_COMMUNITY) {
                bundle.putString(
                    AppConstants.EXTRAS.SUB_COMMUNITY_ID,
                    getViewModel().subCommunityId.get()
                )
                bundle.putString(
                    AppConstants.EXTRAS.COMMUNITY_ID,
                    getViewModel().communityId.get()
                )
                bundle.putBoolean(
                    AppConstants.EXTRAS.IS_CHILD_SUB_COMMUNITY,
                    getViewModel().isChildSubCommunity.get()
                )
            }
            bundle.putString(AppConstants.EXTRAS.ADMIN_USER_ID, getViewModel().adminUserIds.get())
            mNavController!!.navigate(R.id.toUpdateParticipantFragment, bundle)
        }
    }

    override fun onParticipantPress(data: ParticipantData) {
        navigateToUserProfile(data.userId)
    }

    override fun participantFetchSuccessfully(list: List<ParticipantData>?) {
        setRefreshEnableDisable(false)
        getViewModel().isDataEmpty.set(list?.isEmpty() == true)
        list?.let {
            participantAdapter.data = list
        }
    }
}