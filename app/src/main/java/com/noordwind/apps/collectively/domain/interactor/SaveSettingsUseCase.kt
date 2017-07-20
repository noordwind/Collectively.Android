package com.noordwind.apps.collectively.domain.interactor

import io.reactivex.Observable
import com.noordwind.apps.collectively.data.model.Settings
import com.noordwind.apps.collectively.data.repository.SettingsRepository
import com.noordwind.apps.collectively.domain.thread.PostExecutionThread
import com.noordwind.apps.collectively.domain.thread.UseCaseThread

class SaveSettingsUseCase(val settingsRepository: SettingsRepository,
                        useCaseThread: UseCaseThread,
                        postExecutionThread: PostExecutionThread) : UseCase<Boolean, Settings>(useCaseThread, postExecutionThread) {
    override fun buildUseCaseObservable(settings: Settings?): Observable<Boolean>  = settingsRepository.saveSettings(settings!!)
}

