package com.noordwind.apps.collectively.presentation.statistics

import com.noordwind.apps.collectively.data.repository.UsersRepository
import com.noordwind.apps.collectively.domain.interactor.UseCase
import com.noordwind.apps.collectively.domain.thread.PostExecutionThread
import com.noordwind.apps.collectively.domain.thread.UseCaseThread
import com.noordwind.apps.collectively.presentation.extension.asObservable
import io.reactivex.Observable

class LoadUserPictureUrlUseCase(val usersRepository: UsersRepository,
                            useCaseThread: UseCaseThread,
                            postExecutionThread: PostExecutionThread) : UseCase<String, String>(useCaseThread, postExecutionThread) {

    override fun buildUseCaseObservable(params: String?): Observable<String> = usersRepository.loadUser(params!!).flatMap { it.avatarUrl.asObservable() }
}

