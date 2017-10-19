package com.noordwind.apps.collectively.presentation.settings.dagger

import com.noordwind.apps.collectively.data.repository.ProfileRepository
import com.noordwind.apps.collectively.data.repository.RemarksRepository
import com.noordwind.apps.collectively.domain.interactor.profile.LoadCurrentUserProfileDataUseCase
import com.noordwind.apps.collectively.domain.interactor.profile.LoadUserProfileDataUseCase
import com.noordwind.apps.collectively.domain.thread.PostExecutionThread
import com.noordwind.apps.collectively.domain.thread.UseCaseThread
import com.noordwind.apps.collectively.presentation.profile.ProfileMvp
import com.noordwind.apps.collectively.presentation.profile.ProfilePresenter
import dagger.Module
import dagger.Provides

@Module
class ProfileScreenModule(val view: ProfileMvp.View) {

    @Provides
    internal fun presenter(
            remarksRepository: RemarksRepository,
            profileRepository: ProfileRepository,
            ioThread: UseCaseThread,
            uiThread: PostExecutionThread): ProfileMvp.Presenter {

        return ProfilePresenter(view,
                LoadCurrentUserProfileDataUseCase(remarksRepository, profileRepository, ioThread, uiThread),
                LoadUserProfileDataUseCase(remarksRepository, ioThread, uiThread))
    }
}
