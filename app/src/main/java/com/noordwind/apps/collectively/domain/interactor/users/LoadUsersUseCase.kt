package com.noordwind.apps.collectively.presentation.statistics

import com.noordwind.apps.collectively.data.model.User
import com.noordwind.apps.collectively.data.repository.UsersRepository
import com.noordwind.apps.collectively.domain.interactor.UseCase
import com.noordwind.apps.collectively.domain.thread.PostExecutionThread
import com.noordwind.apps.collectively.domain.thread.UseCaseThread
import io.reactivex.Observable

class LoadUsersUseCase(val usersRepository: UsersRepository,
                            useCaseThread: UseCaseThread,
                            postExecutionThread: PostExecutionThread) : UseCase<List<User>, Void>(useCaseThread, postExecutionThread) {

    override fun buildUseCaseObservable(params: Void?): Observable<List<User>> = usersRepository.loadUsers()
}

