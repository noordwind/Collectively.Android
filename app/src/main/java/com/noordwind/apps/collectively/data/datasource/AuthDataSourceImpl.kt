package com.noordwind.apps.collectively.data.datasource

import com.noordwind.apps.collectively.data.model.*
import com.noordwind.apps.collectively.data.net.Api
import io.reactivex.Observable
import retrofit2.Response

class AuthDataSourceImpl(val api: Api) : AuthDataSource {
    override fun deleteAccount(): Observable<Response<Void>> = api.deleteAccount()

    override fun setNickName(request: SetNickNameRequest): Observable<Response<Void>> = api.setNickName(request)

    override fun facebookLogin(authRequest: FacebookAuthRequest): Observable<AuthResponse> = api.facebookLogin(authRequest)

    override fun signUp(authRequest: SignUpRequest): Observable<Response<Void>> = api.signUp(authRequest)

    override fun resetPassword(body: ResetPasswordRequest): Observable<Response<Void>> = api.resetPassword(body)

    override fun login(authRequest: AuthRequest): Observable<AuthResponse> = api.login(authRequest)

    override fun chanePassword(body: ChangePasswordRequest): Observable<Response<Void>> = api.changePassword(body)
}

