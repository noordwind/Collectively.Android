package pl.adriankremski.collectively.data.repository

import io.reactivex.Observable
import pl.adriankremski.collectively.data.model.Profile

interface ProfileRepository {
    fun loadProfile(forceRefresh: Boolean): Observable<Profile>
}

