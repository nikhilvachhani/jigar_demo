package com.frami.ui.invite

import android.os.Bundle
import androidx.activity.viewModels
import androidx.navigation.NavGraph
import com.frami.BR
import com.frami.R
import com.frami.databinding.BaseActivityBinding
import com.frami.ui.base.BaseActivity
import com.frami.utils.AppConstants

class InviteParticipantActivity : BaseActivity<BaseActivityBinding, InviteParticipantViewModel>(),
    InviteParticipantNavigator {

    private var mViewBinding: BaseActivityBinding? = null

    override fun getBindingVariable(): Int {
        return BR.viewModel
    }

    override fun getLayoutId(): Int = R.layout.base_activity

    private val viewModelInstance: InviteParticipantViewModel by viewModels {
        viewModeFactory
    }

    override fun getViewModel(): InviteParticipantViewModel {
        return viewModelInstance
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mViewBinding = getViewDataBinding()

        setupNavController()
        viewModelInstance.setNavigator(this)

    }

    private fun setupNavController() {
        val graph: NavGraph =
            mNavController!!.navInflater.inflate(R.navigation.nav_invite_participant)
        val activityIdContains = intent.extras?.containsKey(AppConstants.EXTRAS.ACTIVITY_ID)
        if (activityIdContains == true) {
            if (intent.extras?.getString(AppConstants.EXTRAS.ACTIVITY_ID)
                    ?.isNotEmpty() == true
            ) {
                graph.startDestination = R.id.updateParticipantFragment
            }
        }
        mNavController!!.setGraph(graph, intent.extras)
    }

    override fun onBack() {
    }
}
