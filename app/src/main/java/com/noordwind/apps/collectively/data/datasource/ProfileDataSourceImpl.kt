package com.noordwind.apps.collectively.data.datasource

import io.reactivex.Observable
import com.noordwind.apps.collectively.data.net.Api
import com.noordwind.apps.collectively.data.model.Profile

class ProfileDataSourceImpl(val api: Api) : ProfileDataSource{

    override fun profile(): Observable<Profile>  = api.loadProfile()
}

