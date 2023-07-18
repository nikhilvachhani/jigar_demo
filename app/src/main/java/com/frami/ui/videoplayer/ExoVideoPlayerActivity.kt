package com.frami.ui.videoplayer

import android.os.Bundle
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.navigation.NavGraph
import com.frami.BR
import com.frami.R
import com.frami.databinding.BaseActivityBinding
import com.frami.ui.base.BaseActivity

class ExoVideoPlayerActivity : BaseActivity<BaseActivityBinding, ExoVideoPlayerViewModel>(),
    ExoVideoPlayerNavigator {

    private var mViewBinding: BaseActivityBinding? = null

    override fun getBindingVariable(): Int {
        return BR.viewModel
    }

    override fun getLayoutId(): Int = R.layout.base_activity

    private val viewModelInstance: ExoVideoPlayerViewModel by viewModels {
        viewModeFactory
    }

    override fun getViewModel(): ExoVideoPlayerViewModel {
        return viewModelInstance
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mViewBinding = getViewDataBinding()

        setupNavController()
        viewModelInstance.setNavigator(this)

        window.statusBarColor = ContextCompat.getColor(this, R.color.lightBg)
        window.navigationBarColor = ContextCompat.getColor(this, R.color.lightBg)
    }

    private fun setupNavController() {
        val graph: NavGraph =
            mNavController!!.navInflater.inflate(R.navigation.nav_video)
//        graph.startDestination = R.id.htmlPagesFragment
        mNavController!!.setGraph(graph, intent.extras)
    }

    fun clickListener() {
    }

    override fun onBack() {
    }
}
