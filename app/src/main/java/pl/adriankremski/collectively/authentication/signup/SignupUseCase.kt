package pl.adriankremski.collectively.authentication.signup

import io.reactivex.Observable
import pl.adriankremski.collectively.repository.AuthenticationRepository
import pl.adriankremski.collectively.repository.SessionRepository

class SignUpUseCase(val authenticationRepository: AuthenticationRepository, val sessionRepository: SessionRepository) {
    fun signUp(username: String, email: String, password: String): Observable<String>
            = authenticationRepository.signUp(username, email, password).doOnNext { sessionRepository.sessionToken = it }
}

