package com.noordwind.apps.collectively.data.repository.util

import android.location.Address
import hu.akarnokd.rxjava.interop.RxJavaInterop
import io.reactivex.Observable
import com.noordwind.apps.collectively.presentation.extension.lastKnownAddressObservable
import pl.charmas.android.reactivelocation.ReactiveLocationProvider

class LocationRepositoryImpl(val reactiveLocationProvider: ReactiveLocationProvider) : LocationRepository {
    override fun lastKnownAddress(): Observable<List<Address>> {
        return RxJavaInterop.toV2Observable(reactiveLocationProvider.lastKnownAddressObservable())
    }
}


