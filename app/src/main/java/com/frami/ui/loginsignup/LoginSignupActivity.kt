package com.frami.ui.loginsignup

import android.os.Bundle
import android.view.WindowManager
import androidx.activity.viewModels
import androidx.navigation.NavGraph
import com.frami.BR
import com.frami.R
import com.frami.databinding.BaseActivityBinding
import com.frami.ui.base.BaseActivity

class LoginSignupActivity : BaseActivity<BaseActivityBinding, LoginSignupViewModel>(),
    LoginSignupNavigator {

    private var mBinding: BaseActivityBinding? = null

    private val viewModelInstance: LoginSignupViewModel by viewModels {
        viewModeFactory
    }


    override fun getBindingVariable(): Int {
        return BR.viewModel
    }

    override fun getLayoutId(): Int = R.layout.base_activity

    override fun getViewModel(): LoginSignupViewModel {
        return viewModelInstance
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        mBinding = getViewDataBinding()

        setupNavController()
        viewModelInstance.setNavigator(this)
    }

    private fun setupNavController() {
        val graph: NavGraph =
            mNavController!!.navInflater.inflate(R.navigation.nav_auth_graph)
        mNavController!!.graph = graph
    }

    override fun onBack() {

    }
}
