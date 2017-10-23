package com.noordwind.apps.collectively.presentation.settings.dagger

import com.noordwind.apps.collectively.domain.interactor.profile.LoadCurrentUserProfileDataUseCase
import com.noordwind.apps.collectively.domain.interactor.profile.LoadUserProfileDataUseCase
import com.noordwind.apps.collectively.presentation.profile.mvp.ProfileMvp
import com.noordwind.apps.collectively.presentation.profile.mvp.ProfilePresenter
import dagger.Module
import dagger.Provides

@Module
class ProfileScreenModule(val view: ProfileMvp.View) {

    @Provides
    internal fun presenter(loadCurrentUserProfileDataUseCase: LoadCurrentUserProfileDataUseCase,
                           loadUserProfileDataUseCase: LoadUserProfileDataUseCase): ProfileMvp.Presenter {
        return ProfilePresenter(view, loadCurrentUserProfileDataUseCase, loadUserProfileDataUseCase)
    }
}
