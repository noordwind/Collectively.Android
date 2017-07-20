package com.noordwind.apps.collectively.data.datasource

import io.reactivex.Observable
import com.noordwind.apps.collectively.data.model.User

interface UserDataSource {
    fun loadUser(userName: String): Observable<User>
}
