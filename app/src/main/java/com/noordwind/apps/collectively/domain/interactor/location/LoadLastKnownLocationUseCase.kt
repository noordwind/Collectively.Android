package com.noordwind.apps.collectively.usecases

import android.location.Address
import com.noordwind.apps.collectively.domain.interactor.UseCase
import com.noordwind.apps.collectively.domain.repository.LocationRepository
import com.noordwind.apps.collectively.domain.thread.PostExecutionThread
import com.noordwind.apps.collectively.domain.thread.UseCaseThread
import io.reactivex.Observable

class LoadLastKnownLocationUseCase(val locationRepository: LocationRepository,
                                   useCaseThread: UseCaseThread,
                                   postExecutionThread: PostExecutionThread) : UseCase<List<Address>, Void>(useCaseThread, postExecutionThread) {

    override fun buildUseCaseObservable(params: Void?): Observable<List<Address>> =
            locationRepository.lastKnownAddress()
}