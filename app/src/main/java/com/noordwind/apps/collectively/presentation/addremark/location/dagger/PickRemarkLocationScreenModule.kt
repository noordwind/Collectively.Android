package com.noordwind.apps.collectively.presentation.settings.dagger

import com.noordwind.apps.collectively.presentation.addremark.PickRemarkLocationMvp
import com.noordwind.apps.collectively.presentation.addremark.PickRemarkLocationPresenter
import com.noordwind.apps.collectively.usecases.LoadAddressFromLocationUseCase
import dagger.Module
import dagger.Provides

@Module
class PickRemarkLocationScreenModule(val view: PickRemarkLocationMvp.View) {

    @Provides
    internal fun presenter(loadAddressFromLocationUseCase: LoadAddressFromLocationUseCase): PickRemarkLocationMvp.Presenter {
        return PickRemarkLocationPresenter(view, loadAddressFromLocationUseCase)
    }
}
