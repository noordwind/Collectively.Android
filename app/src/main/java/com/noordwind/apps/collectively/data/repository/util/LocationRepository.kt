package com.noordwind.apps.collectively.data.repository.util

import android.location.Address
import io.reactivex.Observable

interface LocationRepository {
    fun lastKnownAddress() : Observable<List<Address>>
}


