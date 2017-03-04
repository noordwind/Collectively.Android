package pl.adriankremski.collectively.repository

import io.reactivex.Observable
import pl.adriankremski.collectively.model.Profile

interface ProfileRepository {
    fun loadProfile(): Observable<Profile>
}

