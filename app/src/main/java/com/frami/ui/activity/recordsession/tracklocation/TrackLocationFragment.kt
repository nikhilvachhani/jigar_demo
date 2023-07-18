package com.frami.ui.activity.recordsession.tracklocation

import android.Manifest
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import android.location.Location
import android.net.Uri
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.text.TextUtils
import android.util.Log
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import com.frami.BR
import com.frami.R
import com.frami.data.model.home.ActivityData
import com.frami.data.model.home.ActivityTypes
import com.frami.data.model.lookup.ActivityOptionsData
import com.frami.databinding.FragmentTrackLocationBinding
import com.frami.ui.base.BaseFragment
import com.frami.utils.AppConstants
import com.frami.utils.DateTimeUtils
import com.frami.utils.extensions.hide
import com.frami.utils.extensions.visible
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Polyline
import com.google.android.gms.maps.model.PolylineOptions
import com.google.gson.Gson
import com.google.maps.android.PolyUtil
import com.karumi.dexter.BuildConfig
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import java.text.DecimalFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.*


class TrackLocationFragment :
    BaseFragment<FragmentTrackLocationBinding, TrackLocationFragmentViewModel>(),
    TrackLocationFragmentNavigator, OnMapReadyCallback {

    private val viewModelInstance: TrackLocationFragmentViewModel by viewModels {
        viewModeFactory
    }
    private var mViewBinding: FragmentTrackLocationBinding? = null
    override fun getBindingVariable(): Int = BR.viewModel

    override fun getLayoutId(): Int = R.layout.fragment_track_location

    override fun getViewModel(): TrackLocationFragmentViewModel = viewModelInstance

    private var mMap: GoogleMap? = null

    //Location
    // bunch of location related apis
    private var mFusedLocationClient: FusedLocationProviderClient? = null
    private var mSettingsClient: SettingsClient? = null
    private var mLocationRequest: LocationRequest? = null
    private var mLocationSettingsRequest: LocationSettingsRequest? = null
    private var mLocationCallback: LocationCallback? = null
    private var mCurrentLocation: Location? = null

    // boolean flag to toggle the ui
    private var mRequestingLocationUpdates = false

    val REQUEST_CHECK_SETTINGS = 100
    val UPDATE_INTERVAL_IN_MILLISECONDS: Long = 100000000
    val FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS: Long = 2000


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            if (arguments?.containsKey(AppConstants.FROM.ACTIVITY_TYPE) == true) {
                val activityType =
                    arguments?.getSerializable(AppConstants.FROM.ACTIVITY_TYPE) as ActivityTypes
                getViewModel().selectedActivityTypes.set(activityType)
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setStatusBarColor(R.color.lightBg)
        mViewBinding = getViewDataBinding()
        viewModelInstance.setNavigator(this)

        toolbar()
        handleBackPress()
        clickListener()
        setupMap()
        // restore the values from saved instance state
        // restore the values from saved instance state
        restoreValuesFromBundle(savedInstanceState)
        init()
    }

    private fun setupMap() {
        val mapFragment = childFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(this)
    }

    private fun init() {
        getViewModel().getActivityOptionsAPI()
        setCurrentDate()
        initLocation()
        Timer().scheduleAtFixedRate(object : TimerTask() {
            override fun run() {
                if (!getViewModel().isPauseSession.get()) {
                    getViewModel().timeCount.set(getViewModel().timeCount.get()?.plus(1000))
                }
                setTime()
            }
        }, 0, 1000)
    }

    private fun setCurrentDate() {
        val cal = Calendar.getInstance()
        getViewModel().activityStartDateYear.set(cal[Calendar.YEAR])
        getViewModel().activityStartDateMonth.set(cal[Calendar.MONTH] + 1)
        getViewModel().activityStartDateDay.set(cal[Calendar.DAY_OF_MONTH])

        val hours = cal[Calendar.HOUR_OF_DAY]
        log("hours $hours")
        val AM_PM: String = if (hours < 12) AppConstants.KEYS.AM else AppConstants.KEYS.PM
        val newHours = if (hours < 12) hours else hours - 12
        getViewModel().activityStartTimeHH.set(newHours)
        getViewModel().activityStartTimeMM.set(cal[Calendar.MINUTE])
        getViewModel().activityStartTimeAA.set(AM_PM).apply {
            getViewModel().activityDate.set(
                DateTimeUtils.getDateFromDateFormatToTODateFormat(
                    "${getViewModel().activityStartDateYear.get()} ${getViewModel().activityStartDateMonth.get()} ${getViewModel().activityStartDateDay.get()} ${getViewModel().activityStartTimeHH.get()}:${getViewModel().activityStartTimeMM.get()} ${getViewModel().activityStartTimeAA.get()}",
                    DateTimeUtils.dateFormatYYYYMMDD_HH_MM_A,
                    DateTimeUtils.dateFormatDDMMMYYYY_HHMMA
                )
            )
        }
    }

    private fun setActivityTitle() {
        val data = getViewModel().selectedActivityTypes.get()
        val selectedActivityTitle =
            getViewModel().activityTitleList.get()?.find { it.key == data?.key }
        if (selectedActivityTitle != null) {
            val selectedActivityTypeText =
                getTimingText().plus(" ").plus(selectedActivityTitle.value)
            getViewModel().selectedActivityTitle.set(selectedActivityTypeText)
        }

        val distanceVisibilityModel =
            getViewModel().visibilityOffList.get()?.find { it.key == AppConstants.KEYS.DISTANCE }
        log("distanceVisibilityModel ${Gson().toJson(distanceVisibilityModel)} ${data?.key}")
        data?.key?.let {
            distanceVisibilityModel?.value?.contains(it)
                ?.let { getViewModel().distanceVisibility.set(!it) }
        }

        val avgPaceVisibilityModel =
            getViewModel().visibilityOffList.get()?.find { it.key == AppConstants.KEYS.AVERAGEPACE }
        log("avgPaceVisibilityModel ${Gson().toJson(avgPaceVisibilityModel)} ${data?.key}")
        data?.key?.let {
            avgPaceVisibilityModel?.value?.contains(it)
                ?.let { getViewModel().avgPaceVisibility.set(!it) }
        }

    }

    private fun getTimingText(): String {
        val morningStart = "00:00"
        val morningEnd = "12:00"
        val afterNoonStart = "12:00"
        val afterNoonEnd = "16:00"
        val eveningStart = "16:00"
        val eveningEnd = "21:00"

        val pattern = "HH:mm"
        val sdf = SimpleDateFormat(pattern, Locale.US)
        val newHours =
            if (getViewModel().activityStartTimeAA.get() == AppConstants.KEYS.AM) getViewModel().activityStartTimeHH.get() else getViewModel().activityStartTimeHH.get()
                ?.plus(12)
        val selectedTime =
            newHours.toString() + ":" + getViewModel().activityStartTimeMM.get().toString()
        log("selectedTime $selectedTime")
        try {
            val date = Date()
            val now: String = sdf.format(date.time)
            val nowDate: Date? = sdf.parse(now)
            val morningStartDate: Date? = sdf.parse(morningStart)
            val morningEndDate: Date? = sdf.parse(morningEnd)
            val afterNoonStartDate: Date? = sdf.parse(afterNoonStart)
            val afterNoonEndDate: Date? = sdf.parse(afterNoonEnd)
            val eveningStartDate: Date? = sdf.parse(eveningStart)
            val eveningEndDate: Date? = sdf.parse(eveningEnd)
            if (nowDate?.after(morningStartDate) == true && nowDate.before(morningEndDate)) {
                return "Morning"
            } else if (nowDate?.after(afterNoonStartDate) == true && nowDate.before(afterNoonEndDate)) {
                return "Afternoon"
            } else if (nowDate?.after(eveningStartDate) == true && nowDate.before(eveningEndDate)) {
                return "Evening"
            } else return ""
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return ""
    }

    private fun setTime() {
        val timeInMillis = getViewModel().timeCount.get() ?: 0
        val seconds = (timeInMillis / 1000).toInt() % 60
        val minutes = (timeInMillis / (1000 * 60) % 60)
        val hours = (timeInMillis / (1000 * 60 * 60) % 24)
//        val text = String.format(
//            Locale.US,
//            "%02d:%02d:%02d",
//            TimeUnit.MILLISECONDS.toHours(timeInMillis),
//            TimeUnit.MILLISECONDS.toMinutes(timeInMillis) -
//                    TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(timeInMillis)), // The change is in this line
//            TimeUnit.MILLISECONDS.toSeconds(timeInMillis) -
//                    TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(timeInMillis))
//        );
        val text = String.format(Locale.US, "%02d:%02d:%02d", hours, minutes, seconds);
        getViewModel().time.set(text)
    }

    private fun toolbar() {
        mViewBinding!!.toolBarLayout.tvTitle.hide()
        mViewBinding!!.toolBarLayout.cvBack.visible()
        mViewBinding!!.toolBarLayout.cvBack.setOnClickListener { onBack() }
    }

    private fun handleBackPress() {
        val callback: OnBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                onBack()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)
    }

    override fun onBack() {
        mNavController!!.navigateUp()
//        requireActivity().finish()
    }

    private fun clickListener() {
        mViewBinding!!.btnResume.setOnClickListener {
            getViewModel().isPauseSession.set(!getViewModel().isPauseSession.get())
        }
        mViewBinding!!.btnFinish.setOnClickListener {
            validateDataAndCallAPI()
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        if (getViewModel().currentLat.get() != 0.0 && getViewModel().currentLng.get() != 0.0) {
        }
    }

    //Location
    private fun initLocation() {
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        mSettingsClient = LocationServices.getSettingsClient(requireActivity())
        mLocationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                super.onLocationResult(locationResult)
                // location is received
                log("locationResult LAT: ${locationResult.lastLocation.latitude} LONG: ${locationResult.lastLocation.longitude} SPEED: ${locationResult.lastLocation.speed}")
                val latLong = LatLng(
                    locationResult.lastLocation.latitude,
                    locationResult.lastLocation.longitude
                )
//                val speed = (locationResult.lastLocation.speed * 60 / 1000).toInt()
//                getViewModel().speed.set(speed.toString())
                if (!getViewModel().isPauseSession.get()) {
                    val lastLatLongList = ArrayList<LatLng>()
                    if (getViewModel().latLongArray.get() != null)
                        lastLatLongList.addAll(getViewModel().latLongArray.get()!!)
                    lastLatLongList.add(latLong)
                    getViewModel().latLongArray.set(lastLatLongList)
                    mCurrentLocation = locationResult.lastLocation

                    if (lastLatLongList.isNotEmpty()) {
                        if (lastLatLongList.size > 2) {
                            val secondLast = lastLatLongList[lastLatLongList.size - 2]
//                                if (lastLatLongList.size > 2) lastLatLongList[lastLatLongList.size - 2] else lastLatLongList.first()
                            val last = lastLatLongList.last()
                            val distanceInKM =
                                getDistanceFromLatLonInKm(
                                    secondLast.latitude,
                                    secondLast.longitude,
                                    last.latitude,
                                    last.longitude
                                )
                            log("first $secondLast last $last distance $distanceInKM")
                            val lastDistance = getViewModel().totalDistance.get() ?: 0.0
                            val totalDistance = lastDistance.plus(distanceInKM)
                            getViewModel().totalDistance.set(totalDistance)

                            getViewModel().distance.set(DecimalFormat("#.##").format(totalDistance))

                            val durationInSeconds = (getViewModel().timeCount.get() ?: 1000) / 1000
                            val paceInSeconds = durationInSeconds.div(totalDistance)
                            val paceInMinutes = paceInSeconds / 60

                            getViewModel().speed.set(DecimalFormat("#.##").format(paceInMinutes))
//                            log("distanceInKM>>> $distanceInKM  durationInSeconds>>> $durationInSeconds speed>>> $paceInMinutes ")
//                            mMap!!.clear()
                            if (isAdded) {
                                val line: Polyline = mMap!!.addPolyline(
                                    PolylineOptions()
                                        .add(last, secondLast)
                                        .width(5f)
                                        .color(
                                            ContextCompat.getColor(
                                                requireContext(),
                                                R.color.themeColor
                                            )
                                        )
                                )
                                zoomMapTOLatLong(latLong)
                            }
                            val encoded = PolyUtil.encode(lastLatLongList)
                            getViewModel().encodedPath.set(encoded)
//                            log("encoded>> $encoded")
//                            log("decoded>> ${Gson().toJson(PolyUtil.decode(encoded))}")
                        }
                    }
                }
                if (!getViewModel().isCurrentLocationSet.get()) {
                    updateLocationUI()
                }
            }
        }
        mRequestingLocationUpdates = false
        mLocationRequest = LocationRequest()
        mLocationRequest!!.interval = UPDATE_INTERVAL_IN_MILLISECONDS
        mLocationRequest!!.fastestInterval = FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS
        mLocationRequest!!.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        val builder = LocationSettingsRequest.Builder()
        builder.addLocationRequest(mLocationRequest!!)
        mLocationSettingsRequest = builder.build()
        fetchLocation()
    }

    private fun fetchLocation() {
        // Requesting ACCESS_FINE_LOCATION using Dexter library
        Dexter.withContext(requireActivity())
            .withPermission(Manifest.permission.ACCESS_FINE_LOCATION)
            .withListener(object : PermissionListener {
                override fun onPermissionGranted(response: PermissionGrantedResponse) {
                    mRequestingLocationUpdates = true
                    startLocationUpdates()
                }

                override fun onPermissionDenied(response: PermissionDeniedResponse) {
                    if (response.isPermanentlyDenied) {
                        // open device settings when the permission is
                        // denied permanently
                        openSettings()
                    }
                }

                override fun onPermissionRationaleShouldBeShown(
                    permission: PermissionRequest,
                    token: PermissionToken
                ) {
                    token.continuePermissionRequest()
                }
            }).check()
    }

    private fun openSettings() {
        val intent = Intent()
        intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
        val uri = Uri.fromParts("package", BuildConfig.APPLICATION_ID, null)
        intent.data = uri
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
    }

    /**
     * Starting location updates
     * Check whether location settings are satisfied and then
     * location updates will be requested
     */
    private fun startLocationUpdates() {
        mSettingsClient
            ?.checkLocationSettings(mLocationSettingsRequest!!)
            ?.addOnSuccessListener(requireActivity()) {
                Log.i("", "All location settings are satisfied.")
                if (ActivityCompat.checkSelfPermission(
                        requireContext(),
                        Manifest.permission.ACCESS_FINE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                        requireContext(),
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return@addOnSuccessListener
                }
                mFusedLocationClient!!.requestLocationUpdates(
                    mLocationRequest!!,
                    mLocationCallback!!, Looper.myLooper()!!
                )
//                mFusedLocationClient!!.removeLocationUpdates(mLocationCallback!!) //Remove Location Update
                mMap?.isMyLocationEnabled = true
                //                        updateLocationUI();
            }
            ?.addOnFailureListener(requireActivity()) { e ->
                val statusCode = (e as ApiException).statusCode
                when (statusCode) {
                    LocationSettingsStatusCodes.RESOLUTION_REQUIRED -> {
                        Log.i(
                            "", "Location settings are not satisfied. Attempting to upgrade " +
                                    "location settings "
                        )
                        try {
                            // Show the dialog by calling startResolutionForResult(), and check the
                            // result in onActivityResult().
                            val rae = e as ResolvableApiException
                            rae.startResolutionForResult(requireActivity(), REQUEST_CHECK_SETTINGS)
                        } catch (sie: IntentSender.SendIntentException) {
                            Log.i("", "PendingIntent unable to execute request.")
                        }
                    }
                    LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE ->
                        "Location settings are inadequate, and cannot be " +
                                "fixed here. Fix in Settings."
                }
                updateLocationUI()
            }
    }

    /**
     * Update the UI displaying the location data
     * and toggling the buttons
     */
    private fun updateLocationUI() {
        if (mCurrentLocation != null) {
            if (isAdded) {
                getViewModel().currentLat.set(mCurrentLocation!!.latitude)
                getViewModel().currentLng.set(mCurrentLocation!!.longitude).also { setMapMarker() }
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean("is_requesting_updates", mRequestingLocationUpdates)
        outState.putParcelable("last_known_location", mCurrentLocation)
    }

    private fun setMapMarker() {
        if (mMap != null) {
            getViewModel().isCurrentLocationSet.set(true)
            val currentLocation =
                LatLng(getViewModel().currentLat.get()!!, getViewModel().currentLng.get()!!)
            addMarker(currentLocation, true)
        }
    }

    private fun addMarker(latLng: LatLng, isCameraZoom: Boolean) {
//        mMap!!.clear()
        getViewModel().currentLat.set(latLng.latitude)
        getViewModel().currentLng.set(latLng.longitude)
//        mMap!!.addMarker(
//            MarkerOptions().position(latLng)
//                .title(getViewModel().selectedAddress.get())
//                .icon(CommonUtils.bitmapDescriptorFromVector(requireContext()))
//        )
        if (isCameraZoom) {
            zoomMapTOLatLong(latLng)
        }
    }

    private fun zoomMapTOLatLong(latLng: LatLng) {
        mMap!!.moveCamera(CameraUpdateFactory.newLatLng(latLng))
        val cameraPosition = CameraPosition.Builder()
            .target(latLng) // Sets the center of the map to location user
            .zoom(16f) // Sets the zoom
            .bearing(90f) // Sets the orientation of the camera to east
            .tilt(40f) // Sets the tilt of the camera to 30 degrees
            .build()
        mMap!!.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
    }

    /**
     * Restoring values from saved instance state
     */
    private fun restoreValuesFromBundle(savedInstanceState: Bundle?) {
        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey("is_requesting_updates")) {
                mRequestingLocationUpdates = savedInstanceState.getBoolean("is_requesting_updates")
            }
            if (savedInstanceState.containsKey("last_known_location")) {
                mCurrentLocation = savedInstanceState.getParcelable("last_known_location")
            }
        }
//        updateLocationUI();
    }

    override fun activityOptionsFetchSuccessfully(activityOptionsData: ActivityOptionsData?) {
        if (activityOptionsData != null) {
            getViewModel().activityTitleList.set(activityOptionsData.activityTitle)
            getViewModel().visibilityOffList.set(activityOptionsData.visibilityOff)
            if (activityOptionsData.distance.isNotEmpty()) {
//                val distanceInKm = activityOptionsData.distance.find { it.key == AppConstants.KEYS.Kilometer }
                getViewModel().selectedDistanceUnit.set(activityOptionsData.distance.first())
            }
            if (activityOptionsData.avgPace.isNotEmpty()) {
                val paceUnit = activityOptionsData.avgPace.first()
                getViewModel().selectedPaceUnit.set(paceUnit)
            }
            setActivityTitle()
        }
    }


    private fun distance(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double {
        val theta = lon1 - lon2
        var dist = (sin(deg2rad(lat1))
                * sin(deg2rad(lat2))
                + (cos(deg2rad(lat1))
                * cos(deg2rad(lat2))
                * cos(deg2rad(theta))))
        dist = acos(dist)
        dist = rad2deg(dist)
        dist *= 60 * 1.1515
        return dist
    }

    private fun deg2rad(deg: Double): Double {
        return deg * Math.PI / 180.0
    }

    private fun rad2deg(rad: Double): Double {
        return rad * 180.0 / Math.PI
    }

    fun getDistanceFromLatLonInKm(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double {
        val R = 6371; // Radius of the earth in km
        val dLat = deg2rad(lat2 - lat1);  // deg2rad below
        val dLon = deg2rad(lon2 - lon1);
        val a =
            sin(dLat / 2) * sin(dLat / 2) +
                    cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) *
                    sin(dLon / 2) * sin(dLon / 2)
        ;
        val c = 2 * atan2(sqrt(a), sqrt(1 - a));
        val d = R * c; // Distance in km
        return d;
    }


    private fun validateDataAndCallAPI() {
        hideKeyboard()
        val activityTitle = getViewModel().selectedActivityTitle.get() ?: ""
        val activityDescription = ""
        val activityType = getViewModel().selectedActivityTypes.get()
        val activityDate = getViewModel().activityDate.get()?.let {
            DateTimeUtils.getLocalDateFromDateFormatToTODateFormat(
                it,
                DateTimeUtils.dateFormatDDMMMYYYY_HHMMA,
                DateTimeUtils.dateFormatServer_YYYY_MM_DD_T_HHMMSS
            )
        }
        val activityDateUTC = getViewModel().activityDate.get()?.let {
            DateTimeUtils.getDateLocalTOUTCDateFormat(
                it,
                DateTimeUtils.dateFormatDDMMMYYYY_HHMMA,
                DateTimeUtils.dateFormatServer_YYYY_MM_DD_T_HHMMSSZ
            )
        }
        val durationInSeconds = ((getViewModel().timeCount.get() ?: 1000) / 1000).toInt()
        val distance = getViewModel().distance.get() ?: ""
        val distanceUnit = getViewModel().selectedDistanceUnit.get()
        val activityPace = getViewModel().speed.get() ?: ""
        val paceUnit = getViewModel().selectedPaceUnit.get()

        if (activityType == null) {
            showMessage("Please select activity type")
        } else if (TextUtils.isEmpty(activityTitle)) {
            showMessage("Please enter activity title")
        } else {
            val user = getViewModel().user.get()
            val params = HashMap<String, Any>()
            params["UserId"] = getViewModel().getUserId()
            params["userName"] = user?.userName ?: ""
            params["profilePhotoUrl"] = user?.profilePhotoUrl ?: ""
            params["ActivityTitle"] = activityTitle
            params["Description"] = activityDescription
            params["ActivityType.Key"] = activityType.key
            params["ActivityType.Name"] = activityType.name
            params["ActivityType.Icon"] = activityType.icon ?: ""
            params["ActivityType.Color"] = activityType.color
            params["ActivityType.CombinationNo"] = activityType.combinationNo
            params["StartDateTimeLocalDevice"] = activityDate ?: ""
            params["StartDateTimeUTC"] = activityDateUTC ?: ""
            params["DurationInSeconds"] = durationInSeconds
            params["Distance.Key"] = distanceUnit?.key ?: ""
            params["Distance.Value"] = distance.ifEmpty { "0" }
            params["AveragePace.Key"] = paceUnit?.key ?: ""
            params["AveragePace.Value"] = activityPace.toString().replace(":", ".")
            params["RoutePath"] = getViewModel().encodedPath.get() ?: ""
            getViewModel().createActivity(params)
        }
    }

    override fun createActivitySuccess(data: ActivityData?) {
        val bundle = Bundle()
        bundle.putString(AppConstants.EXTRAS.ACTIVITY_ID, data?.activityId)
        bundle.putBoolean(AppConstants.FROM.TRACK_ACTIVITY, true)
        mNavController!!.navigate(R.id.toSuccessFragment, bundle)
    }

}