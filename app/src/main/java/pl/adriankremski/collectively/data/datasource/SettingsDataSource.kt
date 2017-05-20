package pl.adriankremski.collectively.data.datasource

import io.reactivex.Observable
import pl.adriankremski.collectively.data.model.Settings
import retrofit2.Response

interface SettingsDataSource {
    fun settings() : Observable<Settings>
    fun saveSettings(settings: Settings) : Observable<Response<Void>>
}

