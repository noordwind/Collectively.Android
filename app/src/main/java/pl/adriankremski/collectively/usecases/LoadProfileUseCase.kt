package pl.adriankremski.collectively.profile

import io.reactivex.Observable
import pl.adriankremski.collectively.model.Profile
import pl.adriankremski.collectively.repository.ProfileRepository

class LoadProfileUseCase(val profileRepository: ProfileRepository) {
    fun loadProfile(): Observable<Profile> = profileRepository.loadProfile()
}

