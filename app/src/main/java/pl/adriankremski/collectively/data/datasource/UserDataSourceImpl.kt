package pl.adriankremski.collectively.data.datasource

import io.reactivex.Observable
import pl.adriankremski.collectively.data.model.*
import pl.adriankremski.collectively.data.net.Api
import retrofit2.Response

class UserDataSourceImpl(val api: Api) : UserDataSource {
    override fun loadUser(userName: String): Observable<User> = api.loadUser(userName)
}

