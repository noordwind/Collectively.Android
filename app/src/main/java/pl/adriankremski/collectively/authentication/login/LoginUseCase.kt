package pl.adriankremski.collectively.authentication.login

import io.reactivex.Observable
import pl.adriankremski.collectively.repository.AuthenticationRepository
import pl.adriankremski.collectively.repository.SessionRepository

class LoginUseCase(val authenticationRepository: AuthenticationRepository, val sessionRepository: SessionRepository) {

    fun isLoggedIn(): Boolean = sessionRepository.isLoggedIn

    fun loginWithEmail(email: String, password: String): Observable<String> =
            authenticationRepository.loginWithEmail(email, password).doOnNext { sessionRepository.sessionToken = it }
}

