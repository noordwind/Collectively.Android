package com.noordwind.apps.collectively.presentation.settings.dagger

import com.noordwind.apps.collectively.data.datasource.MapFiltersRepository
import com.noordwind.apps.collectively.data.repository.UserGroupsRepository
import com.noordwind.apps.collectively.domain.interactor.remark.filters.map.*
import com.noordwind.apps.collectively.domain.thread.PostExecutionThread
import com.noordwind.apps.collectively.domain.thread.UseCaseThread
import com.noordwind.apps.collectively.presentation.views.dialogs.mapfilters.mvp.MapFiltersMvp
import com.noordwind.apps.collectively.presentation.views.dialogs.mapfilters.mvp.MapFiltersPresenter
import dagger.Module
import dagger.Provides

@Module
class MapFiltersDialogModule(val view: MapFiltersMvp.View) {

    @Provides
    internal fun presenter(mapFiltersRepository: MapFiltersRepository,
                           userGroupsRepository: UserGroupsRepository,
                           ioThread: UseCaseThread, uiThread: PostExecutionThread): MapFiltersMvp.Presenter {

        return MapFiltersPresenter(view,
                LoadMapFiltersUseCase(mapFiltersRepository, userGroupsRepository, ioThread, uiThread),
                AddMapCategoryFilterUseCase(mapFiltersRepository, ioThread, uiThread),
                RemoveMapCategoryFilterUseCase(mapFiltersRepository, ioThread, uiThread),
                AddMapStatusFilterUseCase(mapFiltersRepository, ioThread, uiThread),
                RemoveMapStatusFilterUseCase(mapFiltersRepository, ioThread, uiThread),
                SelectShowOnlyMyRemarksUseCase(mapFiltersRepository, ioThread, uiThread),
                SelectRemarkGroupUseCase(mapFiltersRepository, ioThread, uiThread))
    }
}
