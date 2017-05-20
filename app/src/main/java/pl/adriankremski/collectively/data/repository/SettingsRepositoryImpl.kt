package pl.adriankremski.collectively.data.repository

import io.reactivex.Observable
import pl.adriankremski.collectively.data.datasource.SettingsDataSource
import pl.adriankremski.collectively.data.model.Settings
import pl.adriankremski.collectively.data.repository.util.OperationRepository

class SettingsRepositoryImpl(val settingsDataSource: SettingsDataSource, val operationRepository: OperationRepository) : SettingsRepository {
    override fun saveSettings(settings: Settings): Observable<Boolean> {
        return operationRepository.pollOperation(settingsDataSource.saveSettings(settings)).flatMap { Observable.just(true) }
    }

    override fun loadSettings(): Observable<Settings> = settingsDataSource.settings()
}

