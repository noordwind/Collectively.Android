package pl.adriankremski.collectively.domain.interactor.authentication

import io.reactivex.Observable
import pl.adriankremski.collectively.data.repository.SessionRepository
import pl.adriankremski.collectively.data.repository.AuthenticationRepository
import pl.adriankremski.collectively.domain.interactor.UseCase
import pl.adriankremski.collectively.domain.model.SignUpCredentials
import pl.adriankremski.collectively.domain.thread.PostExecutionThread
import pl.adriankremski.collectively.domain.thread.UseCaseThread

class SignUpUseCase(val authenticationRepository: AuthenticationRepository,
                    val sessionRepository: SessionRepository,
                    useCaseThread: UseCaseThread, postExecutionThread: PostExecutionThread) : UseCase<String, SignUpCredentials>(useCaseThread, postExecutionThread) {

    override fun buildUseCaseObservable(credentials: SignUpCredentials?): Observable<String> {
        return authenticationRepository.signUp(credentials!!.name, credentials.email, credentials.password)
    }
}

