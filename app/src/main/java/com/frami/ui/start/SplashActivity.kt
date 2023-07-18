package com.frami.ui.start

import android.os.Bundle
import android.view.WindowManager
import androidx.activity.viewModels
import androidx.navigation.NavGraph
import com.frami.BR
import com.frami.R
import com.frami.databinding.BaseActivityBinding
import com.frami.ui.base.BaseActivity
import com.frami.utils.AppConstants
import com.google.gson.Gson


class SplashActivity : BaseActivity<BaseActivityBinding, SplashModel>(),
    SplashNavigator {

    private var mViewBinding: BaseActivityBinding? = null

    private val viewModelInstance: SplashModel by viewModels {
        viewModeFactory
    }

    override fun getBindingVariable(): Int {
        return BR.viewModel
    }

    override fun getLayoutId(): Int = R.layout.base_activity

    override fun getViewModel(): SplashModel {
        return viewModelInstance
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        mViewBinding = getViewDataBinding()

        viewModelInstance.setNavigator(this)

        setupNavController()
    }


    private fun setupNavController() {
        val graph: NavGraph = mNavController!!.navInflater.inflate(R.navigation.nav_splash_graph)
        if (intent.extras?.containsKey(AppConstants.EXTRAS.SCREEN_TYPE) == true) {
            getViewModel().saveIsOpenFromNotification(true)
        }
        log("Splash intent extras>>  ${Gson().toJson(intent.dataString)}")
        //com.framiactivity.frami://https://framiwebapptest.azurewebsites.net/activities/3af14ab9-0ae0-4527-972a-a23b1277c58f
        val uri = intent.data
        if (uri != null) {
            //Open from universal link
            getViewModel().saveIsOpenFromNotification(true)
            val bundle = Bundle()
            uri.pathSegments?.forEachIndexed { index, params ->
                if (uri.pathSegments.size == 3) {
                    if (index == 1) {
                        bundle.putString(AppConstants.EXTRAS.SCREEN_TYPE, params)
                    } else if (index == 2) {
                        bundle.putString(AppConstants.EXTRAS.RELATED_ITEM_ID, params)
                    }
                } else {
                    if (index == 0) {
                        bundle.putString(AppConstants.EXTRAS.SCREEN_TYPE, params)
                    } else {
                        bundle.putString(AppConstants.EXTRAS.RELATED_ITEM_ID, params)
                    }
                }
            }
            mNavController!!.setGraph(graph, bundle)
        } else {
            mNavController!!.setGraph(graph, intent.extras)
        }
    }


    override fun onBack() {

    }
}
