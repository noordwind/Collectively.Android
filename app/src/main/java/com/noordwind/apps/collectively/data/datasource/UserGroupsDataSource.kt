package com.noordwind.apps.collectively.data.datasource

import com.noordwind.apps.collectively.data.model.UserGroup
import io.reactivex.Observable

interface UserGroupsDataSource{
    fun groups() : Observable<List<UserGroup>>
}

