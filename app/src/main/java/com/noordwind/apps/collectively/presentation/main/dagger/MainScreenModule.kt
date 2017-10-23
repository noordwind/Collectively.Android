package com.noordwind.apps.collectively.presentation.settings.dagger

import com.noordwind.apps.collectively.data.datasource.FiltersTranslationsDataSource
import com.noordwind.apps.collectively.domain.interactor.remark.LoadRemarkCategoriesUseCase
import com.noordwind.apps.collectively.domain.interactor.remark.LoadRemarksUseCase
import com.noordwind.apps.collectively.domain.interactor.remark.filters.map.LoadMapFiltersUseCase
import com.noordwind.apps.collectively.presentation.main.mvp.MainMvp
import com.noordwind.apps.collectively.presentation.main.mvp.MainPresenter
import com.noordwind.apps.collectively.usecases.LoadAddressFromLocationUseCase
import dagger.Module
import dagger.Provides

@Module
class MainScreenModule(val view: MainMvp.View) {

    @Provides
    internal fun presenter(
            loadRemarksUseCase: LoadRemarksUseCase,
            loadRemarkCategoriesUseCase: LoadRemarkCategoriesUseCase,
            loadMapFiltersUseCase: LoadMapFiltersUseCase,
            loadAddressFromLocationUseCase: LoadAddressFromLocationUseCase,
            translationDataSource: FiltersTranslationsDataSource): MainMvp.Presenter {

        return MainPresenter(view, loadRemarksUseCase, loadRemarkCategoriesUseCase, loadMapFiltersUseCase,
                loadAddressFromLocationUseCase, translationDataSource)
    }
}
