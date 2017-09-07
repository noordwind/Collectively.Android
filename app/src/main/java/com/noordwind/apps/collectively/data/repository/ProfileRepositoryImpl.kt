package com.noordwind.apps.collectively.data.repository

import com.noordwind.apps.collectively.data.cache.Cache
import com.noordwind.apps.collectively.data.datasource.ProfileDataSource
import com.noordwind.apps.collectively.data.model.Profile
import io.reactivex.Observable

class ProfileRepositoryImpl(val profileDataSource: ProfileDataSource, val profileCache: Cache<Profile>) : ProfileRepository {

    override fun loadProfileFromCache(): Observable<Profile> {
        return profileCache.getData()
    }

    override fun loadProfile(forceRefresh: Boolean): Observable<Profile> {
        if (forceRefresh || profileCache.isExpired()) {
            return loadProfileAndSaveToCache()
        } else {
            return profileCache.getData()
        }
    }

    private fun loadProfileAndSaveToCache(): Observable<Profile> {
        return profileDataSource.profile().flatMap {
            profileCache.putData(it)
            Observable.just(it)
        }
    }

    override fun loadProfileFromCacheSync(): Profile {
        return profileCache.getDataSync()
    }
}

