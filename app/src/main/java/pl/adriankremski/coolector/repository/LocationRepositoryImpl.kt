package pl.adriankremski.coolector.repository

import android.content.Context
import android.location.Address
import hu.akarnokd.rxjava.interop.RxJavaInterop
import io.reactivex.Observable
import pl.adriankremski.coolector.utils.lastKnownAddressObservable
import pl.charmas.android.reactivelocation.ReactiveLocationProvider

class LocationRepositoryImpl(val context: Context) : LocationRepository {
    override fun lastKnownAddress(): Observable<List<Address>> {
        return RxJavaInterop.toV2Observable(ReactiveLocationProvider(context).lastKnownAddressObservable())
    }
}


