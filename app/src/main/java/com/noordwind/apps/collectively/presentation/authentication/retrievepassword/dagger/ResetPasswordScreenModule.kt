package com.noordwind.apps.collectively.presentation.settings.dagger

import com.noordwind.apps.collectively.domain.repository.ConnectivityRepository
import com.noordwind.apps.collectively.domain.interactor.authentication.RetrievePasswordUseCase
import com.noordwind.apps.collectively.presentation.authentication.retrievepassword.mvp.ResetPasswordMvp
import com.noordwind.apps.collectively.presentation.authentication.retrievepassword.mvp.ResetPasswordPresenter
import dagger.Module
import dagger.Provides

@Module
class ResetPasswordScreenModule(val view: ResetPasswordMvp.View) {

    @Provides
    internal fun presenter(retrievePasswordUseCase: RetrievePasswordUseCase,
                           connectivityRepository: ConnectivityRepository): ResetPasswordMvp.Presenter {

        return ResetPasswordPresenter(view, retrievePasswordUseCase, connectivityRepository)
    }
}
