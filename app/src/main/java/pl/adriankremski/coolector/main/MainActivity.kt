package pl.adriankremski.coolector.main

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.Typeface
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBarDrawerToggle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.RelativeSizeSpan
import android.text.style.StyleSpan
import android.view.Gravity
import android.view.Menu
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.getbase.floatingactionbutton.FloatingActionButton
import com.getbase.floatingactionbutton.FloatingActionsMenu
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.LocationListener
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import pl.adriankremski.coolector.BaseActivity
import pl.adriankremski.coolector.R
import pl.adriankremski.coolector.TheApp
import pl.adriankremski.coolector.adapter.delegates.MainScreenRemarksAdapterDelegate
import pl.adriankremski.coolector.addremark.AddRemarkActivity
import pl.adriankremski.coolector.authentication.login.MainPresenter
import pl.adriankremski.coolector.model.Remark
import pl.adriankremski.coolector.model.RemarkCategory
import pl.adriankremski.coolector.repository.RemarksRepository
import pl.adriankremski.coolector.utils.colorOfCategory
import pl.adriankremski.coolector.utils.toBitmapDescriptor
import pl.adriankremski.coolector.utils.uppercaseFirstLetter
import java.util.*
import javax.inject.Inject


class MainActivity : BaseActivity(), MainMvp.View, OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener, MainScreenRemarksAdapterDelegate.OnRemarkSelectedListener {
    companion object {
        fun login(context: Context) {
            val intent = Intent(context, MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            context.startActivity(intent)
        }
    }

    private lateinit var mFloatingActionsMenu: FloatingActionsMenu
    private lateinit var mTitleLabel: TextView
    private lateinit var mToolbarOptionLabel: TextView
    private var mMap: GoogleMap? = null
    private var mGoogleApiClient: GoogleApiClient? = null
    private var mLocationRequest: LocationRequest? = null
    private var mLastLocation: Location? = null
    private var mCurrLocationMarker: Marker? = null
    private val MY_PERMISSIONS_REQUEST_LOCATION = 99
    private val mRemarksMarkers: LinkedList<Marker> = LinkedList<Marker>()

    @Inject
    lateinit var mRemarksRepository: RemarksRepository
    lateinit var mMainPresenter: MainPresenter

    private lateinit var drawerToggle: ActionBarDrawerToggle

    private lateinit var drawerLayout: DrawerLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        TheApp[this].appComponent?.inject(this)
        setContentView(R.layout.activity_main)

        val span = SpannableString(getString(R.string.main_screen_title))
        span.setSpan(RelativeSizeSpan(1.2f), 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        span.setSpan(StyleSpan(Typeface.BOLD), 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)

        mTitleLabel = findViewById(R.id.title) as TextView
        mToolbarOptionLabel = findViewById(R.id.option) as TextView
        mToolbarOptionLabel.text = getString(R.string.list)
        mToolbarOptionLabel.visibility = View.GONE
        mToolbarOptionLabel.setOnClickListener { drawerLayout.openDrawer(Gravity.RIGHT) }

        drawerLayout = findViewById(R.id.drawer_layout) as DrawerLayout
        drawerToggle = ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close)
        drawerLayout.setDrawerListener(drawerToggle)
        drawerToggle.syncState()

        mTitleLabel.text = span
        mFloatingActionsMenu = findViewById(R.id.actions) as FloatingActionsMenu

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkLocationPermission();
        }

        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        mMainPresenter = MainPresenter(this, mRemarksRepository)
        mMainPresenter.loadRemarkCategories()
    }

    override fun clearCategories() {
//        mFloatingActionsMenu.removeAllViews()
    }

    override fun showRemarkCategory(remarkCategory: RemarkCategory) {
        var remarkButton = FloatingActionButton(baseContext);
        remarkButton.colorNormal = Color.parseColor(remarkCategory.name.colorOfCategory())
        remarkButton.title = remarkCategory.name.uppercaseFirstLetter()
        remarkButton.setOnClickListener { runOnUiThread { AddRemarkActivity.start(baseContext) } }
        mFloatingActionsMenu.addButton(remarkButton)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMainPresenter.loadRemarks()
        mMap = googleMap
        mMap?.mapType = GoogleMap.MAP_TYPE_NORMAL

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                buildGoogleApiClient();
                mMap?.isMyLocationEnabled = true;
            }
        } else {
            buildGoogleApiClient();
            mMap?.isMyLocationEnabled = true;
        }
    }

    fun buildGoogleApiClient() {
        mGoogleApiClient = GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build()
        mGoogleApiClient?.connect()
    }

    override fun onConnected(p0: Bundle?) {
        mLocationRequest = LocationRequest();
        mLocationRequest?.interval = 1000;
        mLocationRequest?.fastestInterval = 1000;
        mLocationRequest?.priority = LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY;

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }
    }

    override fun onLocationChanged(location: Location?) {
        mLastLocation = location;

        if (mCurrLocationMarker != null) {
            mCurrLocationMarker?.remove();
        }

        //Place current location marker
        var latLng = LatLng(location?.latitude!!, location?.longitude!!);

        var markerOptions = MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title("Current Position");
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));

        mCurrLocationMarker = mMap?.addMarker(markerOptions);

        //move map camera
        mMap?.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap?.animateCamera(CameraUpdateFactory.zoomTo(12.0f));

        //stop location updates
        if (mGoogleApiClient != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
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
                    if (mGoogleApiClient == null) {
                        buildGoogleApiClient();
                    }
                    mMap?.isMyLocationEnabled = true;
                }

            } else {
                // Permission denied, Disable the functionality that depends on this permission.
                Toast.makeText(this, "Permission denied", Toast.LENGTH_LONG).show()
            }
        }
        // other 'case' lines to check for other permissions this app might request.
        //You can add here other case statements according to your requirement.
    }

    override fun showRemarks(remarks: List<Remark>) {
        mToolbarOptionLabel.visibility = View.VISIBLE
        mRemarksMarkers.forEach(Marker::remove)
        remarks.forEach {
            var latLng = LatLng(it.location.coordinates[1], it.location.coordinates[0]);
            var markerOptions = MarkerOptions();
            markerOptions.position(latLng);
            markerOptions.title(it.description);
            markerOptions.icon(it.category.colorOfCategory().toBitmapDescriptor());
            mRemarksMarkers.add(mMap!!.addMarker(markerOptions))
        }

        var fragment = supportFragmentManager.findFragmentById(R.id.fragment_view_navigation) as NavigationViewFragment
        fragment.onRemarkSelectedListener = this
        fragment.setRemarks(remarks)
    }

    override fun onRemarkSelected(remark: Remark) {
        drawerLayout.closeDrawer(Gravity.RIGHT)
        var latLng = LatLng(remark.location.coordinates[1], remark.location.coordinates[0]);
        mMap?.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap?.animateCamera(CameraUpdateFactory.zoomTo(13.0f));
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true;
    }
}
