package pl.adriankremski.collectively.domain.interactor.authentication

import io.reactivex.Observable
import pl.adriankremski.collectively.data.repository.AuthenticationRepository
import pl.adriankremski.collectively.domain.interactor.UseCase
import pl.adriankremski.collectively.domain.thread.PostExecutionThread
import pl.adriankremski.collectively.domain.thread.UseCaseThread

class FacebookLoginUseCase(val authenticationRepository: AuthenticationRepository,
                   useCaseThread: UseCaseThread,
                   postExecutionThread: PostExecutionThread) : UseCase<String, String>(useCaseThread, postExecutionThread) {

    override fun buildUseCaseObservable(params: String?): Observable<String> = authenticationRepository.loginWithFacebookToken(params!!)
}

