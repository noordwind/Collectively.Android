package com.noordwind.apps.collectively.data.repository.util

import android.location.Address
import android.location.LocationManager
import com.google.android.gms.maps.model.LatLng
import com.noordwind.apps.collectively.presentation.extension.lastKnownAddressObservable
import hu.akarnokd.rxjava.interop.RxJavaInterop
import io.reactivex.Observable
import pl.charmas.android.reactivelocation.ReactiveLocationProvider
import java.util.*

class LocationRepositoryImpl(val locationManager: LocationManager, val reactiveLocationProvider: ReactiveLocationProvider) : LocationRepository {
    override fun lastKnownAddress(): Observable<List<Address>> {
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            return Observable.just(LinkedList<Address>())
        } else {
            return RxJavaInterop.toV2Observable(reactiveLocationProvider.lastKnownAddressObservable())
        }
    }

    override fun addressFromLatLng(latLng: LatLng): Observable<List<Address>> {
        return RxJavaInterop.toV2Observable(reactiveLocationProvider.getReverseGeocodeObservable(latLng.latitude,
                latLng.longitude, 1))
    }
}


