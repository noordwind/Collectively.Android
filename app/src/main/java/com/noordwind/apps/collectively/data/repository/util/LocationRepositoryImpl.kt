package com.noordwind.apps.collectively.data.repository.util

import android.location.Address
import com.google.android.gms.maps.model.LatLng
import com.noordwind.apps.collectively.presentation.extension.lastKnownAddressObservable
import hu.akarnokd.rxjava.interop.RxJavaInterop
import io.reactivex.Observable
import pl.charmas.android.reactivelocation.ReactiveLocationProvider

class LocationRepositoryImpl(val reactiveLocationProvider: ReactiveLocationProvider) : LocationRepository {
    override fun lastKnownAddress(): Observable<List<Address>> {
        return RxJavaInterop.toV2Observable(reactiveLocationProvider.lastKnownAddressObservable())
    }

    override fun addressFromLatLng(latLng: LatLng): Observable<List<Address>> {
        return RxJavaInterop.toV2Observable(reactiveLocationProvider.getReverseGeocodeObservable(latLng.latitude,
                latLng.longitude, 1))
    }
}


