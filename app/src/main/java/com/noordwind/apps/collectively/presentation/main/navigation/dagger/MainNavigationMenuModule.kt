package com.noordwind.apps.collectively.presentation.settings.dagger

import com.noordwind.apps.collectively.data.repository.ProfileRepository
import com.noordwind.apps.collectively.data.repository.util.LocationRepository
import com.noordwind.apps.collectively.domain.interactor.profile.LoadProfileUseCase
import com.noordwind.apps.collectively.domain.thread.PostExecutionThread
import com.noordwind.apps.collectively.domain.thread.UseCaseThread
import com.noordwind.apps.collectively.presentation.main.navigation.mvp.NavigationMvp
import com.noordwind.apps.collectively.presentation.main.navigation.mvp.NavigationPresenter
import com.noordwind.apps.collectively.usecases.LoadLastKnownLocationUseCase
import dagger.Module
import dagger.Provides

@Module
class MainNavigationMenuModule(val view: NavigationMvp.View) {

    @Provides
    internal fun presenter(profileRepository: ProfileRepository,
                           locationRepository: LocationRepository,
                           ioThread: UseCaseThread, uiThread: PostExecutionThread): NavigationMvp.Presenter {
        return NavigationPresenter(view,
                LoadProfileUseCase(profileRepository, ioThread, uiThread),
                LoadLastKnownLocationUseCase(locationRepository, ioThread, uiThread))
    }
}
