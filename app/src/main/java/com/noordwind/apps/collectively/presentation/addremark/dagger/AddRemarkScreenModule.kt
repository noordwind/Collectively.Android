package com.noordwind.apps.collectively.presentation.settings.dagger

import com.noordwind.apps.collectively.data.repository.util.ConnectivityRepository
import com.noordwind.apps.collectively.domain.interactor.remark.LoadRemarkCategoriesUseCase
import com.noordwind.apps.collectively.domain.interactor.remark.SaveRemarkUseCase
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
            saveRemarkUseCase: SaveRemarkUseCase,
            loadRemarkCategoriesUseCase: LoadRemarkCategoriesUseCase,
            loadLastKnownLocationUseCase: LoadLastKnownLocationUseCase,
            loadAddressFromLocationUseCase: LoadAddressFromLocationUseCase,
            loadUserGroupsUseCase: LoadUserGroupsUseCase,
            connectivityRepository: ConnectivityRepository): AddRemarkMvp.Presenter {

        return AddRemarkPresenter(view, saveRemarkUseCase,
                loadRemarkCategoriesUseCase,
                loadLastKnownLocationUseCase,
                loadAddressFromLocationUseCase,
                loadUserGroupsUseCase,
                connectivityRepository)
    }
}
