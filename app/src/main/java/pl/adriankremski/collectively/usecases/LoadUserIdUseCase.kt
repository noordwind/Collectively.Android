package pl.adriankremski.collectively.usecases

import io.reactivex.Observable
import pl.adriankremski.collectively.data.repository.ProfileRepository

class LoadUserIdUseCase(val profileRepository: ProfileRepository) {
    fun userId() : Observable<String> = profileRepository.loadProfile().flatMap { Observable.just(it.userId) }
}

