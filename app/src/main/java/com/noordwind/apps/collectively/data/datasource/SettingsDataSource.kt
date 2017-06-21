package com.noordwind.apps.collectively.data.datasource

import io.reactivex.Observable
import com.noordwind.apps.collectively.data.model.Settings
import retrofit2.Response

interface SettingsDataSource {
    fun settings() : Observable<Settings>
    fun saveSettings(settings: Settings) : Observable<Response<Void>>
}

