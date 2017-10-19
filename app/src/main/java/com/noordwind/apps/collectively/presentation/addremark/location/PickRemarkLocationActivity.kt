package com.noordwind.apps.collectively.presentation.addremark.location

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.LocationListener
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.noordwind.apps.collectively.Constants
import com.noordwind.apps.collectively.R
import com.noordwind.apps.collectively.TheApp
import com.noordwind.apps.collectively.data.repository.util.LocationRepository
import com.noordwind.apps.collectively.domain.thread.PostExecutionThread
import com.noordwind.apps.collectively.domain.thread.UseCaseThread
import com.noordwind.apps.collectively.presentation.addremark.PickRemarkLocationMvp
import com.noordwind.apps.collectively.presentation.addremark.PickRemarkLocationPresenter
import com.noordwind.apps.collectively.presentation.settings.dagger.PickRemarkLocationScreenModule
import com.noordwind.apps.collectively.usecases.LoadAddressFromLocationUseCase
import jonathanfinerty.once.Once
import kotlinx.android.synthetic.main.activity_pick_location.*
import kotlinx.android.synthetic.main.view_toolbar_with_title.*
import java.util.*
import javax.inject.Inject

class PickRemarkLocationActivity : com.noordwind.apps.collectively.presentation.BaseActivity(), OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener, GoogleMap.OnMapLongClickListener, PickRemarkLocationMvp.View {
    companion object {
        fun start(activity: Activity, latLng: LatLng?) {
            val intent = Intent(activity, PickRemarkLocationActivity::class.java)
            intent.putExtra(Constants.BundleKey.LOCATION, latLng)
            activity.startActivityForResult(intent, Constants.RequestCodes.PICK_LOCATION)
        }
    }

    @Inject
    lateinit var presenter: PickRemarkLocationMvp.Presenter

    private var map: GoogleMap? = null
    private var googleApiClient: GoogleApiClient? = null
    private var locationRequest: LocationRequest? = null
    private val MY_PERMISSIONS_REQUEST_LOCATION = 99
    private val markers = LinkedList<Marker>()
    private var selectedLocation: LatLng? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        TheApp[this].appComponent!!.plusPickRemarkLocationScreenComponent(PickRemarkLocationScreenModule(this)).inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pick_location)
        toolbarTitleLabel.text = getString(R.string.picki_location_screen_title)

        selectedLocation = intent.getParcelableExtra(Constants.BundleKey.LOCATION)

        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        if (!Once.beenDone(Once.THIS_APP_INSTALL, Constants.OnceKey.SHOW_PICK_LOCATION_HINT)) {
            tooltipBackground.visibility = View.VISIBLE
            tooltipBackground.setOnClickListener {
                tooltipBackground.visibility = View.GONE
                Once.markDone(Constants.OnceKey.SHOW_PICK_LOCATION_HINT)
            }
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        map?.setOnMapLongClickListener(this)
        map?.mapType = GoogleMap.MAP_TYPE_NORMAL

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                buildGoogleApiClient();
                map?.isMyLocationEnabled = true;
            }
        } else {
            buildGoogleApiClient();
            map?.isMyLocationEnabled = true;
        }

        selectedLocation?.let {
            showMarker(selectedLocation!!)

            var cameraUpdate = CameraUpdateFactory.newLatLngZoom(selectedLocation, 14.0f);
            map?.animateCamera(cameraUpdate);
        }
    }

    override fun onStart() {
        super.onStart()
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkLocationPermission();
        }
    }

    fun buildGoogleApiClient() {
        googleApiClient = GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build()
        googleApiClient?.connect()
    }

    override fun onConnected(p0: Bundle?) {
        locationRequest = LocationRequest();
        locationRequest?.interval = 1000;
        locationRequest?.fastestInterval = 1000;
        locationRequest?.priority = LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY;

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this);
        }
    }

    override fun onLocationChanged(location: Location?) {
        if (selectedLocation == null) {
            var latLng = LatLng(location?.latitude!!, location?.longitude!!);
            var cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 14.0f);
            map?.animateCamera(cameraUpdate);
        }
    }

    fun checkLocationPermission(): Boolean {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            // Asking user if explanation is needed
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an expanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

                //Prompt the user once explanation has been shown
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), MY_PERMISSIONS_REQUEST_LOCATION)


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), MY_PERMISSIONS_REQUEST_LOCATION)
            }
            return false
        } else {
            return true
        }
    }

    override fun onConnectionSuspended(p0: Int) {}
    override fun onConnectionFailed(p0: ConnectionResult) {}

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (requestCode == MY_PERMISSIONS_REQUEST_LOCATION) {
            // If request is cancelled, the result arrays are empty.
            if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                // Permission was granted.
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    if (googleApiClient == null) {
                        buildGoogleApiClient();
                    }
                    map?.isMyLocationEnabled = true;
                }

            } else {
                // Permission denied, Disable the functionality that depends on this permission.
                Toast.makeText(this, "Permission denied", Toast.LENGTH_LONG).show()
            }
        }
        // other 'case' lines to check for other permissions this app might request.
        //You can add here other case statements according to your requirement.
    }

    override fun onMapLongClick(latLng: LatLng?) {
        latLng?.let {
            showMarker(latLng)
        }
    }

    fun showMarker(latLng: LatLng) {
        markers.forEach(Marker::remove)
        markers.clear()
        var markerOptions = MarkerOptions()
        markerOptions.position(latLng)
        map!!.moveCamera(CameraUpdateFactory.newLatLng(latLng))
        markers.add(map!!.addMarker(markerOptions))
        presenter.loadAddress(latLng)
    }

    override fun showAddressLoading() {
        locationLabel.text = getString(R.string.loading_adress)
    }

    override fun showAddress(addressPretty: String) {
        locationLabel.text = addressPretty
    }

    override fun showAddressLoadingError() {
        locationLabel.text = getString(R.string.error_loading_adress)
    }

    override fun showAddressLoadingNetworkError() {
        locationLabel.text = getString(R.string.error_loading_adress)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            android.R.id.home -> {
                onBackPressed()
                return true
            }
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        if (presenter.hasAddress()) {
            var latLng = presenter.location()
            var address = presenter.address()

            var resultIntent = Intent()
            resultIntent.putExtra(Constants.BundleKey.LOCATION, latLng)
            resultIntent.putExtra(Constants.BundleKey.ADDRESS, address)

            setResult(Activity.RESULT_OK, resultIntent)
        } else {
            setResult(Activity.RESULT_CANCELED)
        }
        super.onBackPressed()
    }
}
