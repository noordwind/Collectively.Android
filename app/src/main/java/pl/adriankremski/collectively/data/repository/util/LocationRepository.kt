package pl.adriankremski.collectively.data.repository.util

import android.location.Address
import io.reactivex.Observable

interface LocationRepository {
    fun lastKnownAddress() : Observable<List<Address>>
}


