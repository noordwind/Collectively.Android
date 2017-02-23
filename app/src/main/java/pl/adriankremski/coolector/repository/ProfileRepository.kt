package pl.adriankremski.coolector.repository

import io.reactivex.Observable
import pl.adriankremski.coolector.model.Profile

interface ProfileRepository {
    fun loadProfile(): Observable<Profile>
}

