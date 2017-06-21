package com.noordwind.apps.collectively.data.repository

import io.reactivex.Observable
import com.noordwind.apps.collectively.data.datasource.UserDataSource
import com.noordwind.apps.collectively.data.model.User

class UsersRepositoryImpl(val userDataSource: UserDataSource) : UsersRepository {
    override fun loadUser(userName: String): Observable<User> = userDataSource.loadUser(userName)
}

