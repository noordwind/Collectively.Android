package com.noordwind.apps.collectively.data.datasource

import com.noordwind.apps.collectively.data.model.*
import io.reactivex.Observable
import retrofit2.Response

interface AuthDataSource {
    fun login(authRequest: AuthRequest): Observable<AuthResponse>
    fun facebookLogin(authRequest: FacebookAuthRequest): Observable<AuthResponse>
    fun signUp(authRequest: SignUpRequest): Observable<Response<Void>>
    fun resetPassword(body: ResetPasswordRequest): Observable<Response<Void>>
    fun chanePassword(body: ChangePasswordRequest): Observable<Response<Void>>
    fun setNickName(request: SetNickNameRequest): Observable<Response<Void>>
    fun deleteAccount(): Observable<Response<Void>>
}
