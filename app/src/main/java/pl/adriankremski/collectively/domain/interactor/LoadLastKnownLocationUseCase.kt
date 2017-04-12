package pl.adriankremski.collectively.usecases

import android.location.Address
import io.reactivex.Observable
import pl.adriankremski.collectively.domain.thread.PostExecutionThread
import pl.adriankremski.collectively.domain.thread.UseCaseThread
import pl.adriankremski.collectively.domain.interactor.UseCase
import pl.adriankremski.collectively.data.repository.util.LocationRepository

class LoadLastKnownLocationUseCase(val locationRepository: LocationRepository,
                                   useCaseThread: UseCaseThread,
                                   postExecutionThread: PostExecutionThread) : UseCase<List<Address>, Void>(useCaseThread, postExecutionThread) {

    override fun buildUseCaseObservable(params: Void?): Observable<List<Address>> = locationRepository.lastKnownAddress()
}