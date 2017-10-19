package com.noordwind.apps.collectively.presentation.settings.dagger

import com.noordwind.apps.collectively.data.datasource.FiltersTranslationsDataSource
import com.noordwind.apps.collectively.data.datasource.MapFiltersRepository
import com.noordwind.apps.collectively.data.repository.RemarksRepository
import com.noordwind.apps.collectively.data.repository.UserGroupsRepository
import com.noordwind.apps.collectively.data.repository.util.LocationRepository
import com.noordwind.apps.collectively.domain.interactor.remark.LoadRemarkCategoriesUseCase
import com.noordwind.apps.collectively.domain.interactor.remark.LoadRemarksUseCase
import com.noordwind.apps.collectively.domain.interactor.remark.filters.map.LoadMapFiltersUseCase
import com.noordwind.apps.collectively.domain.thread.PostExecutionThread
import com.noordwind.apps.collectively.domain.thread.UseCaseThread
import com.noordwind.apps.collectively.presentation.main.mvp.MainMvp
import com.noordwind.apps.collectively.presentation.main.mvp.MainPresenter
import com.noordwind.apps.collectively.usecases.LoadAddressFromLocationUseCase
import dagger.Module
import dagger.Provides

@Module
class MainScreenModule(val view: MainMvp.View) {

    @Provides
    internal fun presenter(mapFiltersRepository: MapFiltersRepository,
                           remarksRepository: RemarksRepository,
                           locationRepository: LocationRepository,
                           translationDataSource: FiltersTranslationsDataSource,
                           userGroupsRepository: UserGroupsRepository,
                           ioThread: UseCaseThread, uiThread: PostExecutionThread): MainMvp.Presenter {

        return MainPresenter(view, LoadRemarksUseCase(remarksRepository, ioThread, uiThread),
                LoadRemarkCategoriesUseCase(remarksRepository, translationDataSource, ioThread, uiThread),
                LoadMapFiltersUseCase(mapFiltersRepository, userGroupsRepository, ioThread, uiThread),
                LoadAddressFromLocationUseCase(locationRepository, ioThread, uiThread),
                translationDataSource)
    }
}
