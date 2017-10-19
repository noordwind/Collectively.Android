package com.noordwind.apps.collectively.presentation.settings.dagger

import com.noordwind.apps.collectively.data.datasource.RemarkFiltersRepository
import com.noordwind.apps.collectively.data.repository.UserGroupsRepository
import com.noordwind.apps.collectively.domain.interactor.remark.filters.map.*
import com.noordwind.apps.collectively.domain.interactor.remark.filters.remark.SelectRemarkGroupUseCase
import com.noordwind.apps.collectively.domain.thread.PostExecutionThread
import com.noordwind.apps.collectively.domain.thread.UseCaseThread
import com.noordwind.apps.collectively.presentation.views.dialogs.mapfilters.RemarkFiltersPresenter
import com.noordwind.apps.collectively.presentation.views.dialogs.remarkfilters.FiltersMvp
import dagger.Module
import dagger.Provides

@Module
class RemarkFiltersDialogModule(val view: FiltersMvp.View) {

    @Provides
    internal fun presenter(filtersRepository: RemarkFiltersRepository,
                           userGroupsRepository: UserGroupsRepository,
                           ioThread: UseCaseThread, uiThread: PostExecutionThread): FiltersMvp.Presenter {

        return RemarkFiltersPresenter(view,
                loadRemarkFiltersUseCase = LoadRemarkFiltersUseCase(filtersRepository, userGroupsRepository, ioThread, uiThread),
                addCategoryFilterUseCase = AddCategoryFilterUseCase(filtersRepository, ioThread, uiThread),
                addStatusFilterUseCase = AddStatusFilterUseCase(filtersRepository, ioThread, uiThread),
                removeCategoryFilterUseCase = RemoveCategoryFilterUseCase(filtersRepository, ioThread, uiThread),
                selectRemarkGroupUseCase = SelectRemarkGroupUseCase(filtersRepository, ioThread, uiThread),
                removeStatusFilterUseCase = RemoveStatusFilterUseCase(filtersRepository, ioThread, uiThread))
    }
}
