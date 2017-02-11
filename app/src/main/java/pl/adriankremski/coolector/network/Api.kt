package pl.adriankremski.coolector.network

import io.reactivex.Observable
import pl.adriankremski.coolector.model.*
import retrofit2.http.*

interface Api {

    @Headers("Accept: application/json", "Content-type: application/json")
    @POST("api/sign-in")
    fun login(@Body authRequest: AuthRequest): Observable<AuthResponse>

    @Headers("Accept: application/json", "Content-type: application/json")
    @POST("api/sign-up")
    fun signUp(@Body authRequest: SignUpRequest): Observable<Void>

    @Headers("Accept: application/json", "Content-type: application/json")
    @GET("api/remarks/categories")
    fun remarkCategories(): Observable<List<RemarkCategory>>

    @Headers("Accept: application/json", "Content-type: application/json")
    @GET("api/remarks")
    fun remarks(@Query("latest") latest: Boolean): Observable<List<Remark>>

    @Headers("Accept: application/json", "Content-type: application/json")
    @GET("api/remarks/tags")
    fun remarkTags(): Observable<List<RemarkTag>>

    @Headers("Accept: application/json", "Content-type: application/json")
    @POST("api/reset-password")
    fun resetPassword(@Body body: ResetPasswordRequest): Observable<Void>
}
