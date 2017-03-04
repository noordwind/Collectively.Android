package pl.adriankremski.collectively.main

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
import kotlinx.android.synthetic.main.activity_main.*
import pl.adriankremski.collectively.BaseActivity
import pl.adriankremski.collectively.R
import pl.adriankremski.collectively.TheApp
import pl.adriankremski.collectively.adapter.delegates.MainScreenRemarksAdapterDelegate
import pl.adriankremski.collectively.addremark.AddRemarkActivity
import pl.adriankremski.collectively.model.Remark
import pl.adriankremski.collectively.model.RemarkCategory
import pl.adriankremski.collectively.repository.RemarksRepository
import pl.adriankremski.collectively.usecases.LoadRemarkCategoriesUseCase
import pl.adriankremski.collectively.usecases.LoadRemarksUseCase
import pl.adriankremski.collectively.utils.colorOfCategory
import pl.adriankremski.collectively.utils.toBitmapDescriptor
import pl.adriankremski.collectively.utils.uppercaseFirstLetter
import pl.adriankremski.collectively.views.MainScreenRemarkBottomSheetDialog
import java.util.*
import javax.inject.Inject


class MainActivity : BaseActivity(), MainMvp.View, OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener, MainScreenRemarksAdapterDelegate.OnRemarkSelectedListener, GoogleMap.OnMarkerClickListener {
    companion object {
        fun login(context: Context) {
            val intent = Intent(context, MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            context.startActivity(intent)
        }
    }

    private lateinit var floatingActionsMenu: FloatingActionsMenu
    private var map: GoogleMap? = null
    private var googleApiClient: GoogleApiClient? = null
    private var locationRequest: LocationRequest? = null
    private var lastLocation: Location? = null
    private var currLocationMarker: Marker? = null
    private val MY_PERMISSIONS_REQUEST_LOCATION = 99
    private val remarksMarkers: LinkedList<Marker> = LinkedList<Marker>()
    private var remarks: List<Remark>? = null

    @Inject
    lateinit var remarksRepository: RemarksRepository
    lateinit var mainPresenter: MainPresenter

    private lateinit var drawerToggle: ActionBarDrawerToggle

    private lateinit var drawerLayout: DrawerLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        TheApp[this].appComponent?.inject(this)
        setContentView(R.layout.activity_main)

        val span = SpannableString(getString(R.string.main_screen_title))
        span.setSpan(RelativeSizeSpan(1.2f), 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        span.setSpan(StyleSpan(Typeface.BOLD), 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)

//        mToolbarOptionLabel = findViewById(R.id.option) as TextView
//        mToolbarOptionLabel.text = getString(R.string.list)
//        mToolbarOptionLabel.visibility = View.GONE
//        mToolbarOptionLabel.setOnClickListener { drawerLayout.openDrawer(Gravity.RIGHT) }

        mainMenu.setOnClickListener { drawerLayout.openDrawer(Gravity.LEFT) }

        drawerLayout = findViewById(R.id.drawer_layout) as DrawerLayout
        drawerToggle = ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close)
        drawerLayout.setDrawerListener(drawerToggle)
        drawerToggle.syncState()

        floatingActionsMenu = findViewById(R.id.actions) as FloatingActionsMenu

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkLocationPermission();
        }

        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        mainPresenter = MainPresenter(this, LoadRemarksUseCase(remarksRepository), LoadRemarkCategoriesUseCase(remarksRepository))
        mainPresenter.loadRemarkCategories()
    }

    override fun clearCategories() {
//        floatingActionsMenu.removeAllViews()
    }

    override fun showRemarkCategory(remarkCategory: RemarkCategory) {
        var remarkButton = FloatingActionButton(baseContext);
        remarkButton.colorNormal = Color.parseColor(remarkCategory.name.colorOfCategory())
        remarkButton.title = remarkCategory.name.uppercaseFirstLetter()
        remarkButton.setOnClickListener { runOnUiThread { AddRemarkActivity.start(baseContext) } }
        floatingActionsMenu.addButton(remarkButton)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mainPresenter.loadRemarks()
        map = googleMap
        map?.mapType = GoogleMap.MAP_TYPE_NORMAL
        map?.setOnMarkerClickListener(this)

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                buildGoogleApiClient();
                map?.isMyLocationEnabled = true;
            }
        } else {
            buildGoogleApiClient();
            map?.isMyLocationEnabled = true;
        }
    }

    override fun onStart() {
        super.onStart()
        if (map != null) {
            mainPresenter.loadRemarks()
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
        lastLocation = location;

        if (currLocationMarker != null) {
            currLocationMarker?.remove();
        }

        //Place current location marker
        var latLng = LatLng(location?.latitude!!, location?.longitude!!);

        var markerOptions = MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title("Current Position");
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));

        currLocationMarker = map?.addMarker(markerOptions);

        //move map camera
        map?.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        map?.animateCamera(CameraUpdateFactory.zoomTo(12.0f));

        //stop location updates
        if (googleApiClient != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(googleApiClient, this);
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

    override fun showRemarks(remarks: List<Remark>) {
        this.remarks = remarks
        remarksMarkers.forEach(Marker::remove)
        remarks.forEach {
            if (it.location != null) {
                var latLng = LatLng(it.location.coordinates[1], it.location.coordinates[0]);
                var markerOptions = MarkerOptions();
                markerOptions.position(latLng);
                markerOptions.title(it.description);
                markerOptions.icon(it.category.colorOfCategory().toBitmapDescriptor());
                remarksMarkers.add(map!!.addMarker(markerOptions))
            }
        }

        var fragment = supportFragmentManager.findFragmentById(R.id.fragment_view_navigation) as NavigationViewFragment
        fragment.onRemarkSelectedListener = this
        fragment.setRemarks(remarks)
    }

    override fun onMarkerClick(marker: Marker?): Boolean {
        remarks?.filter { it.description != null && it.description.equals(marker?.title) }?.forEach { MainScreenRemarkBottomSheetDialog(this, it).show() }
        return true
    }

    override fun onRemarkSelected(remark: Remark) {
        drawerLayout.closeDrawer(Gravity.RIGHT)

        if (remark.location != null) {
            var latLng = LatLng(remark.location.coordinates[1], remark.location.coordinates[0]);
            map?.moveCamera(CameraUpdateFactory.newLatLng(latLng));
            map?.animateCamera(CameraUpdateFactory.zoomTo(16.0f));
            MainScreenRemarkBottomSheetDialog(this, remark).show()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true;
    }
}
