package com.noordwind.apps.collectively.domain.interactor.settings

import io.reactivex.Observable
import com.noordwind.apps.collectively.data.model.Settings
import com.noordwind.apps.collectively.data.repository.SettingsRepository
import com.noordwind.apps.collectively.domain.interactor.UseCase
import com.noordwind.apps.collectively.domain.thread.PostExecutionThread
import com.noordwind.apps.collectively.domain.thread.UseCaseThread

class LoadSettingsUseCase(val settingsRepository: SettingsRepository,
                          useCaseThread: UseCaseThread,
                          postExecutionThread: PostExecutionThread) : UseCase<Settings, Void>(useCaseThread, postExecutionThread) {
    override fun buildUseCaseObservable(params: Void?): Observable<Settings> = settingsRepository.loadSettings()
}

