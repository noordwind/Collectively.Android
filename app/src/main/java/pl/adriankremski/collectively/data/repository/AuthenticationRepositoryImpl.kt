package pl.adriankremski.collectively.data.repository

import io.reactivex.Observable
import pl.adriankremski.collectively.Constants
import pl.adriankremski.collectively.data.datasource.AuthDataSource
import pl.adriankremski.collectively.data.model.*
import pl.adriankremski.collectively.data.repository.util.OperationRepository

class AuthenticationRepositoryImpl(val authDataSource: AuthDataSource,
                                   val profileRepository: ProfileRepository,
                                   val operationRepository: OperationRepository,
                                   val sessionRepository: SessionRepository) : AuthenticationRepository {

    override fun loginWithEmail(email: String, password: String): Observable<String> {
        val authRequest = AuthRequest(email, password, Constants.AuthProvider.COOLECTOR)

        return authDataSource.login(authRequest)
                .flatMap {
                    authResponse ->
                    Observable.just(authResponse.token)
                }
                .flatMap {
                    authResponse ->
                    sessionRepository.sessionToken = authResponse
                    Observable.just(authResponse)
                }
                .flatMap {
                    authResponse ->
                    profileRepository.loadProfile(true).flatMap {
                        Observable.just(authResponse)
                    }
                }
    }

    override fun loginWithFacebookToken(token: String): Observable<String> {
        val authRequest = FacebookAuthRequest(token, Constants.AuthProvider.FACEBOOK)

        return authDataSource.facebookLogin(authRequest)
                .flatMap { authResponse -> Observable.just(authResponse.token) }
                .doOnNext { sessionRepository.sessionToken = it }
    }

    override fun signUp(username: String, email: String, password: String): Observable<String> {
        return operationRepository.pollOperation(authDataSource.signUp(SignUpRequest(username, email, password)))
                .flatMap { loginWithEmail(email, password) }
    }

    override fun resetPassword(email: String): Observable<Boolean> {
        return operationRepository.pollOperation(authDataSource.resetPassword(ResetPasswordRequest(email))).flatMap { Observable.just(true) }
    }

    override fun changePassword(currentPassword: String, newPassword: String): Observable<Boolean> {
        return operationRepository.pollOperation(
                authDataSource.chanePassword(ChangePasswordRequest(currentPassword, newPassword)))
                .flatMap { Observable.just(true) }
    }
}

