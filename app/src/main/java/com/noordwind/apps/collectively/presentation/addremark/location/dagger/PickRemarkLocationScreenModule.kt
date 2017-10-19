package com.noordwind.apps.collectively.presentation.settings.dagger

import com.noordwind.apps.collectively.data.repository.util.LocationRepository
import com.noordwind.apps.collectively.domain.thread.PostExecutionThread
import com.noordwind.apps.collectively.domain.thread.UseCaseThread
import com.noordwind.apps.collectively.presentation.addremark.PickRemarkLocationMvp
import com.noordwind.apps.collectively.presentation.addremark.PickRemarkLocationPresenter
import com.noordwind.apps.collectively.usecases.LoadAddressFromLocationUseCase
import dagger.Module
import dagger.Provides

@Module
class PickRemarkLocationScreenModule(val view: PickRemarkLocationMvp.View) {

    @Provides
    internal fun presenter(
            locationRepository: LocationRepository,
            ioThread: UseCaseThread,
            uiThread: PostExecutionThread): PickRemarkLocationMvp.Presenter {

        return PickRemarkLocationPresenter(view, LoadAddressFromLocationUseCase(locationRepository, ioThread, uiThread))
    }
}
