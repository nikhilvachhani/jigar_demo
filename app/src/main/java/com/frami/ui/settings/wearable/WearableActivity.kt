package com.frami.ui.settings.wearable

import android.net.Uri
import android.os.Bundle
import android.view.WindowManager
import androidx.activity.viewModels
import androidx.navigation.NavGraph
import com.frami.BR
import com.frami.R
import com.frami.databinding.BaseActivityBinding
import com.frami.ui.base.BaseActivity
import com.frami.utils.AppConstants

class WearableActivity : BaseActivity<BaseActivityBinding, WearableViewModel>(),
    WearableNavigator {

    private var mBinding: BaseActivityBinding? = null

    private val viewModelInstance: WearableViewModel by viewModels {
        viewModeFactory
    }


    override fun getBindingVariable(): Int {
        return BR.viewModel
    }

    override fun getLayoutId(): Int = R.layout.base_activity

    override fun getViewModel(): WearableViewModel {
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

        val bundle = Bundle()
        if (intent.extras?.containsKey(AppConstants.EXTRAS.FROM) == true) {
            bundle.putString(
                AppConstants.EXTRAS.FROM,
                intent.extras?.getString(AppConstants.EXTRAS.FROM)
            )
        }

        val uri: Uri? = intent.data
        log("WearableActivity error intent>> $uri")
        if (uri != null) {
            val oAuthToken = uri.getQueryParameter(AppConstants.EXTRAS.O_AUTH_TOKEN)
            if (oAuthToken != null) {
                val oAuthVerifier = uri.getQueryParameter(AppConstants.EXTRAS.O_AUTH_TOKEN_VERIFIER)
                bundle.putString(AppConstants.EXTRAS.O_AUTH_TOKEN, oAuthToken)
                bundle.putString(AppConstants.EXTRAS.O_AUTH_TOKEN_VERIFIER, oAuthVerifier)
            }
            val authority = uri.authority
            val path = uri.path
            log(".authority>>> $authority")
            log(".uri.PATH>>> $path")
            if (authority != null) {
                if (authority.contains(getString(R.string.callback_domain)) && path.toString()
                        .contains("/callback/strava")
                ) {
                    val stravaCode = uri.getQueryParameter(AppConstants.EXTRAS.CODE)
                    log("stravaCode>>> $stravaCode")
                    if (stravaCode != null) {
                        bundle.putString(AppConstants.EXTRAS.CODE, stravaCode)
                        bundle.putString(AppConstants.EXTRAS.FROM, AppConstants.FROM.STRAVA)
                    }
                } else if (authority.contains(getString(R.string.callback_domain)) && path.toString()
                        .contains("/callback/polar")
                ) {
                    val polarCode = uri.getQueryParameter(AppConstants.EXTRAS.CODE)
                    log("polarCode>>> $polarCode")
                    if (polarCode != null) {
                        bundle.putString(AppConstants.EXTRAS.CODE, polarCode)
                        bundle.putString(AppConstants.EXTRAS.FROM, AppConstants.FROM.POLAR)
                    }
                } else if (authority.contains(getString(R.string.callback_domain)) && path.toString()
                        .contains("/callback/fitbit")
                ) {
                    val fitbitCode = uri.getQueryParameter(AppConstants.EXTRAS.CODE)
                    log("fitbitCode>>> $fitbitCode")
                    if (fitbitCode != null) {
                        bundle.putString(AppConstants.EXTRAS.CODE, fitbitCode)
                        bundle.putString(AppConstants.EXTRAS.FROM, AppConstants.FROM.FITBIT)
                    }
                }
            }
        }

        val graph: NavGraph =
            mNavController!!.navInflater.inflate(R.navigation.nav_wearable)
        mNavController!!.setGraph(graph, bundle)
    }

    override fun onBack() {

    }
}
