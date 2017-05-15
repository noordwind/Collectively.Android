package pl.adriankremski.collectively.domain.interactor

import io.reactivex.Observable
import pl.adriankremski.collectively.data.model.Optional
import pl.adriankremski.collectively.data.repository.FacebookTokenRepository
import pl.adriankremski.collectively.domain.thread.PostExecutionThread
import pl.adriankremski.collectively.domain.thread.UseCaseThread

class GetFacebookTokenUseCase(val facebookTokenRepository: FacebookTokenRepository,
                        useCaseThread: UseCaseThread,
                        postExecutionThread: PostExecutionThread) : UseCase<Optional<String>, Void>(useCaseThread, postExecutionThread) {
    override fun buildUseCaseObservable(params: Void?): Observable<Optional<String>>  = facebookTokenRepository.facebookToken
}

