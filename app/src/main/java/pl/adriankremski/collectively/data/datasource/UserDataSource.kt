package pl.adriankremski.collectively.data.datasource

import io.reactivex.Observable
import pl.adriankremski.collectively.data.model.User

interface UserDataSource {
    fun loadUser(userName: String): Observable<User>
}
