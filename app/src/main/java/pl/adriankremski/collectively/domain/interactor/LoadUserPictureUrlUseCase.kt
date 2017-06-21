package pl.adriankremski.collectively.presentation.statistics

import io.reactivex.Observable
import pl.adriankremski.collectively.data.repository.UsersRepository
import pl.adriankremski.collectively.domain.interactor.UseCase
import pl.adriankremski.collectively.domain.thread.PostExecutionThread
import pl.adriankremski.collectively.domain.thread.UseCaseThread
import pl.adriankremski.collectively.presentation.extension.asObservable

class LoadUserPictureUrlUseCase(val usersRepository: UsersRepository,
                            useCaseThread: UseCaseThread,
                            postExecutionThread: PostExecutionThread) : UseCase<String, String>(useCaseThread, postExecutionThread) {

    override fun buildUseCaseObservable(params: String?): Observable<String> = usersRepository.loadUser(params!!).flatMap { it.avatarUrl.asObservable() }
}

