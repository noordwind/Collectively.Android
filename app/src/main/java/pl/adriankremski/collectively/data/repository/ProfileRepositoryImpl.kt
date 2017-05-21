package pl.adriankremski.collectively.data.repository

import io.reactivex.Observable
import pl.adriankremski.collectively.data.cache.Cache
import pl.adriankremski.collectively.data.datasource.ProfileDataSource
import pl.adriankremski.collectively.data.model.Profile

class ProfileRepositoryImpl(val profileDataSource: ProfileDataSource, val profileCache: Cache<Profile>) : ProfileRepository {

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
}

