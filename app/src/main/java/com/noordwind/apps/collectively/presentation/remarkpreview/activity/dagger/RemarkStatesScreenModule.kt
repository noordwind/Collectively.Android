package com.noordwind.apps.collectively.presentation.settings.dagger

import com.noordwind.apps.collectively.data.repository.ProfileRepository
import com.noordwind.apps.collectively.data.repository.RemarksRepository
import com.noordwind.apps.collectively.domain.interactor.remark.states.LoadRemarkStatesUseCase
import com.noordwind.apps.collectively.domain.interactor.remark.states.ProcessRemarkUseCase
import com.noordwind.apps.collectively.domain.interactor.remark.states.ReopenRemarkUseCase
import com.noordwind.apps.collectively.domain.interactor.remark.states.ResolveRemarkUseCase
import com.noordwind.apps.collectively.domain.thread.PostExecutionThread
import com.noordwind.apps.collectively.domain.thread.UseCaseThread
import com.noordwind.apps.collectively.presentation.remarkpreview.activity.mvp.RemarkStatesMvp
import com.noordwind.apps.collectively.presentation.remarkpreview.activity.mvp.RemarkStatesPresenter
import dagger.Module
import dagger.Provides

@Module
class RemarkStatesScreenModule(val view: RemarkStatesMvp.View) {

    @Provides
    internal fun presenter(remarksRepository: RemarksRepository,
                           profileRepository: ProfileRepository,
                           ioThread: UseCaseThread,
                           uiThread: PostExecutionThread): RemarkStatesMvp.Presenter {

        return RemarkStatesPresenter(view,
                LoadRemarkStatesUseCase(remarksRepository, profileRepository, ioThread, uiThread),
                ProcessRemarkUseCase(remarksRepository, profileRepository, ioThread, uiThread),
                ResolveRemarkUseCase(remarksRepository, profileRepository, ioThread, uiThread),
                ReopenRemarkUseCase(remarksRepository, profileRepository, ioThread, uiThread))
    }
}
