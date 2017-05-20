package pl.adriankremski.collectively.domain.interactor

import io.reactivex.Observable
import pl.adriankremski.collectively.data.model.Settings
import pl.adriankremski.collectively.data.repository.SettingsRepository
import pl.adriankremski.collectively.domain.thread.PostExecutionThread
import pl.adriankremski.collectively.domain.thread.UseCaseThread

class LoadSettingsUseCase(val settingsRepository: SettingsRepository,
                        useCaseThread: UseCaseThread,
                        postExecutionThread: PostExecutionThread) : UseCase<Settings, Void>(useCaseThread, postExecutionThread) {
    override fun buildUseCaseObservable(params: Void?): Observable<Settings>  = settingsRepository.loadSettings()
}

