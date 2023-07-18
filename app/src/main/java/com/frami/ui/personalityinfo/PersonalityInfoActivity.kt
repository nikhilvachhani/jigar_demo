package com.frami.ui.personalityinfo

import android.os.Bundle
import androidx.activity.viewModels
import androidx.navigation.NavGraph
import com.frami.BR
import com.frami.R
import com.frami.databinding.BaseActivityBinding
import com.frami.ui.base.BaseActivity
import com.frami.utils.AppConstants

class PersonalityInfoActivity : BaseActivity<BaseActivityBinding, PersonalityInfoViewModel>(),
    PersonalityInfoNavigator {

    private var mViewBinding: BaseActivityBinding? = null

    override fun getBindingVariable(): Int {
        return BR.viewModel
    }

    override fun getLayoutId(): Int = R.layout.base_activity

    private val viewModelInstance: PersonalityInfoViewModel by viewModels {
        viewModeFactory
    }

    override fun getViewModel(): PersonalityInfoViewModel {
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
            mNavController!!.navInflater.inflate(R.navigation.nav_personality_info)
        if (intent.extras != null && intent.extras!!.containsKey(AppConstants.EXTRAS.IS_PERSONAL_INFO_COMPLETED)) {
            if (intent.extras!!.getBoolean(AppConstants.EXTRAS.IS_PERSONAL_INFO_COMPLETED, false)) {
                graph.startDestination = R.id.contactInfoFragment
            }
        }
        mNavController!!.setGraph(graph, intent.extras)
    }

    override fun onBack() {
    }
}
