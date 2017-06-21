package pl.adriankremski.collectively.data.repository

import io.reactivex.Observable
import pl.adriankremski.collectively.data.model.User

interface UsersRepository {
    fun loadUser(userName: String): Observable<User>
}
