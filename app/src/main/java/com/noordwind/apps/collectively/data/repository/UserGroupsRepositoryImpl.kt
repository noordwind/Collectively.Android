package com.noordwind.apps.collectively.data.repository

import com.noordwind.apps.collectively.data.cache.Cache
import com.noordwind.apps.collectively.data.datasource.UserGroupsDataSource
import com.noordwind.apps.collectively.data.model.UserGroup
import io.reactivex.Observable

class UserGroupsRepositoryImpl(val userGroupsDataSource: UserGroupsDataSource, val cache: Cache<List<UserGroup>>) : UserGroupsRepository {

    override fun loadGroups(forceRefresh: Boolean): Observable<List<UserGroup>> {
        if (forceRefresh || cache.isExpired()) {
            return loadAndSaveToCache()
        } else {
            return cache.getData()
        }
    }

    private fun loadAndSaveToCache(): Observable<List<UserGroup>> {
        return userGroupsDataSource.groups().flatMap {
            cache.putData(it)
            Observable.just(it)
        }
    }
}

