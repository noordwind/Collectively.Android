package pl.adriankremski.collectively.data.datasource

import io.reactivex.Observable
import pl.adriankremski.collectively.data.model.Profile

interface ProfileDataSource{
    fun profile() : Observable<Profile>
}

