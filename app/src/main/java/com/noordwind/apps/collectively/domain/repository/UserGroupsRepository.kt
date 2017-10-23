package com.noordwind.apps.collectively.data.repository

import com.noordwind.apps.collectively.data.model.UserGroup
import io.reactivex.Observable

interface UserGroupsRepository {
    fun loadGroups(forceRefresh: Boolean): Observable<List<UserGroup>>
}

