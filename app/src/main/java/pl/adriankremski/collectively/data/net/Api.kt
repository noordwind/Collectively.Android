package pl.adriankremski.collectively.data.net

import io.reactivex.Observable
import pl.adriankremski.collectively.Constants
import pl.adriankremski.collectively.data.model.*
import retrofit2.Response
import retrofit2.http.*

interface Api {

    // GET
    @Headers(Constants.ApiHeader.ACCEPT_HEADER, Constants.ApiHeader.CONTENT_TYPE_HEADER)
    @GET("remarks/categories")
    fun remarkCategories(): Observable<List<RemarkCategory>>

    @Headers(Constants.ApiHeader.ACCEPT_HEADER, Constants.ApiHeader.CONTENT_TYPE_HEADER)
    @GET("remarks")
    fun remarks(@Query("latest") latest: Boolean, @Query("orderBy") orderBy: String, @Query("sortorder") sortorder: String, @Query("results") results: Int): Observable<List<Remark>>

    @Headers(Constants.ApiHeader.ACCEPT_HEADER, Constants.ApiHeader.CONTENT_TYPE_HEADER)
    @GET("remarks/tags")
    fun remarkTags(): Observable<List<RemarkTag>>

    @Headers(Constants.ApiHeader.ACCEPT_HEADER, Constants.ApiHeader.CONTENT_TYPE_HEADER)
    @GET("{path}")
    fun createdRemark(@Path(value = "path", encoded = true) resourcePath: String): Observable<RemarkNotFromList>

    @Headers(Constants.ApiHeader.ACCEPT_HEADER, Constants.ApiHeader.CONTENT_TYPE_HEADER)
    @GET("{path}")
    fun operation(@Path(value = "path", encoded = true) operationPath: String): Observable<Operation>

    @Headers(Constants.ApiHeader.ACCEPT_HEADER, Constants.ApiHeader.CONTENT_TYPE_HEADER)
    @GET("statistics/categories")
    fun loadCategoriesStatistics(): Observable<List<StatisticEntry>>

    @Headers(Constants.ApiHeader.ACCEPT_HEADER, Constants.ApiHeader.CONTENT_TYPE_HEADER)
    @GET("statistics/tags")
    fun loadTagStatistics(): Observable<List<StatisticEntry>>

    @Headers(Constants.ApiHeader.ACCEPT_HEADER, Constants.ApiHeader.CONTENT_TYPE_HEADER)
    @GET("account")
    fun loadProfile(): Observable<Profile>

    @Headers(Constants.ApiHeader.ACCEPT_HEADER, Constants.ApiHeader.CONTENT_TYPE_HEADER)
    @GET("remarks/{id}")
    fun remarkPreview(@Path("id") remarkId: String): Observable<RemarkPreview>

    // POST
    @Headers(Constants.ApiHeader.ACCEPT_HEADER, Constants.ApiHeader.CONTENT_TYPE_HEADER)
    @POST("sign-in")
    fun login(@Body authRequest: AuthRequest): Observable<AuthResponse>

    @Headers(Constants.ApiHeader.ACCEPT_HEADER, Constants.ApiHeader.CONTENT_TYPE_HEADER)
    @POST("sign-up")
    fun signUp(@Body authRequest: SignUpRequest): Observable<Response<Void>>

    @Headers(Constants.ApiHeader.ACCEPT_HEADER, Constants.ApiHeader.CONTENT_TYPE_HEADER)
    @POST("reset-password")
    fun resetPassword(@Body body: ResetPasswordRequest): Observable<Response<Void>>

    @Headers(Constants.ApiHeader.ACCEPT_HEADER, Constants.ApiHeader.CONTENT_TYPE_HEADER)
    @POST("remarks")
    fun saveRemark(@Body remark: NewRemark): Observable<Response<Void>>

    @Headers(Constants.ApiHeader.ACCEPT_HEADER, Constants.ApiHeader.CONTENT_TYPE_HEADER)
    @POST("remarks/{id}/comments")
    fun submitRemarkComment(@Path("id") remarkId: String, @Body remarkComment: RemarkComment): Observable<Response<Void>>

    //PUT
    @Headers(Constants.ApiHeader.ACCEPT_HEADER, Constants.ApiHeader.CONTENT_TYPE_HEADER)
    @PUT("remarks/{id}/votes")
    fun submitRemarkVote(@Path("id") remarkId: String, @Body remarkVote: RemarkVote): Observable<Response<Void>>

    //DELETE
    @Headers(Constants.ApiHeader.ACCEPT_HEADER)
    @DELETE("remarks/{id}/votes")
    fun deleteRemarkVote(@Path("id") remarkId: String): Observable<Response<Void>>
}
