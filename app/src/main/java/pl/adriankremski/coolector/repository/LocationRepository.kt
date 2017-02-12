package pl.adriankremski.coolector.repository

import android.location.Address
import io.reactivex.Observable


interface LocationRepository {
    fun lastKnownAddress() : Observable<List<Address>>
}


