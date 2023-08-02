package com.frami.ui.base

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.telephony.TelephonyManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import androidx.activity.result.ActivityResultLauncher
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment.findNavController
import com.androidnetworking.common.ANConstants
import com.androidnetworking.error.ANError
import com.bumptech.glide.Glide
import com.frami.R
import com.frami.data.model.BaseResponse
import com.frami.data.model.activity.comment.CommentData
import com.frami.data.model.challenge.CompetitorData
import com.frami.data.model.community.subcommunity.SubCommunityData
import com.frami.data.model.explore.CommunityData
import com.frami.data.model.home.ActivityData
import com.frami.data.model.home.ActivityResponseData
import com.frami.data.model.home.ActivityTypes
import com.frami.data.model.home.ChatListData
import com.frami.data.model.invite.ParticipantData
import com.frami.data.model.lookup.ActivityOptionsData
import com.frami.data.model.lookup.application.ApplicationOptionsData
import com.frami.data.model.lookup.challenges.ChallengesOptionsData
import com.frami.data.model.lookup.community.CommunityOptionsData
import com.frami.data.model.lookup.events.EventsOptionsData
import com.frami.data.model.lookup.reward.RewardOptionsData
import com.frami.data.model.post.PostData
import com.frami.data.model.profile.UserProfileData
import com.frami.data.model.rewards.Organizer
import com.frami.data.model.rewards.RewardsDetails
import com.frami.data.model.rewards.UnlockReward
import com.frami.data.model.settings.help.CMSData
import com.frami.data.model.user.User
import com.frami.ui.dashboard.DashboardActivity
import com.frami.ui.settings.wearable.WearableActivity
import com.frami.ui.videoplayer.ExoVideoPlayerActivity
import com.frami.utils.AppConstants
import com.frami.utils.AppConstants.StatusCodes.Companion.AUTHORIZATION_FAILED
import com.frami.utils.AppConstants.StatusCodes.Companion.FORBIDDEN
import com.frami.utils.CommonUtils
import com.frami.utils.DateTimeUtils
import com.frami.utils.NetworkUtils
import com.frami.utils.widget.toolbar.MenuTintUtils
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.gson.Gson
import dagger.android.support.AndroidSupportInjection
import kotlinx.coroutines.launch
import okhttp3.Response
import javax.inject.Inject
import kotlin.math.floor


