package com.noordwind.apps.collectively.presentation.statistics

import com.noordwind.apps.collectively.data.model.UserGroup
import com.noordwind.apps.collectively.data.repository.UserGroupsRepository
import com.noordwind.apps.collectively.domain.interactor.UseCase
import com.noordwind.apps.collectively.domain.thread.PostExecutionThread
import com.noordwind.apps.collectively.domain.thread.UseCaseThread
import io.reactivex.Observable

class LoadUserGroupsUseCase(val userGroupsRepository: UserGroupsRepository,
                            useCaseThread: UseCaseThread,
                            postExecutionThread: PostExecutionThread) : UseCase<List<UserGroup>, Boolean>(useCaseThread, postExecutionThread) {

    override fun buildUseCaseObservable(forceRefresh: Boolean?): Observable<List<UserGroup>> =
            userGroupsRepository.loadGroups(forceRefresh!!)
}

