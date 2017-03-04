package pl.adriankremski.collectively.usecases

import android.location.Address
import io.reactivex.Observable
import pl.adriankremski.collectively.repository.LocationRepository

class LoadLastKnownLocationUseCase(val locationRepository: LocationRepository) {
    fun lastKnownAddress() : Observable<List<Address>> = locationRepository.lastKnownAddress()
}