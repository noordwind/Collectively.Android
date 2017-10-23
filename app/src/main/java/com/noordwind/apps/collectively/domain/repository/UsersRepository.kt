package com.noordwind.apps.collectively.data.repository

import com.noordwind.apps.collectively.data.model.User
import io.reactivex.Observable

interface UsersRepository {
    fun loadUser(userName: String): Observable<User>
    fun loadUsers(): Observable<List<User>>
}
