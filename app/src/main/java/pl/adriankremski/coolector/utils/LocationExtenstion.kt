package pl.adriankremski.coolector.utils

import android.location.Address
import pl.charmas.android.reactivelocation.ReactiveLocationProvider
import rx.Observable

fun ReactiveLocationProvider.lastKnownAddressObservable(): Observable<List<Address>> {
    return lastKnownLocation.flatMap({
        location -> getReverseGeocodeObservable(location.latitude, location.longitude, 1)
    })
}
