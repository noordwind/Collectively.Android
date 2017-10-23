package com.noordwind.apps.collectively.data.repository

import com.noordwind.apps.collectively.data.model.Profile
import io.reactivex.Observable

interface ProfileRepository {
    fun loadProfile(forceRefresh: Boolean): Observable<Profile>
    fun loadProfileFromCache(): Observable<Profile>
    fun loadProfileFromCacheSync(): Profile
}

