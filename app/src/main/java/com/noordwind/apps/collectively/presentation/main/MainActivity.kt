package com.noordwind.apps.collectively.presentation.main

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
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.view.Gravity
import android.view.View
import android.widget.Toast
import com.getbase.floatingactionbutton.FloatingActionButton
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
import com.google.maps.android.clustering.ClusterManager
import com.noordwind.apps.collectively.Constants
import com.noordwind.apps.collectively.R
import com.noordwind.apps.collectively.TheApp
import com.noordwind.apps.collectively.data.datasource.FiltersTranslationsDataSource
import com.noordwind.apps.collectively.data.datasource.MapFiltersRepository
import com.noordwind.apps.collectively.data.model.Remark
import com.noordwind.apps.collectively.data.model.RemarkCategory
import com.noordwind.apps.collectively.data.repository.RemarksRepository
import com.noordwind.apps.collectively.data.repository.UserGroupsRepository
import com.noordwind.apps.collectively.data.repository.util.LocationRepository
import com.noordwind.apps.collectively.domain.interactor.remark.LoadRemarkCategoriesUseCase
import com.noordwind.apps.collectively.domain.interactor.remark.LoadRemarksUseCase
import com.noordwind.apps.collectively.domain.interactor.remark.filters.map.LoadMapFiltersUseCase
import com.noordwind.apps.collectively.domain.thread.PostExecutionThread
import com.noordwind.apps.collectively.domain.thread.UseCaseThread
import com.noordwind.apps.collectively.presentation.adapter.MainScreenInfoWindowAdapter
import com.noordwind.apps.collectively.presentation.adapter.MainScreenRemarksListAdapter
import com.noordwind.apps.collectively.presentation.addremark.AddRemarkActivity
import com.noordwind.apps.collectively.presentation.extension.colorOfCategory
import com.noordwind.apps.collectively.presentation.extension.iconOfCategory
import com.noordwind.apps.collectively.presentation.extension.uppercaseFirstLetter
import com.noordwind.apps.collectively.presentation.extension.visibleRadius
import com.noordwind.apps.collectively.presentation.rxjava.CameraIdleFunc
import com.noordwind.apps.collectively.presentation.util.RemarkClusterRenderer
import com.noordwind.apps.collectively.presentation.views.MainScreenRemarkBottomSheetDialog
import com.noordwind.apps.collectively.presentation.views.dialogs.mapfilters.MapFiltersDialog
import com.noordwind.apps.collectively.presentation.views.toast.ToastManager
import com.noordwind.apps.collectively.usecases.LoadAddressFromLocationUseCase
import io.reactivex.Observable
import io.reactivex.ObservableOnSubscribe
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.subjects.BehaviorSubject
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject


class MainActivity : com.noordwind.apps.collectively.presentation.BaseActivity(), MainMvp.View, OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener, MainScreenRemarksListAdapter.OnRemarkSelectedListener, GoogleMap.OnMarkerClickListener, GoogleMap.OnMapLongClickListener {
    companion object {
        fun login(context: Context) {
            val intent = Intent(context, MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            context.startActivity(intent)
        }
    }

    private var map: GoogleMap? = null
    private var googleApiClient: GoogleApiClient? = null
    private var locationRequest: LocationRequest? = null
    private var lastLocation: Location? = null
    private val MY_PERMISSIONS_REQUEST_LOCATION = 99
    private var initialLocationChanged = true

    private var disposable: Disposable? = null

    private var reloadRemarksList: Boolean = false

    @Inject
    lateinit var remarksRepository: RemarksRepository

    @Inject
    lateinit var mapFiltersRepository: MapFiltersRepository

    @Inject
    lateinit var translationDataSource: FiltersTranslationsDataSource

    @Inject
    lateinit var userGroupsRepository: UserGroupsRepository

    @Inject
    lateinit var locationRepository: LocationRepository

    @Inject
    lateinit var ioThread: UseCaseThread

    @Inject
    lateinit var uiThread: PostExecutionThread

    lateinit var mainPresenter: MainPresenter

    private var toast: ToastManager? = null

    private val mapSubject: BehaviorSubject<GoogleMap> = BehaviorSubject.create()

    private var bottomDialog: MainScreenRemarkBottomSheetDialog? = null

    private var userMarker: Marker? = null
    private var userMarkerLocation: LatLng? = null
    private var mainScreenInfoWindowAdapter: MainScreenInfoWindowAdapter? = null
//    private val remarkMarkers = LinkedList<Marker>()

    private val USER_MARKER_SNIPPET = "user_marker"

    private lateinit var remarksClusterManager : ClusterManager<Remark>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        TheApp[this].appComponent?.inject(this)
        setContentView(R.layout.activity_main)
        mainMenu.setOnClickListener { drawerLayout.openDrawer(Gravity.LEFT) }

        drawerLayout.addDrawerListener(object : DrawerLayout.DrawerListener {
            override fun onDrawerStateChanged(newState: Int) {
            }

            override fun onDrawerSlide(drawerView: View?, slideOffset: Float) {
            }

            override fun onDrawerClosed(drawerView: View?) {
            }

            override fun onDrawerOpened(drawerView: View?) {
                if (drawerView?.id == R.id.right_navigation_view && reloadRemarksList) {
                    var remarks = mainPresenter.getCurrentlyVisibleRemarks()
                    remarks?.let {
                        var fragment = supportFragmentManager.findFragmentById(R.id.fragment_view_navigation) as NavigationViewFragment
                        fragment.onRemarkSelectedListener = this@MainActivity
                        fragment.setRemarks(remarks)
                        reloadRemarksList = false
                    }
                }
            }

        })

        setupUpMapFragment()

        mainPresenter = MainPresenter(this, LoadRemarksUseCase(remarksRepository, ioThread, uiThread),
                LoadRemarkCategoriesUseCase(remarksRepository, translationDataSource, ioThread, uiThread),
                LoadMapFiltersUseCase(mapFiltersRepository, userGroupsRepository, ioThread, uiThread),
                LoadAddressFromLocationUseCase(locationRepository, ioThread, uiThread),
                translationDataSource)

        mainPresenter.onCreate()
        filtersButton.setOnClickListener { mainPresenter.loadMapFiltersDialog() }
    }

