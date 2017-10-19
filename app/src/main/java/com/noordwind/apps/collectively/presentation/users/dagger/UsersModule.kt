package com.noordwind.apps.collectively.presentation.settings.dagger

import com.noordwind.apps.collectively.data.repository.UsersRepository
import com.noordwind.apps.collectively.domain.thread.PostExecutionThread
import com.noordwind.apps.collectively.domain.thread.UseCaseThread
import com.noordwind.apps.collectively.presentation.profile.remarks.user.UsersPresenter
import com.noordwind.apps.collectively.presentation.statistics.LoadUsersUseCase
import com.noordwind.apps.collectively.presentation.users.UsersMvp
import dagger.Module
import dagger.Provides

@Module
class UsersModule(val view: UsersMvp.View) {

    @Provides
    internal fun presenter(usersRepository: UsersRepository,
                           ioThread: UseCaseThread,
                           uiThread: PostExecutionThread): UsersMvp.Presenter {
        return UsersPresenter(view, LoadUsersUseCase(usersRepository, ioThread, uiThread))
    }
}
