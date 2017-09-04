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
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBarDrawerToggle
import android.view.Gravity
import android.view.Menu
import android.view.View
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
import com.google.android.gms.maps.model.VisibleRegion
import com.noordwind.apps.collectively.Constants
import com.noordwind.apps.collectively.R
import com.noordwind.apps.collectively.TheApp
import com.noordwind.apps.collectively.data.datasource.FiltersTranslationsDataSource
import com.noordwind.apps.collectively.data.datasource.MapFiltersRepository
import com.noordwind.apps.collectively.data.model.Remark
import com.noordwind.apps.collectively.data.model.RemarkCategory
import com.noordwind.apps.collectively.data.repository.RemarksRepository
import com.noordwind.apps.collectively.data.repository.UserGroupsRepository
import com.noordwind.apps.collectively.domain.interactor.remark.LoadRemarkCategoriesUseCase
import com.noordwind.apps.collectively.domain.interactor.remark.LoadRemarksUseCase
import com.noordwind.apps.collectively.domain.interactor.remark.filters.map.LoadMapFiltersUseCase
import com.noordwind.apps.collectively.domain.thread.PostExecutionThread
import com.noordwind.apps.collectively.domain.thread.UseCaseThread
import com.noordwind.apps.collectively.presentation.adapter.delegates.MainScreenRemarksAdapterDelegate
import com.noordwind.apps.collectively.presentation.addremark.AddRemarkActivity
import com.noordwind.apps.collectively.presentation.extension.colorOfCategory
import com.noordwind.apps.collectively.presentation.extension.iconOfCategory
import com.noordwind.apps.collectively.presentation.extension.uppercaseFirstLetter
import com.noordwind.apps.collectively.presentation.rxjava.CameraIdleFunc
import com.noordwind.apps.collectively.presentation.views.MainScreenRemarkBottomSheetDialog
import com.noordwind.apps.collectively.presentation.views.dialogs.mapfilters.MapFiltersDialog
import com.noordwind.apps.collectively.presentation.views.toast.ToastManager
import io.reactivex.Observable
import io.reactivex.ObservableOnSubscribe
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject
import jonathanfinerty.once.Once
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject


class MainActivity : com.noordwind.apps.collectively.presentation.BaseActivity(), MainMvp.View, OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener, MainScreenRemarksAdapterDelegate.OnRemarkSelectedListener, GoogleMap.OnMarkerClickListener {
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
    private var initialLocationChanged = true

    private lateinit var remarkMarkerBitmapProvider : RemarkMarkerBitmapProvider
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
    lateinit var ioThread: UseCaseThread

    @Inject
    lateinit var uiThread: PostExecutionThread

    lateinit var mainPresenter: MainPresenter

    private lateinit var drawerToggle: ActionBarDrawerToggle

    private lateinit var drawerLayout: DrawerLayout

    private var toast: ToastManager? = null

    private val mapSubject: BehaviorSubject<GoogleMap> = BehaviorSubject.create()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        TheApp[this].appComponent?.inject(this)
        setContentView(R.layout.activity_main)

        mainMenu.setOnClickListener { drawerLayout.openDrawer(Gravity.LEFT) }

        drawerLayout = findViewById(R.id.drawer_layout) as DrawerLayout
        drawerToggle = ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close)
        drawerLayout.setDrawerListener(drawerToggle)
        drawerToggle.syncState()
        drawerLayout.addDrawerListener(object : DrawerLayout.DrawerListener {
            override fun onDrawerStateChanged(newState: Int) {
            }

            override fun onDrawerSlide(drawerView: View?, slideOffset: Float) {
            }

            override fun onDrawerClosed(drawerView: View?) {
            }

            override fun onDrawerOpened(drawerView: View?) {
                if (drawerView?.id == R.id.right_navigation_view && reloadRemarksList) {
                    var remarks = mainPresenter.getRemarks()
                    remarks?.let {
                        var fragment = supportFragmentManager.findFragmentById(R.id.fragment_view_navigation) as NavigationViewFragment
                        fragment.onRemarkSelectedListener = this@MainActivity
                        fragment.setRemarks(remarks)
                        reloadRemarksList = false
                    }
                }
            }

        })

        floatingActionsMenu = findViewById(R.id.actions) as FloatingActionsMenu

        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        Observable.create(ObservableOnSubscribe<GoogleMap> { emitter ->
            var mapReadyCallback = OnMapReadyCallback {
                emitter.onNext(it)
            }
            mapFragment.getMapAsync(mapReadyCallback)
        }).subscribe(mapSubject)

        mainPresenter = MainPresenter(this, LoadRemarksUseCase(remarksRepository, ioThread, uiThread),
                LoadRemarkCategoriesUseCase(remarksRepository, translationDataSource, ioThread, uiThread),
                LoadMapFiltersUseCase(mapFiltersRepository, userGroupsRepository, ioThread, uiThread),
                translationDataSource)

        mainPresenter.loadRemarkCategories()

        filtersButton.setOnClickListener {
            mainPresenter.loadMapFiltersDialog()
        }

        if (!Once.beenDone(Once.THIS_APP_INSTALL, Constants.OnceKey.SHOW_SWIPE_LEFT_TOOLTIP_ON_MAIN_SCREEN)) {
            tooltipBackground.visibility = View.VISIBLE
            tooltipBackground.setOnClickListener {
                tooltipBackground.visibility = View.GONE
                Once.markDone(Constants.OnceKey.SHOW_SWIPE_LEFT_TOOLTIP_ON_MAIN_SCREEN)
            }
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
        remarkButton.title = mainPresenter.remarkCategoryTranslation(remarkCategory.name).uppercaseFirstLetter()
        remarkButton.setIcon(remarkCategory.name.iconOfCategory())
        remarkButton.setOnClickListener { runOnUiThread { AddRemarkActivity.start(this, remarkCategory.name.toLowerCase()) } }
        floatingActionsMenu.addButton(remarkButton)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        map?.mapType = GoogleMap.MAP_TYPE_NORMAL
        map?.setOnMarkerClickListener(this)

        remarkMarkerBitmapProvider = RemarkMarkerBitmapProvider(baseContext)

        mapSubject.flatMap(CameraIdleFunc())
                .throttleWithTimeout(500, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                        var centerOfMap = map!!.cameraPosition.target
                        var radiusOfMap = calculateVisibleRadius(map!!.projection.visibleRegion).toInt()
                        mainPresenter.loadRemarks(centerOfMap, radiusOfMap)
                })


