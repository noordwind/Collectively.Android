package com.noordwind.apps.collectively.data.repository

import io.reactivex.Observable
import com.noordwind.apps.collectively.data.model.Profile

interface ProfileRepository {
    fun loadProfile(forceRefresh: Boolean): Observable<Profile>
}

