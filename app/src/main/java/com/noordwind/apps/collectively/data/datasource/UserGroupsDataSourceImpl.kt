package com.noordwind.apps.collectively.data.datasource

import com.noordwind.apps.collectively.data.model.UserGroup
import com.noordwind.apps.collectively.data.net.Api
import io.reactivex.Observable

class UserGroupsDataSourceImpl(val api: Api) : UserGroupsDataSource{

    override fun groups(): Observable<List<UserGroup>>  = api.groups()
}

