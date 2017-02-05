package pl.adriankremski.coolector.network

import io.reactivex.Observable
import pl.adriankremski.coolector.model.AuthRequest
import pl.adriankremski.coolector.model.AuthResponse
import pl.adriankremski.coolector.model.SignUpRequest
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface Api {

    @Headers("Accept: application/json", "Content-type: application/json")
    @POST("api/sign-in")
    fun login(@Body authRequest: AuthRequest): Observable<AuthResponse>

    @Headers("Accept: application/json", "Content-type: application/json")
    @POST("api/sign-up")
    fun signUp(@Body authRequest: SignUpRequest): Observable<Void>
}
