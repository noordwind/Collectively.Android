package com.noordwind.apps.collectively.presentation.settings.dagger

import com.noordwind.apps.collectively.domain.interactor.authentication.ChangePasswordUseCase
import com.noordwind.apps.collectively.presentation.changepassword.mvp.ChangePasswordMvp
import com.noordwind.apps.collectively.presentation.changepassword.mvp.ChangePasswordPresenter
import dagger.Module
import dagger.Provides

@Module
class ChangePasswordScreenModule(val view: ChangePasswordMvp.View) {

    @Provides
    internal fun presenter(changePasswordUseCase: ChangePasswordUseCase): ChangePasswordMvp.Presenter {
        return ChangePasswordPresenter(view, changePasswordUseCase)
    }
}
