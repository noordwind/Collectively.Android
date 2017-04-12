package pl.adriankremski.collectively.data.datasource

import io.reactivex.Observable
import pl.adriankremski.collectively.data.net.Api
import pl.adriankremski.collectively.data.model.Profile

class ProfileDataSourceImpl(val api: Api) : ProfileDataSource{

    override fun profile(): Observable<Profile>  = api.loadProfile()
}

