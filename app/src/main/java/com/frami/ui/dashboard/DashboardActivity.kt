package com.frami.ui.dashboard

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import androidx.activity.viewModels
import androidx.annotation.Nullable
import androidx.core.content.ContextCompat
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.navigation.NavGraph
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.frami.BR
import com.frami.R
import com.frami.data.model.challenge.competitor.RelatedItemData
import com.frami.data.model.common.IdNameData
import com.frami.data.model.user.User
import com.frami.databinding.ActivityDashboardBinding
import com.frami.ui.base.BaseActivity
import com.frami.ui.common.CreateDialog
import com.frami.ui.common.LevelUpSuccessDialog
import com.frami.utils.AppConstants
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.gson.Gson


class DashboardActivity : BaseActivity<ActivityDashboardBinding, DashboardViewModel>(),
    DashboardNavigator, BottomNavigationView.OnNavigationItemSelectedListener,
    CreateDialog.OnDialogActionListener {

    private var mBinding: ActivityDashboardBinding? = null

    private val viewModelInstance: DashboardViewModel by viewModels {
        viewModeFactory
    }

    override fun getBindingVariable(): Int {
        return BR.viewModel
    }

    override fun getLayoutId(): Int = R.layout.activity_dashboard

    override fun getViewModel(): DashboardViewModel {
        return viewModelInstance
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
//        window.statusBarColor = ContextCompat.getColor(this, R.color.lightBg)
        setStatusBarColor(R.color.lightBg)
        window.navigationBarColor = ContextCompat.getColor(this, R.color.lightBg)

        mBinding = getViewDataBinding()

        viewModelInstance.setNavigator(this)
        toolbar()
        setupNavController()
        IntentFilter().let {
            it.addAction(AppConstants.Action.LEVEL_UP)
            LocalBroadcastManager.getInstance(this).registerReceiver(receiver, it)
        }
    }

    override fun onDestroy() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(receiver)
        super.onDestroy()
    }

    private fun toolbar() {
        setSupportActionBar(mBinding!!.toolBar)
    }

    private fun setupNavController() {
        log("Dashboard intent>> ${Gson().toJson(intent.extras?.getString(AppConstants.EXTRAS.SCREEN_TYPE))}")
        log("Dashboard intent>> PARENT_TYPE ${Gson().toJson(intent.extras?.getString(AppConstants.EXTRAS.PARENT_TYPE))}")
        val graph: NavGraph =
            mNavController!!.navInflater.inflate(R.navigation.nav_dashboard_graph)
        mNavController!!.setGraph(graph, intent.extras)

        mNavController!!.addOnDestinationChangedListener { controller, destination, arguments ->
            run {
                when (destination.id) {
//                    R.id.chatListFragment,
                    R.id.messageFragment,
//                    R.id.notificationFragment,
//                    R.id.activityDetailsFragment,
                    R.id.commentsFragment,
                    R.id.applauseFragment,
                    R.id.editActivityFragment,
                    R.id.personalInfoFragment,
                    R.id.contactInfoFragment,
                    R.id.wearableFragment,
                    R.id.helpFragment,
                    R.id.aboutUsFragment,
                    R.id.contactUsFragment,
                    R.id.myPreferenceFragment,
                    R.id.contentPreferenceFragment,
                    R.id.notificationPreferenceFragment,
                    R.id.pushNotificationPreferenceMenuFragment,
                    R.id.specificNotificationPreferenceFragment,
                    R.id.privacyControlFragment,
                    R.id.cMSFragment,
                    R.id.successFragment,
                    R.id.createActivityFragment,
                    R.id.recordSessionFragment,
                    R.id.trackLocationFragment,
                    R.id.challengeFragment,
                    R.id.createChallengeStep1Fragment,
                    R.id.createChallengeStep2Fragment,
                    R.id.createChallengeStep3Fragment,
                    R.id.inviteParticipantFragment,
                    R.id.addRewardsFragment,
                    R.id.eventFragment,
                    R.id.createEventStep1Fragment,
                    R.id.createEventStep2Fragment,
                    R.id.createEventStep3Fragment,
                    R.id.communityFragment,
                    R.id.createCommunityStep1Fragment,
                    R.id.createCommunityStep2Fragment,
                    R.id.locationFragment,
                    R.id.createSubCommunityFragment,
                    R.id.createPostFragment,
                    R.id.postRepliesFragment,
                    R.id.exploreSearchFragment,
                    -> {
                        navigationBottomSheetVisibility(false)
                    }
                    else -> {
                        navigationBottomSheetVisibility(true)
                    }
                }

            }
        }


        val bottomNavView: BottomNavigationView = mBinding!!.bottomNavView

        val navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.homeFragment,
                R.id.exploreFragment
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        bottomNavView.setupWithNavController(navController)
        bottomNavView.setOnItemSelectedListener(this)

        getViewModel().getUserLiveData().observe(
            this,
            androidx.lifecycle.Observer { user ->
//                log("getUserLiveData>> " + Gson().toJson(user))
                if (user != null) {
                    setBottomMenuProfileIcon(user)
                }
            })
    }

    private fun navigationBottomSheetVisibility(isVisible: Boolean) {
        getViewModel().isBottomMenuVisible.set(isVisible)
    }

    private fun setBottomMenuProfileIcon(user: User) {
//        mBinding!!.bottomNavView.itemIconTintList = null // this is important
        Glide.with(this)
            .asBitmap()
            .load(user.profilePhotoUrl)
            .error(R.drawable.ic_user_placeholder)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .apply(RequestOptions.circleCropTransform())
            .apply(RequestOptions().override(60, 60))
            .into(object : CustomTarget<Bitmap?>() {
                override fun onLoadCleared(placeholder: Drawable?) {}
                override fun onLoadFailed(errorDrawable: Drawable?) {
//                    log("errorDrawable>> ${errorDrawable.toString()}")
                    mBinding!!.tvYou.setCompoundDrawablesWithIntrinsicBounds(
                        null,
                        errorDrawable,
                        null,
                        null
                    )
                }

                override fun onResourceReady(
                    resource: Bitmap,
                    transition: Transition<in Bitmap?>?
                ) {
                    val drawable: Drawable = BitmapDrawable(resources, resource)
//                    log("drawable>> $drawable")
                    mBinding!!.tvYou.setCompoundDrawablesWithIntrinsicBounds(
                        null,
                        drawable,
                        null,
                        null
                    )
                }
            })
    }

    override fun onBack() {
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        getViewModel().isProfileSelected.set(false)
        return when (item.itemId) {
            R.id.homeFragment -> {
                mNavController!!.navigate(
                    R.id.homeFragment,
                    null,
//                    NavOptions.Builder()
//                        .setLaunchSingleTop(true)
//                        .setEnterAnim(R.anim.b_nav_default_enter_anim)
//                        .setExitAnim(R.anim.b_nav_default_exit_anim)
//                        .setPopEnterAnim(R.anim.b_nav_default_pop_enter_anim)
//                        .setPopExitAnim(R.anim.b_nav_default_pop_exit_anim)
//                        .setPopUpTo(R.id.homeFragment, true)
//                        .build()
                )
                true
            }
            R.id.exploreFragment -> {
                mNavController!!.navigate(
                    R.id.exploreFragment,
                    null
//                    ,
//                    NavOptions.Builder()
////                        .setLaunchSingleTop(true)
//                        .setEnterAnim(R.anim.b_nav_default_enter_anim)
//                        .setExitAnim(R.anim.b_nav_default_exit_anim)
//                        .setPopEnterAnim(R.anim.b_nav_default_pop_enter_anim)
//                        .setPopExitAnim(R.anim.b_nav_default_pop_exit_anim)
////                        .setPopUpTo(R.id.exploreFragment, true)
//                        .build()
                )
                true
            }
            R.id.actionAdd -> {
                showCreateMenuDialog()
                false
            }
            R.id.rewardsFragment -> {
                mNavController!!.navigate(
                    R.id.rewardsFragment, null
//                    ,
//                    NavOptions.Builder()
////                        .setLaunchSingleTop(true)
//                        .setEnterAnim(R.anim.b_nav_default_enter_anim)
//                        .setExitAnim(R.anim.b_nav_default_exit_anim)
//                        .setPopEnterAnim(R.anim.b_nav_default_pop_enter_anim)
//                        .setPopExitAnim(R.anim.b_nav_default_pop_exit_anim)
////                        .setPopUpTo(R.id.rewardsFragment, true)
//                        .build()
                )
                true
            }
            R.id.myProfileFragment -> {
                getViewModel().isProfileSelected.set(true)
                mNavController!!.navigate(
                    R.id.myProfileFragment,
                    null
//                    ,
//                    NavOptions.Builder()
////                        .setLaunchSingleTop(true)
//                        .setEnterAnim(R.anim.b_nav_default_enter_anim)
//                        .setExitAnim(R.anim.b_nav_default_exit_anim)
//                        .setPopEnterAnim(R.anim.b_nav_default_pop_enter_anim)
//                        .setPopExitAnim(R.anim.b_nav_default_pop_exit_anim)
////                        .setPopUpTo(R.id.myProfileFragment, true)
//                        .build()
                )
                true
            }
            else -> {
                true
            }
        }
    }

    private fun showCreateMenuDialog() {
        val createDialog =
            CreateDialog(
                this,
                getViewModel().getCreateDialogList(this) as MutableList<IdNameData>,
                from = AppConstants.FROM.CREATE
            )
        createDialog.setListener(this)
        createDialog.show()
    }

    private fun showCreateActivityMenuDialog() {
        val createDialog =
            CreateDialog(
                this,
                getViewModel().getCreateActivityList(this) as MutableList<IdNameData>,
                from = AppConstants.FROM.CREATE_ACTIVITY
            )
        createDialog.setListener(this)
        createDialog.show()
    }

    override fun onPeriodSelect(from: String, data: IdNameData) {
        when (from) {
            AppConstants.FROM.CREATE -> when (data.value) {
                AppConstants.CREATE.ACTIVITY -> {
                    showCreateActivityMenuDialog()
                }
                AppConstants.CREATE.CHALLENGE -> {
                    mNavController!!.navigate(R.id.challengeFragment)
                }
                AppConstants.CREATE.COMMUNITY -> {
                    mNavController!!.navigate(R.id.communityFragment)
                }
                AppConstants.CREATE.EVENTS -> {
                    mNavController!!.navigate(R.id.eventFragment)
                }
                AppConstants.CREATE.REWARD -> {
                    val bundle = Bundle()
                    bundle.putInt(AppConstants.EXTRAS.TYPE, AppConstants.IS_FROM.CHALLENGE)
                    mNavController!!.navigate(R.id.addRewardsFragment, bundle)
                }
            }
            AppConstants.FROM.CREATE_ACTIVITY -> when (data.value) {
                AppConstants.CREATE.MANUAL_INPUT -> {
                    mNavController!!.navigate(R.id.createActivityFragment)
                }
                AppConstants.CREATE.RECORD_SESSION -> {
                    mNavController!!.navigate(R.id.recordSessionFragment)
                }
            }
        }
    }

    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            when (intent.action ?: return) {
                AppConstants.Action.LEVEL_UP -> {
                    getViewModel().saveIsLevelUp(false).apply {
                        val levelUpData = Gson().fromJson(
                            getViewModel().getLevelUpData(),
                            RelatedItemData::class.java
                        )
                        val levelUpSuccessDialog =
                            LevelUpSuccessDialog(this@DashboardActivity, levelUpData)
                        levelUpSuccessDialog.show()
                    }
                }
            }
        }
    }
}
