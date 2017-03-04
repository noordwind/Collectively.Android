package pl.adriankremski.coolector.authentication.signup

import io.reactivex.Observable
import pl.adriankremski.coolector.model.Profile
import pl.adriankremski.coolector.repository.ProfileRepository

class LoadProfileUseCase(val profileRepository: ProfileRepository) {
    fun loadProfile(): Observable<Profile> = profileRepository.loadProfile()
}

