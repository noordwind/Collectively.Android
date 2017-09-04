package com.noordwind.apps.collectively.presentation.rxjava

import com.google.android.gms.maps.GoogleMap
import io.reactivex.Observable
import io.reactivex.functions.Function


class CameraIdleFunc : Function<GoogleMap, Observable<Any>> {

    override fun apply(googleMap: GoogleMap): Observable<Any> {
        return Observable.create({ emitter ->
            googleMap.setOnCameraIdleListener { emitter.onNext(Any()) }
        })
    }
}
