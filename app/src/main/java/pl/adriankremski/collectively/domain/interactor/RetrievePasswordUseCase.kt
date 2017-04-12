package pl.adriankremski.collectively.domain.interactor

import io.reactivex.Observable
import pl.adriankremski.collectively.domain.thread.PostExecutionThread
import pl.adriankremski.collectively.domain.thread.UseCaseThread
import pl.adriankremski.collectively.data.repository.AuthenticationRepository

class RetrievePasswordUseCase(val authenticationRepository: AuthenticationRepository,
                              useCaseThread: UseCaseThread,
                              postExecutionThread: PostExecutionThread) : UseCase<Boolean, String>(useCaseThread, postExecutionThread) {

    override fun buildUseCaseObservable(email: String?): Observable<Boolean> = authenticationRepository.resetPassword(email!!)
}

