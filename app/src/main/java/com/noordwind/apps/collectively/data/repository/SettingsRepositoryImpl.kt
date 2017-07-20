package com.noordwind.apps.collectively.data.repository

import io.reactivex.Observable
import com.noordwind.apps.collectively.data.datasource.SettingsDataSource
import com.noordwind.apps.collectively.data.model.Settings
import com.noordwind.apps.collectively.data.repository.util.OperationRepository

class SettingsRepositoryImpl(val settingsDataSource: SettingsDataSource, val operationRepository: OperationRepository) : SettingsRepository {
    override fun saveSettings(settings: Settings): Observable<Boolean> {
        return operationRepository.pollOperation(settingsDataSource.saveSettings(settings)).flatMap { Observable.just(true) }
    }

    override fun loadSettings(): Observable<Settings> = settingsDataSource.settings()
}

