package com.noordwind.apps.collectively.presentation.settings.dagger

import com.noordwind.apps.collectively.domain.interactor.remark.filters.map.*
import com.noordwind.apps.collectively.domain.interactor.remark.filters.remark.SelectRemarkGroupUseCase
import com.noordwind.apps.collectively.presentation.views.dialogs.mapfilters.RemarkFiltersPresenter
import com.noordwind.apps.collectively.presentation.views.dialogs.remarkfilters.FiltersMvp
import dagger.Module
import dagger.Provides

@Module
class RemarkFiltersDialogModule(val view: FiltersMvp.View) {

    @Provides
    internal fun presenter(
            loadRemarkFiltersUseCase: LoadRemarkFiltersUseCase,
            addCategoryFilterUseCase: AddCategoryFilterUseCase,
            addStatusFilterUseCase: AddStatusFilterUseCase,
            removeCategoryFilterUseCase: RemoveCategoryFilterUseCase,
            selectRemarkGroupUseCase: SelectRemarkGroupUseCase,
            removeStatusFilterUseCase: RemoveStatusFilterUseCase): FiltersMvp.Presenter {

        return RemarkFiltersPresenter(view, loadRemarkFiltersUseCase, addCategoryFilterUseCase,
                addStatusFilterUseCase, removeCategoryFilterUseCase, selectRemarkGroupUseCase, removeStatusFilterUseCase)
    }
}
