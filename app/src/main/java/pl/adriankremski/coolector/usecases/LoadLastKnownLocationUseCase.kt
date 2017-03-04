package pl.adriankremski.coolector.usecases

import android.location.Address
import io.reactivex.Observable
import pl.adriankremski.coolector.repository.LocationRepository

class LoadLastKnownLocationUseCase(val locationRepository: LocationRepository) {
    fun lastKnownAddress() : Observable<List<Address>> = locationRepository.lastKnownAddress()
}