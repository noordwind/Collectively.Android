package com.noordwind.apps.collectively.presentation.settings.dagger

import com.noordwind.apps.collectively.presentation.profile.remarks.user.UsersPresenter
import com.noordwind.apps.collectively.presentation.statistics.LoadUsersUseCase
import com.noordwind.apps.collectively.presentation.users.UsersMvp
import dagger.Module
import dagger.Provides

@Module
class UsersModule(val view: UsersMvp.View) {

    @Provides
    internal fun presenter(loadUsersUseCase: LoadUsersUseCase) : UsersMvp.Presenter {
        return UsersPresenter(view, loadUsersUseCase)
    }
}
