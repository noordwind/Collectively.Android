package com.noordwind.apps.collectively.presentation.settings.dagger

import com.noordwind.apps.collectively.domain.interactor.remark.states.LoadRemarkStatesUseCase
import com.noordwind.apps.collectively.domain.interactor.remark.states.ProcessRemarkUseCase
import com.noordwind.apps.collectively.domain.interactor.remark.states.ReopenRemarkUseCase
import com.noordwind.apps.collectively.domain.interactor.remark.states.ResolveRemarkUseCase
import com.noordwind.apps.collectively.presentation.remarkpreview.activity.mvp.RemarkStatesMvp
import com.noordwind.apps.collectively.presentation.remarkpreview.activity.mvp.RemarkStatesPresenter
import dagger.Module
import dagger.Provides

@Module
class RemarkStatesScreenModule(val view: RemarkStatesMvp.View) {

    @Provides
    internal fun presenter(
            loadRemarkStatesUseCase: LoadRemarkStatesUseCase,
            processRemarkUseCase: ProcessRemarkUseCase,
            resolveRemarkUseCase: ResolveRemarkUseCase,
            reopenRemarkUseCase: ReopenRemarkUseCase): RemarkStatesMvp.Presenter {
        return RemarkStatesPresenter(view, loadRemarkStatesUseCase, processRemarkUseCase, resolveRemarkUseCase, reopenRemarkUseCase)
    }
}
