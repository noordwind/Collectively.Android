package com.noordwind.apps.collectively.presentation.settings.dagger

import android.content.Context
import com.noordwind.apps.collectively.data.datasource.FiltersTranslationsDataSource
import com.noordwind.apps.collectively.domain.interactor.remark.LoadUserFavoriteRemarksUseCase
import com.noordwind.apps.collectively.domain.interactor.remark.LoadUserRemarksUseCase
import com.noordwind.apps.collectively.domain.interactor.remark.LoadUserResolvedRemarksUseCase
import com.noordwind.apps.collectively.domain.interactor.remark.filters.map.ClearRemarkFiltersUseCase
import com.noordwind.apps.collectively.domain.interactor.remark.filters.map.LoadRemarkFiltersUseCase
import com.noordwind.apps.collectively.presentation.profile.remarks.user.mvp.UserRemarksMvp
import com.noordwind.apps.collectively.presentation.profile.remarks.user.mvp.UserRemarksPresenter
import dagger.Module
import dagger.Provides

@Module
class UserRemarksScreenModule(val view: UserRemarksMvp.View) {

    @Provides
    internal fun presenter(context: Context,
                           loadUserRemarksUseCase: LoadUserRemarksUseCase,
                           loadUserResolvedRemarksUseCase: LoadUserResolvedRemarksUseCase,
                           loadRemarkFiltersUseCase: LoadRemarkFiltersUseCase,
                           loadUserFavoriteRemarksUseCase: LoadUserFavoriteRemarksUseCase,
                           clearRemarkFiltersUseCase: ClearRemarkFiltersUseCase,
                           translationDataSource: FiltersTranslationsDataSource): UserRemarksMvp.Presenter {
        return UserRemarksPresenter(context, view, loadUserRemarksUseCase, loadUserResolvedRemarksUseCase,
                loadRemarkFiltersUseCase, loadUserFavoriteRemarksUseCase, clearRemarkFiltersUseCase,
                translationDataSource)
    }
}
