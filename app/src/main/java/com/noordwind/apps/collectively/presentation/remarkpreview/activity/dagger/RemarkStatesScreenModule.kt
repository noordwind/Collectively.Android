package com.noordwind.apps.collectively.presentation.settings.dagger

import com.noordwind.apps.collectively.domain.interactor.remark.states.*
import com.noordwind.apps.collectively.presentation.remarkpreview.activity.mvp.RemarkStatesMvp
import com.noordwind.apps.collectively.presentation.remarkpreview.activity.mvp.RemarkStatesPresenter
import dagger.Module
import dagger.Provides

@Module
class RemarkStatesScreenModule(val view: RemarkStatesMvp.View) {

    @Provides
    internal fun presenter(
            loadRemarkStatesUseCase: LoadRemarkStatesUseCase,
            deleteRemarkUseCase: DeleteRemarkUseCase,
            processRemarkUseCase: ProcessRemarkUseCase,
            resolveRemarkUseCase: ResolveRemarkUseCase,
            reopenRemarkUseCase: ReopenRemarkUseCase): RemarkStatesMvp.Presenter {
        return RemarkStatesPresenter(view, loadRemarkStatesUseCase, deleteRemarkUseCase,
                processRemarkUseCase, resolveRemarkUseCase, reopenRemarkUseCase)
    }
}
