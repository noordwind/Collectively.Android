package com.noordwind.apps.collectively.data.datasource

import com.noordwind.apps.collectively.data.model.User
import io.reactivex.Observable

interface UserDataSource {
    fun loadUser(userName: String): Observable<User>
    fun loadUsers(): Observable<List<User>>
}
