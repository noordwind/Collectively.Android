package pl.adriankremski.coolector.authentication.retrievepassword

import io.reactivex.Observable
import pl.adriankremski.coolector.repository.AuthenticationRepository

class RetrievePasswordUseCase(val authenticationRepository: AuthenticationRepository) {
    fun resetPassword(email: String): Observable<Boolean> = authenticationRepository.resetPassword(email)
}