    private fun setupUpMapFragment() {
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        Observable.create(ObservableOnSubscribe<GoogleMap> { emitter ->
            var mapReadyCallback = OnMapReadyCallback {
                emitter.onNext(it)
            }
            mapFragment.getMapAsync(mapReadyCallback)
        }).subscribe(mapSubject)
    }

    override fun showTooltip() {
        tooltipBackground.visibility = View.VISIBLE
        tooltipBackground.setOnClickListener {
            tooltipBackground.visibility = View.GONE
            mainPresenter.onTooltipShown()
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

    override fun showRemarkCategory(remarkCategory: RemarkCategory) {
        var remarkButton = FloatingActionButton(baseContext);
        remarkButton.colorNormal = Color.parseColor(remarkCategory.name.colorOfCategory())
        remarkButton.title = mainPresenter.remarkCategoryTranslation(remarkCategory.name).uppercaseFirstLetter()
        remarkButton.setIcon(remarkCategory.name.iconOfCategory())
        remarkButton.setOnClickListener { addRemark(remarkCategory.name.toLowerCase()) }
        floatingMenu.addButton(remarkButton)
    }

    fun addRemark(remarkCategory: String) {
        if (lastLocation != null) {
            var latitude = lastLocation!!.latitude
            var longitude = lastLocation!!.longitude
            runOnUiThread { AddRemarkActivity.start(this, remarkCategory, LatLng(latitude, longitude)) }
        } else {
            runOnUiThread { AddRemarkActivity.start(this, remarkCategory) }
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        map?.mapType = GoogleMap.MAP_TYPE_NORMAL
        map?.setOnMarkerClickListener(this)
        map?.setOnMapLongClickListener(this)
        mainScreenInfoWindowAdapter = MainScreenInfoWindowAdapter(baseContext)
        map?.setMinZoomPreference(10f)
        map?.setInfoWindowAdapter(mainScreenInfoWindowAdapter)
        map?.setOnInfoWindowClickListener { AddRemarkActivity.start(MainActivity@this, Constants.RemarkCategories.PRAISE, userMarkerLocation!!) }

        remarksClusterManager = ClusterManager<Remark>(this, googleMap)
        remarksClusterManager.renderer = RemarkClusterRenderer(this, googleMap, remarksClusterManager)
//        googleMap.setOnCameraIdleListener(remarksClusterManager);
//        googleMap.setOnMarkerClickListener(remarksClusterManager);
//        googleMap.setOnInfoWindowClickListener(remarksClusterManager);

        mapSubject.flatMap(CameraIdleFunc())
                .throttleWithTimeout(500, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    remarksClusterManager.onCameraIdle()
                    var centerOfMap = centerOfMap()!!
                    var radiusOfMap = radiusOfMap()!!
                    mainPresenter.loadRemarks(centerOfMap, radiusOfMap)
                })

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

    override fun onMapLongClick(latLng: LatLng?) {
        userMarker?.let {
            userMarker?.remove()
        }

        latLng?.let {
            var markerOptions = MarkerOptions();
            markerOptions.position(latLng);
            markerOptions.snippet(USER_MARKER_SNIPPET)
            userMarker = map?.addMarker(markerOptions)
            userMarkerLocation = latLng
            mainScreenInfoWindowAdapter?.markerAddress = null
            userMarker?.showInfoWindow()
            mainPresenter.fetchAddressForInfoWindow(latLng)
        }
    }

    override fun updateInfoWindow(address: String) {
        userMarker?.let {
            userMarker!!.hideInfoWindow()
            userMarker?.remove()

            var markerOptions = MarkerOptions();
            markerOptions.position(userMarkerLocation!!);
            markerOptions.snippet(USER_MARKER_SNIPPET)
            userMarker = map?.addMarker(markerOptions)
            mainScreenInfoWindowAdapter?.markerAddress = address
            userMarker?.showInfoWindow()
        }
    }

    override fun centerOfMap(): LatLng? = map?.cameraPosition?.target

    override fun radiusOfMap(): Int? = map?.projection?.visibleRegion?.visibleRadius()

    fun buildGoogleApiClient() {
        googleApiClient = GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build()
        googleApiClient?.connect()
    }

    override fun onStart() {
        super.onStart()
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkLocationPermission()
        }

        if (floatingMenu.childCount == 1) {
            mainPresenter.loadRemarkCategories()
        }
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

        mainPresenter.lastLocation = lastLocation

        if (initialLocationChanged) {
            //Place current location marker
            var latLng = LatLng(location?.latitude!!, location?.longitude!!);

            //move map camera
            var cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 14.0f);
            map?.animateCamera(cameraUpdate);
            initialLocationChanged = false
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

    override fun clearMap() {
        map?.let { map!!.clear() }
        remarksClusterManager.clearItems()
    }


    override fun onMarkerClick(marker: Marker?): Boolean {
        if (marker?.snippet.equals(USER_MARKER_SNIPPET)) {
        } else {
            mainPresenter.getRemarks()?.filter { marker?.snippet.equals(it.id) }?.forEach {
                var latitude = it.location!!.coordinates[1]
                var longitude = it.location!!.coordinates[0]

                var remarkLocation = Location("")
                remarkLocation.latitude = latitude
                remarkLocation.longitude = longitude

                map?.moveCamera(CameraUpdateFactory.newLatLng(LatLng(latitude, longitude)))
                map?.animateCamera(CameraUpdateFactory.zoomTo(20.0f))
                bottomDialog = MainScreenRemarkBottomSheetDialog(this, it, lastLocation, remarkLocation).show()
            }
        }
        return true
    }

    override fun refreshOldRemarks(oldRemarksToRefresh: LinkedList<Remark>) {
        oldRemarksToRefresh.forEach {
            remarksClusterManager.removeItem(it)
        }

        oldRemarksToRefresh.forEach { addRemarkToMap(it) }
        remarksClusterManager.cluster()
    }

    override fun showNewRemarks(remarks: List<Remark>) {
        reloadRemarksList = true
        if (map == null) {
            return
        }

        toast?.let {
            it.hide()
            toast = null
            ToastManager(this, getString(R.string.remarks_updated), Toast.LENGTH_LONG).success().show()
        }

        remarks.forEach { addRemarkToMap(it) }
        remarksClusterManager.cluster()
    }

    private fun addRemarkToMap(remark: Remark) {
//        var newMarkerOptions = markerFromRemark(remark)
        remarksClusterManager.addItem(remark)
//        remarkMarkers.add(map!!.addMarker(newMarkerOptions))
    }

//    private fun markerFromRemark(remark: Remark): MarkerOptions {
//        var latLng = LatLng(remark.location!!.coordinates[1], remark.location!!.coordinates[0]);
//        var markerOptions = MarkerOptions();
//        markerOptions.position(latLng);
//        markerOptions.snippet(remark.id)
//        markerOptions.title(remark.description);
//        markerOptions.icon(remarkMarkerBitmapProvider.remarkMapMarker(remark));
//        return markerOptions
//    }

    override fun onRemarkItemSelected(remark: Remark) {
        drawerLayout.closeDrawer(Gravity.RIGHT)

        if (remark.location != null) {
            var latitude = remark.location!!.coordinates[1]
            var longitude = remark.location!!.coordinates[0]

            var remarkLocation = Location("")
            remarkLocation.latitude = latitude
            remarkLocation.longitude = longitude

            map?.moveCamera(CameraUpdateFactory.newLatLng(LatLng(latitude, longitude)));
            map?.animateCamera(CameraUpdateFactory.zoomTo(20.0f));
            bottomDialog = MainScreenRemarkBottomSheetDialog(this, remark, lastLocation, remarkLocation).show()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == RESULT_OK) {
            if (requestCode == Constants.RequestCodes.ADD_REMARK) {
                var remarkLocation = data!!.extras[Constants.BundleKey.LOCATION] as LatLng
                map?.moveCamera(CameraUpdateFactory.newLatLng(LatLng(remarkLocation.latitude, remarkLocation.longitude)));
                map?.animateCamera(CameraUpdateFactory.zoomTo(20.0f));
                userMarker?.let { userMarker?.remove() }
                userMarkerLocation = null
            }
        }
    }

    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START) || drawerLayout.isDrawerOpen(GravityCompat.END)) {
            drawerLayout.closeDrawers()
        } else if (floatingMenu.isExpanded) {
            floatingMenu.collapse()
        } else if (bottomDialog != null && bottomDialog!!.isVisible()) {
            bottomDialog!!.hide()
        } else {
            super.onBackPressed()
        }
    }

    override fun onStop() {
        super.onStop()
        mainPresenter.destroy()
    }

    override fun onDestroy() {
        super.onDestroy()
        disposable?.let {
            disposable?.dispose()
        }
        disposable = null
    }
}
