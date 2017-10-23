package com.noordwind.apps.collectively.domain.repository

import android.location.Address
import com.google.android.gms.maps.model.LatLng
import io.reactivex.Observable

interface LocationRepository {
    fun lastKnownAddress() : Observable<List<Address>>
    fun addressFromLatLng(latLng: LatLng): Observable<List<Address>>
}


