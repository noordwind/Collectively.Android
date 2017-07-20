package com.noordwind.apps.collectively.data.repository

import io.reactivex.Observable
import com.noordwind.apps.collectively.data.model.User

interface UsersRepository {
    fun loadUser(userName: String): Observable<User>
}
