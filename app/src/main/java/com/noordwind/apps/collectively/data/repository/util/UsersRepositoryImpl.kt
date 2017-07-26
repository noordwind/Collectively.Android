package com.noordwind.apps.collectively.data.repository

import com.noordwind.apps.collectively.data.datasource.UserDataSource
import com.noordwind.apps.collectively.data.model.User
import io.reactivex.Observable

class UsersRepositoryImpl(val userDataSource: UserDataSource) : UsersRepository {
    override fun loadUsers(): Observable<List<User>>  = userDataSource.loadUsers()
    override fun loadUser(userName: String): Observable<User> = userDataSource.loadUser(userName)
}

