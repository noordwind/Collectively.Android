package pl.adriankremski.collectively.data.repository

import io.reactivex.Observable
import pl.adriankremski.collectively.data.datasource.UserDataSource
import pl.adriankremski.collectively.data.model.User

class UsersRepositoryImpl(val userDataSource: UserDataSource) : UsersRepository {
    override fun loadUser(userName: String): Observable<User> = userDataSource.loadUser(userName)
}

