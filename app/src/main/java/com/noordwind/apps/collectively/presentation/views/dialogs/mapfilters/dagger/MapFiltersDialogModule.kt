package com.noordwind.apps.collectively.presentation.settings.dagger

import com.noordwind.apps.collectively.domain.interactor.remark.filters.map.*
import com.noordwind.apps.collectively.presentation.views.dialogs.mapfilters.mvp.MapFiltersMvp
import com.noordwind.apps.collectively.presentation.views.dialogs.mapfilters.mvp.MapFiltersPresenter
import dagger.Module
import dagger.Provides

@Module
class MapFiltersDialogModule(val view: MapFiltersMvp.View) {

    @Provides
    internal fun presenter(
            loadMapFiltersUseCase: LoadMapFiltersUseCase,
            addMapCategoryFilterUseCase: AddMapCategoryFilterUseCase,
            removeMapCategoryFilterUseCase: RemoveMapCategoryFilterUseCase,
            addMapStatusFilterUseCase: AddMapStatusFilterUseCase,
            removeMapStatusFilterUseCase: RemoveMapStatusFilterUseCase,
            selectShowOnlyMyRemarksUseCase: SelectShowOnlyMyRemarksUseCase,
            selectRemarkGroupUseCase: SelectRemarkGroupUseCase): MapFiltersMvp.Presenter {

        return MapFiltersPresenter(view, loadMapFiltersUseCase, addMapCategoryFilterUseCase,
                removeMapCategoryFilterUseCase, addMapStatusFilterUseCase, removeMapStatusFilterUseCase,
                selectShowOnlyMyRemarksUseCase, selectRemarkGroupUseCase)
    }
}
