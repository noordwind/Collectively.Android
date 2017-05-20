package pl.adriankremski.collectively.domain.interactor.profile

import io.reactivex.Observable
import pl.adriankremski.collectively.data.repository.ProfileRepository
import pl.adriankremski.collectively.domain.interactor.UseCase
import pl.adriankremski.collectively.domain.thread.PostExecutionThread
import pl.adriankremski.collectively.domain.thread.UseCaseThread

class LoadUserIdUseCase(val profileRepository: ProfileRepository,
                        useCaseThread: UseCaseThread,
                        postExecutionThread: PostExecutionThread) : UseCase<String, Void>(useCaseThread, postExecutionThread) {
    override fun buildUseCaseObservable(params: Void?): Observable<String> = profileRepository.loadProfile(false).flatMap { Observable.just(it.userId) }
}

