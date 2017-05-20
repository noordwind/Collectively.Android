package pl.adriankremski.collectively.data.repository

import io.reactivex.Observable
import pl.adriankremski.collectively.data.model.Settings

interface SettingsRepository {
    fun loadSettings(): Observable<Settings>
    fun saveSettings(settings: Settings) : Observable<Boolean>
}

