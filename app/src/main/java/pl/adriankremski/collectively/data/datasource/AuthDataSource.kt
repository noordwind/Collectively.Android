package pl.adriankremski.collectively.data.datasource

import io.reactivex.Observable
import pl.adriankremski.collectively.data.model.*
import retrofit2.Response

interface AuthDataSource {
    fun login(authRequest: AuthRequest): Observable<AuthResponse>
    fun facebookLogin(authRequest: FacebookAuthRequest): Observable<AuthResponse>
    fun signUp(authRequest: SignUpRequest): Observable<Response<Void>>
    fun resetPassword(body: ResetPasswordRequest): Observable<Response<Void>>
}
