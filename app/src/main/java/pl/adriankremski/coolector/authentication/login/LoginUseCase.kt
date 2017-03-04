package pl.adriankremski.coolector.authentication.login

import io.reactivex.Observable
import pl.adriankremski.coolector.repository.AuthenticationRepository
import pl.adriankremski.coolector.repository.SessionRepository

class LoginUseCase(val authenticationRepository: AuthenticationRepository, val sessionRepository: SessionRepository) {

    fun isLoggedIn(): Boolean = sessionRepository.isLoggedIn

    fun loginWithEmail(email: String, password: String): Observable<String> =
            authenticationRepository.loginWithEmail(email, password).doOnNext { sessionRepository.sessionToken = it }
}

