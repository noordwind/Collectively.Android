package pl.adriankremski.collectively.domain.interactor.authentication

import io.reactivex.Observable
import pl.adriankremski.collectively.domain.thread.PostExecutionThread
import pl.adriankremski.collectively.domain.thread.UseCaseThread
import pl.adriankremski.collectively.data.repository.AuthenticationRepository
import pl.adriankremski.collectively.domain.interactor.UseCase

class RetrievePasswordUseCase(val authenticationRepository: AuthenticationRepository,
                              useCaseThread: UseCaseThread,
                              postExecutionThread: PostExecutionThread) : UseCase<Boolean, String>(useCaseThread, postExecutionThread) {

    override fun buildUseCaseObservable(email: String?): Observable<Boolean> = authenticationRepository.resetPassword(email!!)
}

