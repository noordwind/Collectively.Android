package com.noordwind.apps.collectively.usecases

import android.location.Address
import com.google.android.gms.maps.model.LatLng
import com.noordwind.apps.collectively.domain.repository.LocationRepository
import com.noordwind.apps.collectively.domain.interactor.UseCase
import com.noordwind.apps.collectively.domain.thread.PostExecutionThread
import com.noordwind.apps.collectively.domain.thread.UseCaseThread
import io.reactivex.Observable

class LoadAddressFromLocationUseCase(val locationRepository: LocationRepository,
                                     useCaseThread: UseCaseThread,
                                     postExecutionThread: PostExecutionThread) : UseCase<List<Address>, LatLng>(useCaseThread, postExecutionThread) {

    override fun buildUseCaseObservable(latLng: LatLng?): Observable<List<Address>> = locationRepository.addressFromLatLng(latLng!!)
}