package com.noordwind.apps.collectively.data.datasource

import io.reactivex.Observable
import com.noordwind.apps.collectively.data.model.*
import com.noordwind.apps.collectively.data.net.Api
import retrofit2.Response

class UserDataSourceImpl(val api: Api) : UserDataSource {
    override fun loadUser(userName: String): Observable<User> = api.loadUser(userName)
}

