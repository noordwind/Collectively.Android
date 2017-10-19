package com.noordwind.apps.collectively.presentation.settings.dagger

import com.noordwind.apps.collectively.data.datasource.FiltersTranslationsDataSource
import com.noordwind.apps.collectively.data.repository.RemarksRepository
import com.noordwind.apps.collectively.data.repository.UserGroupsRepository
import com.noordwind.apps.collectively.data.repository.util.ConnectivityRepository
import com.noordwind.apps.collectively.data.repository.util.LocationRepository
import com.noordwind.apps.collectively.domain.interactor.remark.LoadRemarkCategoriesUseCase
import com.noordwind.apps.collectively.domain.interactor.remark.SaveRemarkUseCase
import com.noordwind.apps.collectively.domain.thread.PostExecutionThread
import com.noordwind.apps.collectively.domain.thread.UseCaseThread
import com.noordwind.apps.collectively.presentation.addremark.mvp.AddRemarkMvp
import com.noordwind.apps.collectively.presentation.addremark.mvp.AddRemarkPresenter
import com.noordwind.apps.collectively.presentation.statistics.LoadUserGroupsUseCase
import com.noordwind.apps.collectively.usecases.LoadAddressFromLocationUseCase
import com.noordwind.apps.collectively.usecases.LoadLastKnownLocationUseCase
import dagger.Module
import dagger.Provides

@Module
class AddRemarkScreenModule(val view: AddRemarkMvp.View) {

    @Provides
    internal fun presenter(
            remarksRepository: RemarksRepository,
            locationRepository: LocationRepository,
            userGroupsRepository: UserGroupsRepository,
            connectivityRepository: ConnectivityRepository,
            translationsDataSource: FiltersTranslationsDataSource,
            ioThread: UseCaseThread,
            uiThread: PostExecutionThread): AddRemarkMvp.Presenter {

        return AddRemarkPresenter(view, SaveRemarkUseCase(remarksRepository, ioThread, uiThread),
                LoadRemarkCategoriesUseCase(remarksRepository, translationsDataSource, ioThread, uiThread),
                LoadLastKnownLocationUseCase(locationRepository, ioThread, uiThread),
                LoadAddressFromLocationUseCase(locationRepository, ioThread, uiThread),
                LoadUserGroupsUseCase(userGroupsRepository, ioThread, uiThread),
                connectivityRepository)
    }
}
