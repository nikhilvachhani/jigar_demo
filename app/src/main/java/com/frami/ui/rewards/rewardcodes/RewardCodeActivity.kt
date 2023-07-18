package com.frami.ui.rewards.rewardcodes

import android.os.Bundle
import androidx.activity.viewModels
import androidx.navigation.NavGraph
import com.frami.BR
import com.frami.R
import com.frami.databinding.BaseActivityBinding
import com.frami.ui.base.BaseActivity

class RewardCodeActivity : BaseActivity<BaseActivityBinding, RewardCodeViewModel>(),
    RewardCodeNavigator {

    private var mViewBinding: BaseActivityBinding? = null

    override fun getBindingVariable(): Int {
        return BR.viewModel
    }

    override fun getLayoutId(): Int = R.layout.base_activity

    private val viewModelInstance: RewardCodeViewModel by viewModels {
        viewModeFactory
    }

    override fun getViewModel(): RewardCodeViewModel {
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
            mNavController!!.navInflater.inflate(R.navigation.nav_add_reward_code)
        mNavController!!.setGraph(graph, intent.extras)
    }

    override fun onBack() {
    }
}