//        map?.setOnCameraIdleListener {
//            cameraIdleSubject.onNext(Pair<LatLng, Int>(centerOfMap, radiusOfMap))
//        }

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

    private fun calculateVisibleRadius(visibleRegion: VisibleRegion): Float {
        var distanceWidth = FloatArray(1)
        var distanceHeight = FloatArray(1)

        var farRight = visibleRegion.farRight;
        var farLeft = visibleRegion.farLeft;
        var nearRight = visibleRegion.nearRight;
        var nearLeft = visibleRegion.nearLeft;

        //calculate the distance width (left <-> right of map on screen)
        Location.distanceBetween(
                (farLeft.latitude + nearLeft.latitude) / 2,
                farLeft.longitude,
                (farRight.latitude + nearRight.latitude) / 2,
                farRight.longitude,
                distanceWidth
        );

        //calculate the distance height (top <-> bottom of map on screen)
        Location.distanceBetween(
                farRight.latitude,
                (farRight.longitude + farLeft.longitude) / 2,
                nearRight.latitude,
                (nearRight.longitude + nearLeft.longitude) / 2,
                distanceHeight
        );

        //visible radius is (smaller distance) / 2:
        return if (distanceWidth[0] < distanceHeight[0]) distanceWidth[0] else distanceHeight[0];
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
        lastLocation = location;

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

    override fun showRemarks(remarks: List<Remark>) {
        reloadRemarksList = true

        var newRemarksIds = remarks.map { it.id }

        toast?.let {
            it.hide()
            toast = null
            ToastManager(this, getString(R.string.remarks_updated), Toast.LENGTH_LONG).success().show()
        }

        if (map == null) {
            return
        }

        this.remarks = remarks
        remarksMarkers.forEach {
            if (!newRemarksIds.contains(it.snippet)) {
                it.remove()
            }
        }
        remarks.forEach {
            if (it.location != null) {
                var latLng = LatLng(it.location.coordinates[1], it.location.coordinates[0]);
                var markerOptions = MarkerOptions();
                markerOptions.position(latLng);
                markerOptions.snippet(it.id)
                markerOptions.title(it.description);
                markerOptions.icon(remarkMarkerBitmapProvider.remarkMapMarker(it));
                remarksMarkers.add(map!!.addMarker(markerOptions))
            }
        }
    }

    override fun onMarkerClick(marker: Marker?): Boolean {
        remarks?.filter { marker?.snippet.equals(it.id) }?.forEach {
            var latitude = it.location!!.coordinates[1]
            var longitude = it.location!!.coordinates[0]

            var remarkLocation = Location("")
            remarkLocation.latitude = latitude
            remarkLocation.longitude = longitude

            map?.moveCamera(CameraUpdateFactory.newLatLng(LatLng(latitude, longitude)))
            map?.animateCamera(CameraUpdateFactory.zoomTo(20.0f))
            MainScreenRemarkBottomSheetDialog(this, it, lastLocation, remarkLocation).show()
        }
        return true
    }

    override fun onRemarkSelected(remark: Remark) {
        drawerLayout.closeDrawer(Gravity.RIGHT)

        if (remark.location != null) {
            var latitude = remark.location!!.coordinates[1]
            var longitude = remark.location!!.coordinates[0]

            var remarkLocation = Location("")
            remarkLocation.latitude = latitude
            remarkLocation.longitude = longitude

            map?.moveCamera(CameraUpdateFactory.newLatLng(LatLng(latitude, longitude)));
            map?.animateCamera(CameraUpdateFactory.zoomTo(20.0f));
            MainScreenRemarkBottomSheetDialog(this, remark, lastLocation, remarkLocation).show()
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == RESULT_OK) {
            if (requestCode == Constants.RequestCodes.ADD_REMARK) {
                var remarkLocation = data!!.extras[Constants.BundleKey.LOCATION] as LatLng
                map?.moveCamera(CameraUpdateFactory.newLatLng(LatLng(remarkLocation.latitude, remarkLocation.longitude)));
                map?.animateCamera(CameraUpdateFactory.zoomTo(20.0f));
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        disposable?.let {
            disposable?.dispose()
        }
        disposable = null
    }
}
