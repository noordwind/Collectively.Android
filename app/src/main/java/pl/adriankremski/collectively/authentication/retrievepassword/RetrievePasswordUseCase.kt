package pl.adriankremski.collectively.authentication.retrievepassword

import io.reactivex.Observable
import pl.adriankremski.collectively.repository.AuthenticationRepository

class RetrievePasswordUseCase(val authenticationRepository: AuthenticationRepository) {
    fun resetPassword(email: String): Observable<Boolean> = authenticationRepository.resetPassword(email)
}

