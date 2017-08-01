package com.noordwind.apps.collectively.data.datasource

import com.noordwind.apps.collectively.data.model.User
import com.noordwind.apps.collectively.data.net.Api
import io.reactivex.Observable

class UserDataSourceImpl(val api: Api) : UserDataSource {
    override fun loadUsers(): Observable<List<User>> = api.loadUsers("1", "1000")
    override fun loadUser(userName: String): Observable<User> = api.loadUser(userName)
}

