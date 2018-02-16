package pl.adriankremski.collectively.presentation.main

import android.Manifest
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBarDrawerToggle
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
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.activity_main.*
import pl.adriankremski.collectively.R
import pl.adriankremski.collectively.TheApp
import pl.adriankremski.collectively.data.datasource.FiltersRepository
import pl.adriankremski.collectively.data.model.Remark
import pl.adriankremski.collectively.data.model.RemarkCategory
import pl.adriankremski.collectively.data.repository.RemarksRepository
import pl.adriankremski.collectively.domain.interactor.remark.LoadRemarkCategoriesUseCase
import pl.adriankremski.collectively.domain.interactor.remark.LoadRemarksUseCase
import pl.adriankremski.collectively.domain.interactor.remark.filters.LoadMapFiltersUseCase
import pl.adriankremski.collectively.domain.thread.PostExecutionThread
import pl.adriankremski.collectively.domain.thread.UseCaseThread
import pl.adriankremski.collectively.presentation.BaseActivity
import pl.adriankremski.collectively.presentation.adapter.delegates.MainScreenRemarksAdapterDelegate
import pl.adriankremski.collectively.presentation.addremark.AddRemarkActivity
import pl.adriankremski.collectively.presentation.extension.colorOfCategory
import pl.adriankremski.collectively.presentation.extension.iconOfCategory
import pl.adriankremski.collectively.presentation.extension.markerBitmapOfCategory
import pl.adriankremski.collectively.presentation.extension.uppercaseFirstLetter
import pl.adriankremski.collectively.presentation.views.MainScreenRemarkBottomSheetDialog
import pl.adriankremski.collectively.presentation.views.dialogs.mapfilters.MapFiltersDialog
import pl.adriankremski.collectively.presentation.views.toast.ToastManager
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
    private val MY_PERMISSIONS_REQUEST_LOCATION = 99
    private val remarksMarkers: LinkedList<Marker> = LinkedList<Marker>()
    private var remarks: List<Remark>? = null

    @Inject
    lateinit var remarksRepository: RemarksRepository

    @Inject
    lateinit var filtersRepository: FiltersRepository

    @Inject
    lateinit var ioThread: UseCaseThread

    @Inject
    lateinit var uiThread: PostExecutionThread

    lateinit var mainPresenter: MainPresenter

    private lateinit var drawerToggle: ActionBarDrawerToggle

    private lateinit var drawerLayout: DrawerLayout

    private var toast: ToastManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        TheApp[this].appComponent?.inject(this)
        setContentView(R.layout.activity_main)

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

        mainPresenter = MainPresenter(this, LoadRemarksUseCase(remarksRepository, ioThread, uiThread),
                LoadRemarkCategoriesUseCase(remarksRepository, ioThread, uiThread),
                LoadMapFiltersUseCase(filtersRepository, ioThread, uiThread))
        mainPresenter.loadRemarkCategories()

        filtersButton.setOnClickListener {
            mainPresenter.loadMapFiltersDialog()
        }
    }

    override fun showMapFiltersDialog() {
        var dialog = MapFiltersDialog.newInstance()
        dialog.show(supportFragmentManager, MapFiltersDialog.javaClass.toString())
        dialog.setOnDismissListener(DialogInterface.OnDismissListener {
            dialog.dismiss()
            mainPresenter.checkIfFiltersHasChanged()
        })
    }

    override fun showRemarksReloadingProgress() {
        toast = ToastManager(this, getString(R.string.updating_remarks), Toast.LENGTH_LONG).progress().show()
    }

    override fun clearCategories() {
//        floatingActionsMenu.removeAllViews()
    }

    override fun showRemarkCategory(remarkCategory: RemarkCategory) {
        var remarkButton = FloatingActionButton(baseContext);
        remarkButton.colorNormal = Color.parseColor(remarkCategory.name.colorOfCategory())
        remarkButton.title = remarkCategory.name.uppercaseFirstLetter()
        remarkButton.setIcon(remarkCategory.name.iconOfCategory())
        remarkButton.setOnClickListener { runOnUiThread { AddRemarkActivity.start(baseContext, remarkCategory.name.toLowerCase()) } }
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


        //Place current location marker
        var latLng = LatLng(location?.latitude!!, location?.longitude!!);

        //move map camera
        map?.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        map?.animateCamera(CameraUpdateFactory.zoomTo(14.0f));

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
        toast?.let {
            it.hide()
            toast = null
            ToastManager(this, getString(R.string.remarks_updated), Toast.LENGTH_LONG).success().show()
        }

        var fragment = supportFragmentManager.findFragmentById(R.id.fragment_view_navigation) as NavigationViewFragment
        fragment.onRemarkSelectedListener = this
        fragment.setRemarks(remarks)

        if (map == null) {
            return
        }

        this.remarks = remarks
        remarksMarkers.forEach(Marker::remove)
        remarks.forEach {
            if (it.location != null) {
                var latLng = LatLng(it.location.coordinates[1], it.location.coordinates[0]);
                var markerOptions = MarkerOptions();
                markerOptions.position(latLng);
                markerOptions.snippet(it.id)
                markerOptions.title(it.description);
                markerOptions.icon(it.category?.name?.markerBitmapOfCategory());
                remarksMarkers.add(map!!.addMarker(markerOptions))
            }
        }
    }

    override fun onMarkerClick(marker: Marker?): Boolean {
        remarks?.filter { it.id != null && it.id.equals(marker?.snippet) }?.forEach {
            //Place current location marker
            var latLng = LatLng(it.location!!.coordinates[1], it.location!!.coordinates[0]);

            //move map camera
            map?.moveCamera(CameraUpdateFactory.newLatLng(latLng));
            map?.animateCamera(CameraUpdateFactory.zoomTo(14.0f));
            MainScreenRemarkBottomSheetDialog(this, it).show()
        }
        return true
    }

    override fun onRemarkSelected(remark: Remark) {
        drawerLayout.closeDrawer(Gravity.RIGHT)

        if (remark.location != null) {
            var latLng = LatLng(remark.location.coordinates[1], remark.location.coordinates[0]);
            map?.moveCamera(CameraUpdateFactory.newLatLng(latLng));
            map?.animateCamera(CameraUpdateFactory.zoomTo(20.0f));
            MainScreenRemarkBottomSheetDialog(this, remark).show()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true;
    }

    override fun onStop() {
        super.onStop()
        mainPresenter.destroy()
    }
}