abstract class BaseFragment<T : ViewDataBinding, V : BaseViewModel<*>> :
        Fragment(), BaseNavigator {

    lateinit var mRootView: View

    //    private lateinit var mViewDataBinding: T
    private var mViewDataBinding: T? = null
    private lateinit var mViewModel: V
    var mNavController: NavController? = null
        private set

    @Inject
    lateinit var viewModeFactory: ViewModelProvider.Factory

    /**
     * Override for set binding variable
     *
     * @return variable id
     */
    abstract fun getBindingVariable(): Int

    /**
     * @return layout resource id
     */
    @LayoutRes
    abstract fun getLayoutId(): Int

    /**
     * Override for set view model
     *
     * @return view model instance
     */
    abstract fun getViewModel(): V

//    @Inject
//    protected var mAnalyticsLogger: AnalyticsLogger? = null

    fun getViewDataBinding(): T? {
        return mViewDataBinding
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        performDependencyInjection()
        super.onCreate(savedInstanceState)
        mViewModel = getViewModel()
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?,
    ): View? {
        mViewDataBinding = DataBindingUtil.inflate(inflater, getLayoutId(), container, false)

        mViewDataBinding!!.setVariable(getBindingVariable(), mViewModel)
        mViewDataBinding!!.executePendingBindings()

        mRootView = mViewDataBinding!!.root
        return mRootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mNavController = findNavController(this)

        loadDataFromDB()
    }

    private fun loadDataFromDB() {
//        getViewModel().getCountryListFromDbLiveData().observe(
//            viewLifecycleOwner,
//            androidx.lifecycle.Observer { countryList ->
//                log("countryList  ${Gson().toJson(countryList)}")
//                if (isAdded)
//                    if (countryList.isNotEmpty() && getViewModel().countryDataList.isEmpty()) {
//                        getViewModel().countryDataList = countryList
//                    }
//            })

        getViewModel().getUserLiveData().observe(
                viewLifecycleOwner,
                androidx.lifecycle.Observer { user ->
//                log("getUserLiveData>> " + Gson().toJson(user))
                    if (user != null) {
                        getViewModel().user.set(user)
                    }
                })
    }

    protected open fun getBaseActivity(): BaseActivity<*, *> =
            requireActivity() as BaseActivity<*, *>


    private fun performDependencyInjection() = AndroidSupportInjection.inject(this)

    protected open fun setStatusBarColor(
            statusBarColor: Int,
            isLight: Boolean = false,
    ) {
//        val window: android.view.Window = getBaseActivity().window
//        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
////        window.clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
////        window.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
//        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            var flags: Int = window.decorView.systemUiVisibility
//            flags = if (isLight) {
//                flags and View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR.inv()
//            } else {
//                flags or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
//            }
//            window.decorView.systemUiVisibility = flags
//        }
//        window.statusBarColor = statusBarColor

//        activity?.window?.statusBarColor = ContextCompat.getColor(requireActivity(), statusBarColor)
//        activity?.window?.navigationBarColor =
//            ContextCompat.getColor(requireContext(), statusBarColor)
        activity?.window?.statusBarColor = ContextCompat.getColor(requireContext(), statusBarColor)
//        activity?.window?.navigationBarColor = statusBarColor
    }

    protected open fun setStatusBarOverlay(isLight: Boolean) {
        val window: android.view.Window = getBaseActivity().window
        window.setFlags(
                WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
        )
//        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//            WindowManager.LayoutParams.FLAG_FULLSCREEN)
//        window.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
//            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)

//        var flags: Int = window.decorView.systemUiVisibility
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            flags = flags or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
//        }
//        window.decorView.systemUiVisibility = flags

    }

    protected open fun setStatusBarHeight(view: View) {

        val statusBarHeight = MenuTintUtils.getStatusBarHeight(requireContext())
        val lp = view.layoutParams
        lp.height = statusBarHeight
        view.layoutParams = lp

    }

    override fun showLoading() {
//        activity?.runOnUiThread {
//        }
        try {
            if (isAdded)
                if (progressDialog != null && !progressDialog!!.isShowing) {
                    progressDialog!!.show()
                } else {
                    initProgressDialog()
                    progressDialog!!.show()
                }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun hideLoading() {
//        activity?.runOnUiThread {
        if (progressDialog != null && progressDialog!!.isShowing) {
            progressDialog!!.dismiss()
        }
//        }
    }

    private var progressDialog: AlertDialog? = null

    open fun initProgressDialog() {
        val inflater = layoutInflater
        val alertLayout: View = inflater.inflate(R.layout.dialog_loading, null)
        val builder1 =
                AlertDialog.Builder(this.activity!!)
        builder1.setView(alertLayout)
        builder1.setCancelable(true)
        progressDialog = builder1.create()
        progressDialog!!.setCancelable(true)
        progressDialog!!.window!!.setBackgroundDrawableResource(R.color.transparent)
    }

    override fun showMessage(message: String?) {
        getBaseActivity().showMessage(message)
    }

    override fun showCenterMessage(message: String?) {
        getBaseActivity().showCenterMessage(message)
    }

    override fun showMessage(messageId: Int) {
        showMessage(getString(messageId))
    }

    override fun showAlert(message: String?) {
        getBaseActivity().showAlert(message)
    }

    override fun showComingSoon() {
        showMessage(getString(R.string.under_development))
    }

    override fun handleError(throwable: Throwable?) {
        throwable?.printStackTrace()
        log("handleError== " + throwable?.message)
        if (throwable is ANError) {
//            Log.e("handleError", throwable.localizedMessage)
            val networkResponse: Response? =
                    if (throwable.response != null) throwable.response.networkResponse() else null

            try {
                if (networkResponse == null || throwable.errorBody == null) {
                    requireActivity().runOnUiThread {
                        if (!isNetworkConnected()) {
                            showMessage(R.string.no_internet)
                        } else {
                            showMessage(R.string.some_thing_wrong)
                        }
                    }
                } else if (throwable.errorCode == 0 && throwable.errorDetail == ANConstants.CONNECTION_ERROR) { // No internet available
                    requireActivity().runOnUiThread {
                        showMessage(
                                R.string.connection_error
                        )
                    }
                } else if (throwable.errorCode == AUTHORIZATION_FAILED || throwable.errorCode == FORBIDDEN) {
                    requireActivity().runOnUiThread { showMessage(R.string.session_expired) }
                    logoutSuccess()
                } else {
                    requireActivity().runOnUiThread { showMessage(R.string.request_timeout) }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }

            // Display error message from error response
            try {
                val errorResponse: BaseResponse =
                        Gson().fromJson(throwable.errorBody, BaseResponse::class.java)
                if (errorResponse.getMessage() != "") {
                    requireActivity().runOnUiThread {
                        showMessage(
                                errorResponse.getMessage()
                        )
                    }
                    return
                }
            } catch (e: Exception) {
            }
        } else {
            requireActivity().runOnUiThread { showMessage(throwable!!.message) }
        }
    }

    override fun isNetworkConnected(): Boolean {
        if (!isAdded)
            return false
        val isNetwork = NetworkUtils.isNetworkConnected(requireContext())
        if (!isNetwork) {
            showMessage(R.string.no_internet)
        }
        return isNetwork
    }

    open fun isNetworkConnected_NoToast(activity: FragmentActivity): Boolean {
        return NetworkUtils.isNetworkConnected(activity)
    }

    open fun hideKeyboard() {
        val imm: InputMethodManager =
                requireActivity().getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        //Find the currently focused view, so we can grab the correct window token from it.
        var view = requireActivity().currentFocus
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = View(requireActivity())
        }
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    override fun log(message: String?) {
        if (isAdded)
            getBaseActivity().log(message)
    }

    protected open fun log(title: String, message: String) {
        getBaseActivity().log("$title : $message")
    }

    override fun onImageUploadSuccess(imageUrl: String?) {
        getBaseActivity().onImageUploadSuccess(imageUrl)
    }


    override fun languageUpdated(language: String) {
        getBaseActivity().languageUpdated(language)
    }

    fun getDeviceCountryCode(): String {
        val tm = context?.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        return tm.networkCountryIso.uppercase()
    }

    override fun countryFetchSuccess() {

    }

    open fun displayLogoutDialog() {
        val alertDialog = AlertDialog.Builder(requireContext(), R.style.AlertDialogTheme).create()
        alertDialog.setMessage(resources.getString(R.string.logout_message))
        alertDialog.setButton(
                AlertDialog.BUTTON_NEGATIVE, resources.getString(R.string.cancel)
        ) { dialog, which -> }
        alertDialog.setButton(
                AlertDialog.BUTTON_POSITIVE, resources.getString(R.string.logout)
        ) { dialog, which -> logout() }
        alertDialog.show()
    }

    override fun logout() {
        getViewModel().logoutAPI()
    }

    override fun logoutSuccess() {
        getBaseActivity().logoutSuccess()
    }

    fun authFlow(
            user: User,
            isEnableNavigateToContactInfo: Boolean,
            wearableDeviceActivityResultLauncher: ActivityResultLauncher<Intent>? = null,
            arguments: Bundle? = null
    ) {
        getViewModel().user.set(user)
        if (!user.isPersonalInfoCompleted) {
            val bundle = Bundle()
            bundle.putBoolean(AppConstants.EXTRAS.IS_PERSONAL_INFO_COMPLETED, false)
            mNavController?.navigate(R.id.toPersonalityInfoActivity, bundle)
            activity?.finish()
        }else if (!user.isPrivacySettingCompleted) {
            val bundle = Bundle()
            bundle.putBoolean(AppConstants.EXTRAS.IS_PERSONAL_INFO_COMPLETED, true)
            mNavController?.navigate(R.id.toPersonalityInfoActivity, bundle)
        }
//        else if (!user.isContactInfoCompleted) {
//            val bundle = Bundle()
//            bundle.putBoolean(AppConstants.EXTRAS.IS_PERSONAL_INFO_COMPLETED, true)
//            if (isEnableNavigateToContactInfo) {
//                mNavController?.navigate(R.id.contactInfoFragment, bundle)
//            } else {
//                mNavController?.navigate(R.id.toPersonalityInfoActivity, bundle)
//                activity?.finish()
//            }
//        }
        else if (wearableDeviceActivityResultLauncher != null && !getViewModel().getIsWearableDeviceSkip() && user.userDevices?.isEmpty() == true) {
            val bundle = Bundle()
            val intent = Intent(requireContext(), WearableActivity::class.java)
            bundle.putString(AppConstants.EXTRAS.FROM, AppConstants.FROM.LOGIN)
            intent.putExtras(bundle)
            wearableDeviceActivityResultLauncher?.launch(intent)
        } else {
            if (arguments != null) {
                val bundle = Bundle()
                bundle.putString(
                        AppConstants.EXTRAS.SCREEN_TYPE,
                        arguments.getString(AppConstants.EXTRAS.SCREEN_TYPE)
                )
                bundle.putString(
                        AppConstants.EXTRAS.RELATED_ITEM_ID,
                        arguments.getString(AppConstants.EXTRAS.RELATED_ITEM_ID)
                )
                bundle.putString(
                        AppConstants.EXTRAS.NOTIFICATION_ID,
                        arguments.getString(AppConstants.EXTRAS.NOTIFICATION_ID)
                )
                if (arguments.containsKey(AppConstants.EXTRAS.USER_NAME)) {
                    bundle.putString(
                            AppConstants.EXTRAS.USER_NAME,
                            arguments.getString(AppConstants.EXTRAS.USER_NAME)
                    )
                }
                if (arguments.containsKey(AppConstants.EXTRAS.PROFILE_PHOTO_URL)) {
                    bundle.putString(
                            AppConstants.EXTRAS.PROFILE_PHOTO_URL,
                            arguments.getString(AppConstants.EXTRAS.PROFILE_PHOTO_URL)
                    )
                }
                if (arguments.containsKey(AppConstants.EXTRAS.RELATED_ITEM_DATA)) {
                    bundle.putString(
                            AppConstants.EXTRAS.RELATED_ITEM_DATA,
                            arguments.getString(AppConstants.EXTRAS.RELATED_ITEM_DATA)
                    )
                }
                if (arguments.containsKey(AppConstants.EXTRAS.PARENT_ID)) {
                    bundle.putString(
                            AppConstants.EXTRAS.PARENT_ID,
                            arguments.getString(AppConstants.EXTRAS.PARENT_ID)
                    )
                }
                if (arguments.containsKey(AppConstants.EXTRAS.PARENT_TYPE)) {
                    bundle.putString(
                            AppConstants.EXTRAS.PARENT_TYPE,
                            arguments.getString(AppConstants.EXTRAS.PARENT_TYPE)
                    )
                }
                navigateToDashboard(bundle)
                activity?.finish()
            } else {
                navigateToDashboard(null)
                activity?.finish()
            }
        }
    }

    //Chart
    fun initChart(
            barChart: BarChart,
            data: ActivityResponseData? = null,
            duration: AppConstants.DURATION? = null,
            activityTypeFilters: List<ActivityTypes>? = null
    ) {
        setStackedBarData(barChart, data, duration, activityTypeFilters)
        barChart.description.isEnabled = false
        // if more than 60 entries are displayed in the getLineChart(), no values will be
        // drawn
        barChart.setMaxVisibleValueCount(50)
        // scaling can now only be done on x- and y-axis separately
        barChart.setPinchZoom(true)

        barChart.setDrawGridBackground(false)
        barChart.setDrawValueAboveBar(false)
        barChart.setDrawBarShadow(false)


        barChart.setDrawValueAboveBar(false)
        barChart.isHighlightFullBarEnabled = false

        // change the position of the y-labels
        val leftAxis: YAxis = barChart.axisLeft
//        leftAxis.valueFormatter = IndexAxisValueFormatter()
        leftAxis.valueFormatter = object : ValueFormatter() {
            override fun getFormattedValue(value: Float): String {
                return (floor(value.toDouble()).toInt()).toString() + " Min"
            }
        }
        leftAxis.axisMinimum = 0f // this replaces setStartAtZero(true)
        leftAxis.setDrawGridLines(true)
        leftAxis.setDrawAxisLine(false)

        barChart.axisRight.isEnabled = false

        val xLabels: XAxis = barChart.xAxis
        xLabels.position = XAxis.XAxisPosition.BOTTOM
        xLabels.setDrawGridLines(true)
        xLabels.granularity = 1.0f
        xLabels.isGranularityEnabled = true
        xLabels.gridColor = ContextCompat.getColor(requireContext(), R.color.inactiveColor)

        // change the position of the y-labels
        val rightAxis: YAxis = barChart.axisRight
        rightAxis.valueFormatter = IndexAxisValueFormatter()
        rightAxis.axisMinimum = 0f // this replaces setStartAtZero(true)
        rightAxis.setDrawGridLines(false)
        rightAxis.setDrawAxisLine(false)


        // setting data
        val l: Legend = barChart.legend
        l.verticalAlignment = Legend.LegendVerticalAlignment.BOTTOM
        l.horizontalAlignment = Legend.LegendHorizontalAlignment.RIGHT
        l.orientation = Legend.LegendOrientation.HORIZONTAL
        l.setDrawInside(false)
        l.formSize = 8f
        l.formToTextSpace = 4f
        l.xEntrySpace = 6f

    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun setStackedBarData(
            barChart: BarChart,
            data: ActivityResponseData? = null,
            duration: AppConstants.DURATION? = null,
            activityTypeFilters: List<ActivityTypes>? = null, // Added for challenge activity analisys specific
    ) {
        if (data == null) {
            setEmptyChartData(barChart, duration)
            return
        }
        var selectedDuration = AppConstants.DURATION.WEEKLY
        if (duration != null) {
            selectedDuration = duration
        }
        val values = ArrayList<BarEntry>()
        var max = 1
        val activities = data.activityAnalysisList
        val activityTypes = activityTypeFilters ?: data.activityTypeFilters
//        val yAxisValueEntries = ArrayList<String>()
        for (i in activities.indices) {
            val activity = activities[i]
            val arrList = ArrayList<Float>()
            var tMax = 1
            for (j in activityTypes) {
                val ind =
                        activity.activitiesData.find { it.activityType?.combinationNo == j.combinationNo }
                if (ind != null) {
//                    log("ind >>> ${ind.activityDuration}  ${Gson().toJson(ind)}")
//                    if (max < ind.activityDuration?.toInt() ?: 0) {
//                        max = ind.activityDuration?.toInt() ?: 0 + 10
//                    }
                    tMax += (ind.activityDuration ?: 0)
                    arrList.add(ind.activityDuration?.toFloat() ?: 0f)
                } else {
                    arrList.add(0f)
                }
            }
            if (max < tMax) {
                max = tMax + 100
            }
            values.add(
                    BarEntry(
                            i.toFloat(),
                            arrList.toFloatArray(),
                            requireContext().resources.getDrawable(
                                    R.drawable.ic_correct,
                                    requireContext().theme
                            )
                    )
            )
            if (max > 100) {
                barChart.axisLeft.axisMaximum = max.toFloat()
            } else {
                barChart.axisLeft.spaceTop = 20f
                barChart.axisLeft.axisMaximum = 100f
            }
//            barChart.axisLeft.valueFormatter = IndexAxisValueFormatter(yAxisValueEntries);
        }


//        for (i in 0 until 7) {
//            val mul: Float = (2 + 1).toFloat()
//            val val1 = (Math.random() * mul).toFloat() + mul / 3
//            val val2 = (Math.random() * mul).toFloat() + mul / 3
//            val val3 = (Math.random() * mul).toFloat() + mul / 3
//            values.add(
//                BarEntry(
//                    i.toFloat(),
//                    floatArrayOf(val1, val2, val3),
//                    requireContext().resources.getDrawable(
//                        R.drawable.ic_correct,
//                        requireContext().theme
//                    )
//                )
//            )
//        }

//        val listWeekly = arrayListOf(
//            "MO",
//            "TU",
//            "WE",
//            "TH",
//            "FI",
//            "SA",
//            "SU",
//        )
        val xAxisValueEntries = ArrayList<String>()
        for (element in activities) {
            when (selectedDuration) {
                AppConstants.DURATION.WEEKLY -> {
                    xAxisValueEntries.add(
                            DateTimeUtils.getDateFromServerDateFormatToTODateFormat(
                                    element.dateOfActivity,
                                    DateTimeUtils.dateFormatE
                            ).toString()
                    )
                }

                AppConstants.DURATION.MONTHLY -> {
                    xAxisValueEntries.add(
                            DateTimeUtils.getDateFromServerDateFormatToTODateFormat(
                                    element.dateOfActivity,
                                    DateTimeUtils.dateFormatDMMM
                            ).toString()
                    )
                }

                else -> {
                    xAxisValueEntries.add(
                            DateTimeUtils.getDateFromServerDateFormatToTODateFormat(
                                    element.dateOfActivity,
                                    DateTimeUtils.dateFormatMMMYY
                            ).toString()
                    )
                }
            }
        }
        barChart.xAxis.valueFormatter = IndexAxisValueFormatter(xAxisValueEntries)

//        log("barChart.xAxis " + barChart.axisLeft.longestLabel)

        val barDataSet: BarDataSet

        if (barChart.data != null && barChart.data.dataSetCount > 0) {
            barDataSet = barChart.data.getDataSetByIndex(0) as BarDataSet
            barDataSet.values = values
            barChart.data.notifyDataChanged()
            barChart.notifyDataSetChanged()
        } else {
            barDataSet = BarDataSet(values, "")
            barDataSet.setDrawIcons(false)
            barDataSet.setDrawValues(false)

            if (activityTypes.isNotEmpty()) {
                val colors: MutableList<Int> = ArrayList()
                for (t in activityTypes) {
                    colors.add(Color.parseColor(t.color))
                }
                barDataSet.colors = colors
            }
            barDataSet.barShadowColor =
                    ContextCompat.getColor(requireContext(), R.color.inactiveColor)

//            barDataSet.stackLabels = arrayOf("", "", "")
            val dataSets = ArrayList<IBarDataSet>()
            dataSets.add(barDataSet)
            barChart.legend.isEnabled = false
            val barData = BarData(dataSets)
            barData.setValueFormatter(IndexAxisValueFormatter())
            barData.setValueTextColor(Color.RED)
            barData.setDrawValues(true)

            barChart.data = barData
        }

        barChart.setFitBars(true)
        barChart.invalidate()
    }

    private fun setEmptyChartData(barChart: BarChart, duration: AppConstants.DURATION?) {
        val values = ArrayList<BarEntry>()
        barChart.axisLeft.spaceTop = 20f
        barChart.axisLeft.axisMaximum = 100f
        for (i in 0 until 7) {
            values.add(BarEntry(i.toFloat(), floatArrayOf(100f)))
        }

        val xAxisValueEntries = ArrayList<String>()
        var selectedDuration = AppConstants.DURATION.WEEKLY
        if (duration != null) {
            selectedDuration = duration
        }
        when (selectedDuration) {
            AppConstants.DURATION.WEEKLY -> {
                for (i in 0 until 7) {
                    xAxisValueEntries.add(
                            DateTimeUtils.getDateFromServerDateFormatToTODateFormat(
                                    DateTimeUtils.getCurrentTime(i, AppConstants.DURATION.WEEKLY),
                                    DateTimeUtils.dateFormatE
                            ).toString()
                    )
                }
            }

            AppConstants.DURATION.MONTHLY -> {
                for (i in 0 until 7) {
                    xAxisValueEntries.add(
                            DateTimeUtils.getDateFromServerDateFormatToTODateFormat(
                                    DateTimeUtils.getCurrentTime(i, AppConstants.DURATION.MONTHLY),
                                    DateTimeUtils.dateFormatDMMM
                            ).toString()
                    )
                }
            }

            else -> {
                for (i in 0 until 7) {
                    xAxisValueEntries.add(
                            DateTimeUtils.getDateFromServerDateFormatToTODateFormat(
                                    DateTimeUtils.getCurrentTime(i, AppConstants.DURATION.YEARLY),
                                    DateTimeUtils.dateFormatMMMYY
                            ).toString()
                    )
                }
            }
        }
        barChart.xAxis.valueFormatter = IndexAxisValueFormatter(xAxisValueEntries)
        val barDataSet: BarDataSet

        if (barChart.data != null && barChart.data.dataSetCount > 0) {
            barDataSet = barChart.data.getDataSetByIndex(0) as BarDataSet
            barDataSet.values = values
            barChart.data.notifyDataChanged()
            barChart.notifyDataSetChanged()
        } else {
            barDataSet = BarDataSet(values, "")
            barDataSet.setDrawIcons(false)
            barDataSet.setDrawValues(false)

            val colors: MutableList<Int> = ArrayList()
            colors.add(Color.parseColor("#E9E9E9"))
            barDataSet.colors = colors
            barDataSet.barShadowColor =
                    ContextCompat.getColor(requireContext(), R.color.inactiveColor)

//            barDataSet.stackLabels = arrayOf("", "", "")
            val dataSets = ArrayList<IBarDataSet>()
            dataSets.add(barDataSet)
            barChart.legend.isEnabled = false
            val barData = BarData(dataSets)
            barData.setValueFormatter(IndexAxisValueFormatter())
            barData.setValueTextColor(Color.RED)
            barData.setDrawValues(true)

            barChart.data = barData
        }

        barChart.setFitBars(true)
        barChart.invalidate()
    }

//    fun initLineChart(lineChart: LineChart, data: HomeData? = null) {
//        lineChart.setViewPortOffsets(0f, 0f, 0f, 0f)
//        lineChart.setBackgroundColor(Color.rgb(104, 241, 175))
//
//        // no description text
//        lineChart.description.isEnabled = false
//
//        // enable touch gestures
//
//        // enable touch gestures
//        lineChart.setTouchEnabled(true)
//
//        // enable scaling and dragging
//
//        // enable scaling and dragging
//        lineChart.isDragEnabled = true
//        lineChart.setScaleEnabled(true)
//
//        // if disabled, scaling can be done on x- and y-axis separately
//
//        // if disabled, scaling can be done on x- and y-axis separately
//        lineChart.setPinchZoom(false)
//
//        lineChart.setDrawGridBackground(false)
//        lineChart.maxHighlightDistance = 300f
//
//        val x: XAxis = lineChart.xAxis
//        x.isEnabled = false
//
//        val y: YAxis = lineChart.axisLeft
////        y.typeface = tfLight
//        y.setLabelCount(6, false)
//        y.textColor = Color.WHITE
//        y.setPosition(YAxis.YAxisLabelPosition.INSIDE_CHART)
//        y.setDrawGridLines(false)
//        y.axisLineColor = Color.WHITE
//
//        lineChart.axisRight.isEnabled = false
//
//
//        lineChart.legend.isEnabled = false
//
//        lineChart.animateXY(2000, 2000)
//
//        // don't forget to refresh the drawing
//
//        // don't forget to refresh the drawing
//        lineChart.invalidate()
//
//        setData(4, 100f, lineChart)
//    }

//    private fun setData(count: Int, range: Float, lineChart: LineChart) {
//        val values = ArrayList<Entry>()
//        for (i in 0 until count) {
//            val value = (Math.random() * (range + 1)).toFloat() + 20
//            values.add(Entry(i.toFloat(), value))
//        }
//        val lineDataSet: LineDataSet
//        if (lineChart.data != null &&
//            lineChart.data.dataSetCount > 0
//        ) {
//            lineDataSet = lineChart.data.getDataSetByIndex(0) as LineDataSet
//            lineDataSet.values = values
//            lineChart.data.notifyDataChanged()
//            lineChart.notifyDataSetChanged()
//        } else {
//            // create a dataset and give it a type
//            lineDataSet = LineDataSet(values, "")
//            lineDataSet.mode = LineDataSet.Mode.HORIZONTAL_BEZIER
//            lineDataSet.cubicIntensity = 0.2f
//            lineDataSet.setDrawFilled(true)
//            lineDataSet.setDrawCircles(false)
//            lineDataSet.lineWidth = 1.8f
//            lineDataSet.circleRadius = 4f
//            lineDataSet.setCircleColor(Color.WHITE)
//            lineDataSet.highLightColor = Color.parseColor("#19AE93")
//            lineDataSet.color = Color.WHITE
//            lineDataSet.fillColor = Color.WHITE
//            lineDataSet.fillAlpha = 100
//            lineDataSet.setDrawHorizontalHighlightIndicator(false)
//            lineDataSet.fillFormatter =
//                IFillFormatter { dataSet, dataProvider ->
//                    lineChart.axisLeft.axisMinimum
//                }
//
//            // create a data object with the data sets
//            val data = LineData(lineDataSet)
////            data.setValueTypeface(tfLight)
//            data.setValueTextSize(9f)
//            data.setDrawValues(false)
//
//            // set data
//            lineChart.data = data
//        }
//    }

    fun getSelectedCompetitorCommunityName(data: List<Organizer>): String {
        if (data.isEmpty()) {
            return ""
        }
        val selectedList: List<Organizer> = data.filter { s -> s.isSelected }
        val stringList: MutableList<String> = ArrayList()
        selectedList.forEach { stringList.add(it.name ?: "") }
        return when {
            selectedList.size == 1 -> selectedList[0].name ?: ""
            selectedList.size > 1 -> selectedList[0].name + " AND +" + (selectedList.size - 1)
            else -> ""
        }
    }

    fun getSelectedActivityTypeName(data: List<ActivityTypes>): String {
//        if (data.isEmpty()) {
//            return ""
//        }
//        if (data[0].name == AppConstants.KEYS.All && data[0].isSelected) {
//            return data[0].name
//        }
//        val selectedList: List<ActivityTypes> = data.filter { s -> s.isSelected }
//        val stringList: MutableList<String> = ArrayList()
//        selectedList.forEach { stringList.add(it.name) }
//        return when {
//            selectedList.size == 1 -> selectedList[0].name
//            selectedList.size > 1 -> selectedList[0].name + " AND +" + (selectedList.size - 1)
//            else -> ""
//        }
        return CommonUtils.getSelectedActivityTypeName(data)
    }

    fun getSelectedActivityTypeIcon(
            data: List<ActivityTypes>
    ): ActivityTypes? {
        if (data.isEmpty()) {
            return null
        }
        if (data[0].name == AppConstants.KEYS.All && data[0].isSelected) {
            return null
        }
        val selectedList: List<ActivityTypes> = data.filter { s -> s.isSelected }
        val stringList: MutableList<String> = ArrayList()
        selectedList.forEach { stringList.add(it.name) }
        return when {
            selectedList.isNotEmpty() -> selectedList[0]
            else -> null
        }
    }

    fun getSelectedParticipantsName(data: List<ParticipantData>): String {
        if (data.isEmpty()) {
            return ""
        }
        val selectedList: List<ParticipantData> = data.filter { s -> s.isSelected }
        val stringList: MutableList<String> = ArrayList()
        selectedList.forEach { stringList.add(it.userName) }
        return when {
            selectedList.size == 1 -> selectedList[0].userName
            selectedList.size > 1 -> selectedList[0].userName + " AND +" + (selectedList.size - 1)
            else -> ""
        }
    }

    fun getSelectedCommunityName(data: List<CompetitorData>): String {
        if (data.isEmpty()) {
            return ""
        }
        val selectedList: List<CompetitorData> = data.filter { s -> s.isSelected }
        val stringList: MutableList<String> = ArrayList()
        selectedList.forEach { it.communityName?.let { it1 -> stringList.add(it1) } }
        return when {
            selectedList.size == 1 -> selectedList[0].communityName ?: ""
            selectedList.size > 1 -> selectedList[0].communityName + " AND +" + (selectedList.size - 1)
            else -> ""
        }
    }

    //Navigation
    fun navigateToIntro() {
        mNavController?.navigate(R.id.toIntroActivity)
    }

    fun navigateToDashboard(arguments: Bundle?) {
        if (arguments != null) {
            mNavController!!.navigate(R.id.toDashboardActivity, arguments)
        } else {
            mNavController!!.navigate(R.id.toDashboardActivity)
        }
    }

    fun navigateToLogin() {
        lifecycleScope.launch {
            mNavController?.navigate(R.id.toLoginSignupActivity)
            activity?.finish()
        }
    }

    fun navigateToUserActivityScreen(
            userId: String? = null,
            bundle: Bundle = Bundle()
    ) {
        userId?.let { bundle.putString(AppConstants.EXTRAS.USER_ID, it) }
        mNavController!!.navigate(R.id.toUserActivityFragment, bundle)
    }

    fun navigateToActivityDetails(
            activityId: String?, isAbleToGoBack: Boolean = true,
            bundle: Bundle = Bundle()
    ) {
        bundle.putString(AppConstants.EXTRAS.ACTIVITY_ID, activityId)
        bundle.putBoolean(AppConstants.EXTRAS.IS_ABLE_TO_GO_BACK, isAbleToGoBack)
        mNavController!!.navigate(R.id.toActivityDetailsFragment, bundle)
    }

    fun navigateToEditManualActivity(activityId: String?, isAbleToGoBack: Boolean = true) {
        val bundle = Bundle()
        bundle.putString(AppConstants.EXTRAS.ACTIVITY_ID, activityId)
        bundle.putBoolean(AppConstants.EXTRAS.IS_ABLE_TO_GO_BACK, isAbleToGoBack)
        mNavController!!.navigate(R.id.toCreateActivityFragment, bundle)
    }

    fun navigateToChallengeDetails(
            challengeId: String?,
            isAbleToGoBack: Boolean = true,
            bundle: Bundle = Bundle()
    ) {
        bundle.putSerializable(AppConstants.EXTRAS.CHALLENGE_ID, challengeId)
        bundle.putBoolean(AppConstants.EXTRAS.IS_ABLE_TO_GO_BACK, isAbleToGoBack)
        mNavController!!.navigate(R.id.toChallengeDetailsFragment, bundle)
    }

    fun navigateToCommunityParticipant(communityData: CommunityData?) {
        val bundle = Bundle()
        bundle.putInt(AppConstants.EXTRAS.TYPE, AppConstants.IS_FROM.COMMUNITY)
        bundle.putBoolean(
                AppConstants.EXTRAS.HIDE_MORE_PARTICIPANT_ADD,
                (communityData?.communityPrivacy == AppConstants.KEYS.Private || communityData?.communityPrivacy == AppConstants.KEYS.Global)
        )
        bundle.putString(AppConstants.EXTRAS.COMMUNITY_ID, communityData?.communityId)
        bundle.putString(AppConstants.EXTRAS.COMMUNITY_PRIVACY, communityData?.communityPrivacy)

        val communityAdminsId: MutableList<String> = ArrayList<String>()
        communityData?.communityAdmins?.forEach { communityAdminsId.add(it.userId) }.apply {
            bundle.putString(AppConstants.EXTRAS.ADMIN_USER_ID, communityAdminsId.joinToString())
        }

        bundle.putBoolean(
                AppConstants.EXTRAS.IS_LOGGED_IN_USER,
                communityData?.isLoggedInUser() == true
        )
        mNavController!!.navigate(R.id.toParticipantFragment, bundle)
    }

    fun navigateToSubCommunityParticipant(subCommunityData: SubCommunityData?) {
        val bundle = Bundle()
        bundle.putInt(AppConstants.EXTRAS.TYPE, AppConstants.IS_FROM.SUB_COMMUNITY)
        bundle.putString(
                AppConstants.EXTRAS.SUB_COMMUNITY_ID,
                subCommunityData?.subCommunityId ?: ""
        )
        subCommunityData?.let {
            bundle.putBoolean(
                    AppConstants.EXTRAS.IS_CHILD_SUB_COMMUNITY,
                    !it.parentSubCommunityId.isNullOrEmpty()
            )
        }
        bundle.putString(
                AppConstants.EXTRAS.COMMUNITY_ID,
                subCommunityData?.communityId ?: ""
        )
        val communityAdminsId: MutableList<String> = ArrayList<String>()
        subCommunityData?.communityAdmins?.forEach { communityAdminsId.add(it.userId) }.apply {
            bundle.putString(AppConstants.EXTRAS.ADMIN_USER_ID, communityAdminsId.joinToString())
        }
        bundle.putBoolean(
                AppConstants.EXTRAS.IS_LOGGED_IN_USER,
                subCommunityData?.isLoggedInUser() == true
        )
        bundle.putBoolean(
                AppConstants.EXTRAS.HIDE_MORE_PARTICIPANT_ADD,
                subCommunityData?.isLoggedInUser() == false
        )
        mNavController!!.navigate(R.id.toParticipantFragment, bundle)
    }

    fun navigateToCommunityDetails(
            communityId: String?, isAbleToGoBack: Boolean = true,
            bundle: Bundle = Bundle()
    ) {
        communityId?.let { bundle.putString(AppConstants.EXTRAS.COMMUNITY_ID, communityId) }
        bundle.putBoolean(AppConstants.EXTRAS.IS_ABLE_TO_GO_BACK, isAbleToGoBack)
        mNavController!!.navigate(R.id.toCommunityDetailsFragment, bundle)
    }

    fun navigateToSubCommunityDetails(
            subCommunityId: String?,
            isAbleToGoBack: Boolean = true,
            bundle: Bundle = Bundle()
    ) {
        bundle.putString(AppConstants.EXTRAS.SUB_COMMUNITY_ID, subCommunityId)
        bundle.putBoolean(AppConstants.EXTRAS.IS_ABLE_TO_GO_BACK, isAbleToGoBack)
        mNavController!!.navigate(R.id.toSubCommunityDetailsFragment, bundle)
    }

    fun navigateToEventDetails(
            eventId: String?,
            isAbleToGoBack: Boolean = true,
            bundle: Bundle = Bundle()
    ) {
        bundle.putSerializable(AppConstants.EXTRAS.EVENT_ID, eventId)
        bundle.putBoolean(AppConstants.EXTRAS.IS_ABLE_TO_GO_BACK, isAbleToGoBack)
        mNavController!!.navigate(R.id.toEventDetailsFragment, bundle)
    }

    fun navigateToChatList() {
        mNavController!!.navigate(R.id.toChatListFragment)
    }

    fun navigateToInviteFriend() {
        mNavController!!.navigate(R.id.toInviteFriendFragment)
    }

    fun navigateToChat(data: ChatListData) {
        val bundle = Bundle()
        bundle.putSerializable(AppConstants.EXTRAS.CHAT, data)
        mNavController!!.navigate(R.id.toMessageFragment, bundle)
    }

    fun navigateToNotification() {
        mNavController!!.navigate(R.id.toNotificationFragment)
    }

    fun navigateToPrivacyPolicy() {
        navigateToCMS(
                activity?.getString(R.string.privacy_policy).toString(),
                getViewModel().getPrivacyPolicyURL()
        )
    }

    fun navigateToTNC() {
        navigateToCMS(
                activity?.getString(R.string.terms_condition).toString(),
                getViewModel().getTermsOfServicesURL()
        )
    }

    fun navigateToFAQ() {
        navigateToCMS(
                activity?.getString(R.string.faq).toString(),
                getViewModel().getFAQURL()
        )
    }

    fun navigateToAbout() {
        navigateToCMS(
                activity?.getString(R.string.about_us).toString(),
                getViewModel().getAboutURL()
        )
    }

    private fun navigateToCMS(title: String, url: String) {
        val bundle = Bundle()
        bundle.putSerializable(AppConstants.EXTRAS.CMS, CMSData(title, url))
        mNavController!!.navigate(R.id.toCMSFragment, bundle)
    }

    fun navigateToUserProfile(userId: String? = null) {
        if (userId.isNullOrEmpty()) return
        val bundle = Bundle()
        bundle.putString(AppConstants.EXTRAS.USER_ID, userId)
        mNavController!!.navigate(R.id.toUserProfileFragment, bundle)
    }

    fun navigateToEditReward(data: RewardsDetails) {
        val bundle = Bundle()
        bundle.putSerializable(AppConstants.EXTRAS.REWARD_DETAILS, data)
        mNavController!!.navigate(R.id.toEditRewardsFragment, bundle)
    }

    fun navigateToApplauseScreen(relatedId: String, postType: String? = null) {
        val bundle = Bundle()
        if (!postType.isNullOrEmpty()) {
            bundle.putString(AppConstants.EXTRAS.RELATED_ID, relatedId)
        } else {
            bundle.putString(AppConstants.EXTRAS.ACTIVITY_ID, relatedId)
        }
        mNavController!!.navigate(R.id.toApplauseFragment, bundle)
    }

    fun navigateToCommentScreen(relatedId: String, postType: String?) {
        val bundle = Bundle()
        bundle.putString(AppConstants.EXTRAS.RELATED_ID, relatedId)
        bundle.putString(AppConstants.EXTRAS.POST_TYPE, postType)
        mNavController!!.navigate(R.id.toCommentsFragment, bundle)
    }

    fun navigateToPostScreen(relatedId: String, postType: String, bundle: Bundle = Bundle()) {
        bundle.putString(AppConstants.EXTRAS.RELATED_ID, relatedId)
        bundle.putString(AppConstants.EXTRAS.POST_TYPE, postType)
        mNavController!!.navigate(R.id.toPostFragment, bundle)
    }

    fun navigateToPostCommentReplayScreen(
            postId: String,
            postType: String?,
            commentData: CommentData? = null
    ) {
        val bundle = Bundle()
        bundle.putString(AppConstants.EXTRAS.POST_ID, postId)
        bundle.putString(AppConstants.EXTRAS.POST_TYPE, postType)
        commentData?.let {
            bundle.putSerializable(AppConstants.EXTRAS.COMMENT, it)
        }
        mNavController!!.navigate(R.id.toPostRepliesFragment, bundle)
    }

    fun navigateToComingSoon(title: String) {
        val bundle = Bundle()
        bundle.putString(AppConstants.EXTRAS.TITLE, title)
        mNavController!!.navigate(R.id.toComingSoonFragment, bundle)
    }

    fun navigateToCreateActivity() {
//        val intent = Intent(requireActivity(), ActivityActivity::class.java).apply {
////                    putExtra(EXTRA_MESSAGE, message)
//        }
//        startActivity(intent)
        mNavController!!.navigate(R.id.createActivityFragment)
    }

    fun switchTabToTab(action: Int) {
        val bottomNavigationView: BottomNavigationView =
                (requireActivity() as DashboardActivity).findViewById(R.id.bottomNavView)
        val view: View = bottomNavigationView.findViewById(action)
        view.performClick()
    }

    override fun userInfoFetchSuccess(user: User?) {
        getBaseActivity().userInfoFetchSuccess(user)
    }

    override fun activityTypesFetchSuccessfully(list: List<ActivityTypes>) {
        getBaseActivity().activityTypesFetchSuccessfully(list)
    }

    override fun activityOptionsFetchSuccessfully(activityOptionsData: ActivityOptionsData?) {
        getBaseActivity().activityOptionsFetchSuccessfully(activityOptionsData)
    }

    override fun challengeOptionsFetchSuccessfully(challengesOptionsData: ChallengesOptionsData?) {
        getBaseActivity().challengeOptionsFetchSuccessfully(challengesOptionsData)
    }

    override fun eventOptionsFetchSuccessfully(eventOptionData: EventsOptionsData?) {
        getBaseActivity().eventOptionsFetchSuccessfully(eventOptionData)
    }

    override fun communityOptionsFetchSuccessfully(communityOptionsData: CommunityOptionsData?) {
        getBaseActivity().communityOptionsFetchSuccessfully(communityOptionsData)
    }

    override fun rewardOptionsFetchSuccessfully(rewardOptionsData: RewardOptionsData) {
        getBaseActivity().rewardOptionsFetchSuccessfully(rewardOptionsData)
    }

    override fun onJoinCommunitySuccess(communityOrSubCommunityId: String) {
        getBaseActivity().onJoinCommunitySuccess(communityOrSubCommunityId)
    }

    override fun onJoinSubCommunitySuccess(communityOrSubCommunityId: String) {
        getBaseActivity().onJoinSubCommunitySuccess(communityOrSubCommunityId)
    }

    override fun onJoinChildSubCommunitySuccess(communityOrSubCommunityId: String) {
        getBaseActivity().onJoinChildSubCommunitySuccess(communityOrSubCommunityId)
    }

    override fun communityMemberFetchSuccessfully(
            list: List<ParticipantData>?,
            data: CommunityData
    ) {
        getBaseActivity().communityMemberFetchSuccessfully(list, data)
    }

    override fun subCommunityMemberFetchSuccessfully(
            list: List<ParticipantData>?,
            data: SubCommunityData
    ) {
        getBaseActivity().subCommunityMemberFetchSuccessfully(list, data)
    }

    override fun changeChallengeParticipantStatusSuccess(
            challengeId: String,
            participantStatus: String
    ) {
        getBaseActivity().changeChallengeParticipantStatusSuccess(challengeId, participantStatus)
    }

    override fun changeEventParticipantStatusSuccess(eventId: String, participantStatus: String) {
        getBaseActivity().changeEventParticipantStatusSuccess(eventId, participantStatus)
    }

    override fun userProfileFetchSuccess(data: UserProfileData?) {
        getBaseActivity().userProfileFetchSuccess(data)
    }

    override fun rewardAddToFavouriteSuccess(rewardId: String, favorite: Boolean) {
        getBaseActivity().rewardAddToFavouriteSuccess(rewardId, favorite)
    }

    override fun applicationOptionsFetchSuccessfully(applicationOptionsData: ApplicationOptionsData) {
        getBaseActivity().applicationOptionsFetchSuccessfully(applicationOptionsData)
    }

    override fun rewardDetailsFetchSuccess(
            rewardsDetails: RewardsDetails?,
            isFromActivate: Boolean
    ) {
        getBaseActivity().rewardDetailsFetchSuccess(rewardsDetails, isFromActivate)
    }

    override fun unlockRewardSuccess(unlockReward: UnlockReward?) {
        getBaseActivity().unlockRewardSuccess(unlockReward)
    }

    override fun activateRewardSuccess(rewardDetails: RewardsDetails, unlockReward: UnlockReward?) {
        getBaseActivity().activateRewardSuccess(rewardDetails, unlockReward)
    }

    override fun postFetchSuccess(list: List<PostData>?, isFromDetails: Boolean?, relatedItemData: String?) {
        getBaseActivity().postFetchSuccess(list, isFromDetails, )
    }

    override fun joinPostInviteSuccess(postData: PostData?, relatedItemData: String?) {
        getBaseActivity().joinPostInviteSuccess(postData)
    }

    fun showZoomImage(url: String?) {
        CommonUtils.showZoomImage(requireActivity(), url)
    }

    fun loadClappingHands(ivClap: AppCompatImageView) {
        if (isAdded)
            Glide.with(requireActivity()).asGif().load(R.drawable.clapping_hands)
                    .into(ivClap)
    }

    override fun applauseStatusUpdateSuccessfully(
            activityData: ActivityData,
            adapterPosition: Int
    ) {
        getBaseActivity().applauseStatusUpdateSuccessfully(activityData, adapterPosition)
    }

    override fun commentAddedSuccessfully() {
        getBaseActivity().commentAddedSuccessfully()
    }

    override fun reportPostSuccessfully() {
        getBaseActivity().reportPostSuccessfully()
    }

    fun logFirebaseEvent(event: String, message: String) {
        getBaseActivity().logFirebaseEvent(event, message)
    }

    override fun applauseStatusUpdateSuccessfullyOnPost(postData: PostData, adapterPosition: Int) {
        getBaseActivity().applauseStatusUpdateSuccessfullyOnPost(postData, adapterPosition)
    }

    override fun setUnreadBadgeCount(count: Int) {
        getBaseActivity().setUnreadBadgeCount(count)
    }

    override fun postDeleteSuccess(message: String) {
        getBaseActivity().postDeleteSuccess(message)
    }

    override fun communityDetailsFetchSuccess(data: CommunityData?, relatedItemData: String?) {
        getBaseActivity().communityDetailsFetchSuccess(data)
    }

    override fun subCommunityDetailsFetchSuccess(data: SubCommunityData?) {
        getBaseActivity().subCommunityDetailsFetchSuccess(data)
    }

    override fun specificPushNotificationUpdatePreferenceSuccess() {
        getBaseActivity().specificPushNotificationUpdatePreferenceSuccess()
    }

    fun clearAllNotification() {
        getBaseActivity().clearAllNotification()
    }

    fun openVideo(url: String?) {
//        requireActivity().startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
        val intent = Intent(requireContext(), ExoVideoPlayerActivity::class.java)
        val bundle = Bundle()
        bundle.putString(AppConstants.EXTRAS.VIDEO_URL, url)
        intent.putExtras(bundle)
        startActivity(intent)
    }
}