package com.noordwind.apps.collectively.data.repository

import com.noordwind.apps.collectively.Constants
import com.noordwind.apps.collectively.data.datasource.AuthDataSource
import com.noordwind.apps.collectively.data.datasource.MapFiltersRepository
import com.noordwind.apps.collectively.data.model.*
import com.noordwind.apps.collectively.data.repository.util.OperationRepository
import com.noordwind.apps.collectively.domain.repository.SessionRepository
import io.reactivex.Observable
import io.reactivex.functions.BiFunction

class AuthenticationRepositoryImpl(val authDataSource: AuthDataSource,
                                   val mapFiltersRepository: MapFiltersRepository,
                                   val profileRepository: ProfileRepository,
                                   val userGroupsRepository: UserGroupsRepository,
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

                    mapFiltersRepository.reset()

                    var profileObs = profileRepository.loadProfile(true)
                    var userGroupsObs = userGroupsRepository.loadGroups(true)

                    Observable.zip(profileObs, userGroupsObs,
                            BiFunction<Profile, List<UserGroup>, String> { t1, t2 -> authResponse })
                }
    }

    override fun loginWithFacebookToken(token: String): Observable<Pair<Profile, String>> {
        val authRequest = FacebookAuthRequest(token, Constants.AuthProvider.FACEBOOK)

        return authDataSource.facebookLogin(authRequest)
                .flatMap {
                    authResponse ->
                    sessionRepository.sessionToken = authResponse.token

                    mapFiltersRepository.reset()

                    var profileObs = profileRepository.loadProfile(true)
                    var userGroupsObs = userGroupsRepository.loadGroups(true)

                    Observable.zip(profileObs, userGroupsObs,
                            BiFunction<Profile, List<UserGroup>, Pair<Profile, String>> { t1, t2 -> Pair(t1, authResponse.token) })
                }
    }

    override fun setNickName(name: String): Observable<Boolean> {
        return operationRepository.pollOperation(authDataSource.setNickName(SetNickNameRequest(name)))
                .flatMap {
                    profileRepository.loadProfile(true).flatMap {
                        Observable.just(true)
                    }
                }
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

    override fun deleteAccount(): Observable<Boolean> {
        return operationRepository.pollOperation(authDataSource.deleteAccount()).flatMap { Observable.just(true) }
    }
}

