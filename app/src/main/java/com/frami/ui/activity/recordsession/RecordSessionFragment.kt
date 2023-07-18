package com.frami.ui.activity.recordsession

import android.Manifest
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import android.location.Location
import android.net.Uri
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.util.Log
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.core.app.ActivityCompat
import androidx.fragment.app.viewModels
import com.frami.BR
import com.frami.R
import com.frami.data.model.home.ActivityTypes
import com.frami.databinding.FragmentRecordSessionBinding
import com.frami.ui.base.BaseFragment
import com.frami.ui.common.SelectActivityTypesDialog
import com.frami.utils.AppConstants
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
import com.google.gson.Gson
import com.karumi.dexter.BuildConfig
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener


class RecordSessionFragment :
    BaseFragment<FragmentRecordSessionBinding, RecordSessionFragmentViewModel>(),
    RecordSessionFragmentNavigator,
    SelectActivityTypesDialog.OnDialogActionListener, OnMapReadyCallback {

    private val viewModelInstance: RecordSessionFragmentViewModel by viewModels {
        viewModeFactory
    }
    private var mViewBinding: FragmentRecordSessionBinding? = null
    override fun getBindingVariable(): Int = BR.viewModel

    override fun getLayoutId(): Int = R.layout.fragment_record_session

    override fun getViewModel(): RecordSessionFragmentViewModel = viewModelInstance

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
    val FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS: Long = 5000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
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
        initLocation()
        getViewModel().getActivityTypesAPI()
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
        mViewBinding!!.clActivityType.setOnClickListener {
            showActivityTypeDialog()
        }
        mViewBinding!!.btnNext.setOnClickListener {
            val bundle = Bundle()
            getViewModel().selectedActivityTypes.get().let {
                bundle.putSerializable(AppConstants.FROM.ACTIVITY_TYPE, it)
            }
            mNavController!!.navigate(R.id.toTrackLocationFragment, bundle)
        }
    }

    private fun showActivityTypeDialog() {
        val dialog =
            getViewModel().activityTypesList.get()?.let {
                it.forEach { it1 ->
                    it1.isSelected = (it1.name === getViewModel().selectedActivityTypes.get()?.name)
                }
                SelectActivityTypesDialog(
                    requireActivity(),
                    it
                )
            }
        dialog?.setListener(this)
        dialog?.show()
    }

    override fun onItemSelect(data: ActivityTypes) {
        getViewModel().selectedActivityTypes.set(data)
    }

    override fun onSelectedItems(data: List<ActivityTypes>) {
        getViewModel().selectedActivityNames.set(getSelectedActivityTypeName(data))
        getViewModel().selectedActivityTypes.set(getSelectedActivityTypeIcon(data))
    }

    override fun activityTypesFetchSuccessfully(list: List<ActivityTypes>) {
        if (list.isNotEmpty()) {
            getViewModel().selectedActivityTypes.set(list[0])
        }
        getViewModel().activityTypesList.set(list)
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
                log("locationResult " + Gson().toJson(locationResult.lastLocation))
                mCurrentLocation = locationResult.lastLocation
                if (!getViewModel().isCurrentLocationSet.get())
                    updateLocationUI()
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

//            mMap!!.setOnCameraIdleListener { //get latlng at the center by calling
//                val midLatLng: LatLng = mMap!!.cameraPosition.target
//                addMarker(midLatLng, false)
//            }
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
            mMap!!.moveCamera(CameraUpdateFactory.newLatLng(latLng))
            val cameraPosition = CameraPosition.Builder()
                .target(latLng) // Sets the center of the map to location user
                .zoom(16f) // Sets the zoom
                .bearing(90f) // Sets the orientation of the camera to east
                .tilt(40f) // Sets the tilt of the camera to 30 degrees
                .build()
            mMap!!.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
        }
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
}