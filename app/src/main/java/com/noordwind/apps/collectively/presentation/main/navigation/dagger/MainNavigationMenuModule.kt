package com.noordwind.apps.collectively.presentation.settings.dagger

import com.noordwind.apps.collectively.domain.interactor.profile.LoadProfileUseCase
import com.noordwind.apps.collectively.presentation.main.navigation.mvp.NavigationMvp
import com.noordwind.apps.collectively.presentation.main.navigation.mvp.NavigationPresenter
import com.noordwind.apps.collectively.usecases.LoadLastKnownLocationUseCase
import dagger.Module
import dagger.Provides

@Module
class MainNavigationMenuModule(val view: NavigationMvp.View) {

    @Provides
    internal fun presenter(loadProfileUseCase: LoadProfileUseCase, loadLastKnownLocationUseCase: LoadLastKnownLocationUseCase): NavigationMvp.Presenter {
        return NavigationPresenter(view, loadProfileUseCase, loadLastKnownLocationUseCase)
    }
}
