package pl.adriankremski.collectively.domain.interactor.profile

import io.reactivex.Observable
import pl.adriankremski.collectively.data.model.Profile
import pl.adriankremski.collectively.data.repository.ProfileRepository
import pl.adriankremski.collectively.domain.interactor.UseCase
import pl.adriankremski.collectively.domain.thread.PostExecutionThread
import pl.adriankremski.collectively.domain.thread.UseCaseThread

class LoadProfileUseCase(
        val profileRepository: ProfileRepository,
        useCaseThread: UseCaseThread,
        postExecutionThread: PostExecutionThread) : UseCase<Profile, Void>(useCaseThread, postExecutionThread) {

    override fun buildUseCaseObservable(params: Void?): Observable<Profile> = profileRepository.loadProfile(false)
}

