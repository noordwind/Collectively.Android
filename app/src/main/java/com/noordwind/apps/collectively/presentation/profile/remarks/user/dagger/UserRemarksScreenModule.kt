package com.noordwind.apps.collectively.presentation.settings.dagger

import android.content.Context
import com.noordwind.apps.collectively.data.datasource.FiltersTranslationsDataSource
import com.noordwind.apps.collectively.data.datasource.RemarkFiltersRepository
import com.noordwind.apps.collectively.data.repository.RemarksRepository
import com.noordwind.apps.collectively.data.repository.UserGroupsRepository
import com.noordwind.apps.collectively.domain.interactor.remark.LoadUserFavoriteRemarksUseCase
import com.noordwind.apps.collectively.domain.interactor.remark.LoadUserRemarksUseCase
import com.noordwind.apps.collectively.domain.interactor.remark.LoadUserResolvedRemarksUseCase
import com.noordwind.apps.collectively.domain.interactor.remark.filters.map.ClearRemarkFiltersUseCase
import com.noordwind.apps.collectively.domain.interactor.remark.filters.map.LoadRemarkFiltersUseCase
import com.noordwind.apps.collectively.domain.thread.PostExecutionThread
import com.noordwind.apps.collectively.domain.thread.UseCaseThread
import com.noordwind.apps.collectively.presentation.profile.remarks.user.mvp.UserRemarksMvp
import com.noordwind.apps.collectively.presentation.profile.remarks.user.mvp.UserRemarksPresenter
import dagger.Module
import dagger.Provides

@Module
class UserRemarksScreenModule(val view: UserRemarksMvp.View) {

    @Provides
    internal fun presenter(context: Context,
                           remarksRepository: RemarksRepository,
                           filtersRepository: RemarkFiltersRepository,
                           userGroupsRepository: UserGroupsRepository,
                           translationDataSource: FiltersTranslationsDataSource,
                           ioThread: UseCaseThread, uiThread: PostExecutionThread): UserRemarksMvp.Presenter {
        return UserRemarksPresenter(context, view,
                loadUserRemarksUseCase = LoadUserRemarksUseCase(remarksRepository, ioThread, uiThread),
                loadUserResolvedRemarksUseCase = LoadUserResolvedRemarksUseCase(remarksRepository, ioThread, uiThread),
                loadRemarkFiltersUseCase = LoadRemarkFiltersUseCase(filtersRepository, userGroupsRepository, ioThread, uiThread),
                loadUserFavoriteRemarksUseCase = LoadUserFavoriteRemarksUseCase(remarksRepository, ioThread, uiThread),
                clearRemarkFiltersUseCase = ClearRemarkFiltersUseCase(filtersRepository, ioThread, uiThread),
                translationsDataSource = translationDataSource)
    }
}
