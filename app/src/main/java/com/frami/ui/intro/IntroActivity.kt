package com.frami.ui.intro

import android.os.Bundle
import androidx.activity.viewModels
import androidx.navigation.NavGraph
import com.frami.BR
import com.frami.R
import com.frami.databinding.BaseActivityBinding
import com.frami.ui.base.BaseActivity

class IntroActivity : BaseActivity<BaseActivityBinding, IntroViewModel>(),
    IntroNavigator {

    private var mViewBinding: BaseActivityBinding? = null

    override fun getBindingVariable(): Int {
        return BR.viewModel
    }

    override fun getLayoutId(): Int = R.layout.base_activity

    private val viewModelInstance: IntroViewModel by viewModels {
        viewModeFactory
    }

    override fun getViewModel(): IntroViewModel {
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
            mNavController!!.navInflater.inflate(R.navigation.nav_intro_graph)
//        graph.startDestination = R.id.htmlPagesFragment
        mNavController!!.setGraph(graph, intent.extras)
    }

    fun clickListener() {
    }

    override fun onBack() {
    }
}
