package pl.adriankremski.collectively.domain.interactor

import io.reactivex.Observable
import pl.adriankremski.collectively.data.model.Settings
import pl.adriankremski.collectively.data.repository.SettingsRepository
import pl.adriankremski.collectively.domain.thread.PostExecutionThread
import pl.adriankremski.collectively.domain.thread.UseCaseThread

class SaveSettingsUseCase(val settingsRepository: SettingsRepository,
                        useCaseThread: UseCaseThread,
                        postExecutionThread: PostExecutionThread) : UseCase<Boolean, Settings>(useCaseThread, postExecutionThread) {
    override fun buildUseCaseObservable(settings: Settings?): Observable<Boolean>  = settingsRepository.saveSettings(settings!!)
}

