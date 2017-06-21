package com.noordwind.apps.collectively.data.repository

import io.reactivex.Observable
import com.noordwind.apps.collectively.data.model.Settings

interface SettingsRepository {
    fun loadSettings(): Observable<Settings>
    fun saveSettings(settings: Settings) : Observable<Boolean>
}

