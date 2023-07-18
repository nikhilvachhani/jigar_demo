package com.frami.ui.common.location.fragment

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.IntentSender.SendIntentException
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
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
import androidx.fragment.app.viewModels
import com.frami.BR
import com.frami.R
import com.frami.databinding.FragmentLocationBinding
import com.frami.ui.base.BaseFragment
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
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.AutocompleteActivity
import com.google.gson.Gson
import com.karumi.dexter.BuildConfig
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import java.util.*


class LocationFragment : BaseFragment<FragmentLocationBinding, LocationFragmentViewModel>(),
    LocationFragmentNavigator, OnMapReadyCallback {

    private val viewModelInstance: LocationFragmentViewModel by viewModels {
        viewModeFactory
    }
    private var mViewBinding: FragmentLocationBinding? = null
    override fun getBindingVariable(): Int = BR.viewModel

    override fun getLayoutId(): Int = R.layout.fragment_location

    override fun getViewModel(): LocationFragmentViewModel = viewModelInstance

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
    val AUTOCOMPLETE_REQUEST_CODE = 101
    val UPDATE_INTERVAL_IN_MILLISECONDS: Long = 100000000
    val FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS: Long = 5000

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mViewBinding = getViewDataBinding()
        viewModelInstance.setNavigator(this)

        toolbar()
        handleBackPress()
        clickListener()
        init()

        setupMap()
        // restore the values from saved instance state
        // restore the values from saved instance state
        restoreValuesFromBundle(savedInstanceState)
//        if (!Places.isInitialized()) {
//            Places.initialize(
//                requireContext(),
//                requireActivity().getString(R.string.google_map_key)
//            )
//        }
    }

    private fun init() {
        initLocation()
    }

    private fun setupMap() {
        val mapFragment = childFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(this)
    }

    private fun toolbar() {
        mViewBinding!!.toolBarLayout.tvTitle.text = getString(R.string.location)
        mViewBinding!!.toolBarLayout.toolBar.setNavigationOnClickListener { v -> onBack() }
        mViewBinding!!.toolBarLayout.cvSearch.hide()
        mViewBinding!!.toolBarLayout.cvNotification.hide()
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
    }

    private fun clickListener() {
        mViewBinding!!.clCurrentLocation.setOnClickListener {
            initLocation()
        }
        mViewBinding!!.btnChoose.setOnClickListener {
            val returnIntent = Intent()
            val bundle = Bundle()
            bundle.putString(AppConstants.EXTRAS.ADDRESS, getViewModel().selectedAddress.get())
            bundle.putString(AppConstants.EXTRAS.LATITUDE, getViewModel().selectedLatitude.get().toString() ?: "0.0")
            bundle.putString(AppConstants.EXTRAS.LONGITUDE, getViewModel().selectedLongitude.get().toString() ?: "0.0")
            returnIntent.putExtras(bundle)
            requireActivity().setResult(Activity.RESULT_OK, returnIntent)
            requireActivity().finish()
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        if (getViewModel().currentLat.get() != 0.0 && getViewModel().currentLng.get() != 0.0) {
            setMapMarker()
        }
    }

    private fun setMapMarker() {
        if (mMap != null) {
            val currentLocation =
                LatLng(getViewModel().currentLat.get()!!, getViewModel().currentLng.get()!!)
            addMarker(currentLocation, true)

            mMap!!.setOnCameraIdleListener { //get latlng at the center by calling
                val midLatLng: LatLng = mMap!!.cameraPosition.target
                addMarker(midLatLng, false)
            }
        }
    }

    private fun addMarker(latLng: LatLng, isCameraZoom: Boolean) {
//        mMap!!.clear()
        getAddress(latLng)
        getViewModel().selectedLatitude.set(latLng.latitude)
        getViewModel().selectedLongitude.set(latLng.longitude)
//        mMap!!.addMarker(
//            MarkerOptions().position(latLng)
//                .title(getViewModel().selectedAddress.get())
//                .icon(CommonUtils.bitmapDescriptorFromVector(requireContext()))
//        )
        if (isCameraZoom) {
            mMap!!.moveCamera(CameraUpdateFactory.newLatLng(latLng))
            val cameraPosition = CameraPosition.Builder()
                .target(latLng) // Sets the center of the map to location user
                .zoom(9f) // Sets the zoom
                .bearing(90f) // Sets the orientation of the camera to east
                .tilt(40f) // Sets the tilt of the camera to 30 degrees
                .build()
            mMap!!.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
        }
    }

    private fun getAddress(latLng: LatLng) {
        var locationAddress = ""
        val addresses: List<Address>
        val geocoder = Geocoder(requireContext(), Locale.US)
        try {
            addresses = geocoder.getFromLocation(
                latLng.latitude,
                latLng.longitude,
                1,
            )!! // Here 1 represent max location result to returned, by documents it recommended 1 to 5
            val address =
                addresses[0].getAddressLine(0) // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
            locationAddress += if (TextUtils.isEmpty(address)) "" else address
            //            getViewModel().setSelectedAddress((TextUtils.isEmpty(address) ? "" : address));
            val city = addresses[0].locality
            getViewModel().selectedCity.set(if (TextUtils.isEmpty(city)) "" else city)
            locationAddress += if (TextUtils.isEmpty(city)) "" else ", $city"
            val postalCode = addresses[0].postalCode
            locationAddress += if (TextUtils.isEmpty(postalCode)) "" else ", $postalCode"
            getViewModel().selectedZIP.set(if (TextUtils.isEmpty(postalCode)) "" else postalCode)
            val state = addresses[0].adminArea
            locationAddress += if (TextUtils.isEmpty(state)) "" else ", $state"
            getViewModel().selectedState.set(if (TextUtils.isEmpty(state)) "" else state)
            val country = addresses[0].countryName
            locationAddress += if (TextUtils.isEmpty(country)) "" else ", $country"
            val knownName = addresses[0].featureName // Only if available else return NULL
        } catch (e: Exception) {
            e.printStackTrace()
        }
        getViewModel().selectedAddress.set(locationAddress)
    }

    //Location
    private fun initLocation() {
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        mSettingsClient = LocationServices.getSettingsClient(requireActivity())
        mLocationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                super.onLocationResult(locationResult)
                // location is received
                log("locationResult " + Gson().toJson(locationResult))
                mCurrentLocation = locationResult.lastLocation
                updateLocationUI()
            }
        }
        mRequestingLocationUpdates = false
        mLocationRequest = LocationRequest()
        mLocationRequest!!.setInterval(UPDATE_INTERVAL_IN_MILLISECONDS)
        mLocationRequest!!.setFastestInterval(FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS)
        mLocationRequest!!.setPriority(com.google.android.gms.location.LocationRequest.PRIORITY_HIGH_ACCURACY)
        val builder = LocationSettingsRequest.Builder()
        builder.addLocationRequest(mLocationRequest!!)
        mLocationSettingsRequest = builder.build()
        fetchLocation()
    }

    private fun fetchLocation() {
        // Requesting ACCESS_FINE_LOCATION using Dexter library
        Dexter.withActivity(requireActivity())
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
                        } catch (sie: SendIntentException) {
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
                getViewModel().currentLng.set(mCurrentLocation!!.longitude)
                setMapMarker()
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean("is_requesting_updates", mRequestingLocationUpdates)
        outState.putParcelable("last_known_location", mCurrentLocation)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        setMapMarker()
        if (requestCode == AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                try {
                    val place = Autocomplete.getPlaceFromIntent(data)
                    log("Place: " + "ID: " + place.id + "address:" + place.address + "Name:" + place.name + " latlong: " + place.latLng)
                    val address = place.address
                    //                    log("PLACE>>>> " + new Gson().toJson(place));
                    // do query with address
//                    getViewModel().setLatitude(place.latLng.latitude)
//                    getViewModel().setLongitude(place.latLng.longitude)
                    getViewModel().selectedAddress.set(place.address)
                    setMapMarker()
                } catch (e: java.lang.Exception) {
                    e.printStackTrace()
                    showMessage(e.message)
                }
            } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
                // TODO: Handle the error.
                val status = Autocomplete.getStatusFromIntent(data)
                log("Error: " + status.statusMessage)
                showMessage(status.statusMessage)
            } else if (resultCode == Activity.RESULT_CANCELED) {
                // The user canceled the operation.
            }
        }
    }

    /**updateLocationUI
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