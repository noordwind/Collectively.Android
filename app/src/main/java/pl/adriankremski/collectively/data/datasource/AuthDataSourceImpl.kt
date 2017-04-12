package pl.adriankremski.collectively.data.datasource

import io.reactivex.Observable
import pl.adriankremski.collectively.data.net.Api
import pl.adriankremski.collectively.data.model.AuthRequest
import pl.adriankremski.collectively.data.model.AuthResponse
import pl.adriankremski.collectively.data.model.ResetPasswordRequest
import pl.adriankremski.collectively.data.model.SignUpRequest
import retrofit2.Response

class AuthDataSourceImpl(val api: Api) : AuthDataSource {

    override fun signUp(authRequest: SignUpRequest): Observable<Response<Void>> = api.signUp(authRequest)

    override fun resetPassword(body: ResetPasswordRequest): Observable<Response<Void>> = api.resetPassword(body)

    override fun login(authRequest: AuthRequest): Observable<AuthResponse> = api.login(authRequest)
}

