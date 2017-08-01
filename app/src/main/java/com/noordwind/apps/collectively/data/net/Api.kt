package com.noordwind.apps.collectively.data.net

import com.noordwind.apps.collectively.Constants
import com.noordwind.apps.collectively.data.model.*
import io.reactivex.Observable
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.*

interface Api {

    // GET

    // USERS
    @Headers(Constants.ApiHeader.ACCEPT_HEADER, Constants.ApiHeader.CONTENT_TYPE_HEADER)
    @GET("users/{name}")
    fun loadUser(@Path("name") userName: String): Observable<User>

    @Headers(Constants.ApiHeader.ACCEPT_HEADER, Constants.ApiHeader.CONTENT_TYPE_HEADER)
    @GET("users")
    fun loadUsers(@Query("page") pageNumber: String, @Query("results") results: String): Observable<List<User>>

    //REMARKS

    @Headers(Constants.ApiHeader.ACCEPT_HEADER, Constants.ApiHeader.CONTENT_TYPE_HEADER)
    @GET("remarks")
    fun remarks(
            @Query("authorId") authorId: String?,
            @Query("states") state: String,
            @Query("categories") categories: List<String>,
            @Query("latest") latest: Boolean,
            @Query("orderBy") orderBy: String,
            @Query("sortorder") sortorder: String,
            @Query("results") results: Int): Observable<List<Remark>>

    @Multipart
    @PUT("remarks/{remarkId}/photo")
    fun uploadRemarkPhoto(@Path("remarkId") remarkId: String, @Part photo: MultipartBody.Part): Observable<Response<Void>>

    @Headers(Constants.ApiHeader.ACCEPT_HEADER, Constants.ApiHeader.CONTENT_TYPE_HEADER)
    @GET("remarks")
    fun userRemarks(@Query("authorId") authorId: String, @Query("results") results: Int): Observable<List<Remark>>

    @Headers(Constants.ApiHeader.ACCEPT_HEADER, Constants.ApiHeader.CONTENT_TYPE_HEADER)
    @GET("remarks")
    fun userFavoriteRemarks(@Query("userFavorites") userId: String, @Query("authorId") authorId: String, @Query("results") results: Int): Observable<List<Remark>>

    @Headers(Constants.ApiHeader.ACCEPT_HEADER, Constants.ApiHeader.CONTENT_TYPE_HEADER)
    @GET("remarks")
    fun userResolvedRemarks(@Query("resolverId") userId: String, @Query("authorId") authorId: String, @Query("results") results: Int): Observable<List<Remark>>

    @Headers(Constants.ApiHeader.ACCEPT_HEADER, Constants.ApiHeader.CONTENT_TYPE_HEADER)
    @GET("remarks/{id}")
    fun remarkPreview(@Path("id") remarkId: String): Observable<RemarkPreview>

    @Headers(Constants.ApiHeader.ACCEPT_HEADER, Constants.ApiHeader.CONTENT_TYPE_HEADER)
    @GET("{path}")
    fun createdRemark(@Path(value = "path", encoded = true) resourcePath: String): Observable<RemarkNotFromList>

    //REMARK CATEGORIES
    @Headers(Constants.ApiHeader.ACCEPT_HEADER, Constants.ApiHeader.CONTENT_TYPE_HEADER)
    @GET("remarks/categories")
    fun remarkCategories(): Observable<List<RemarkCategory>>

    //REMARK TAGS
    @Headers(Constants.ApiHeader.ACCEPT_HEADER, Constants.ApiHeader.CONTENT_TYPE_HEADER)
    @GET("remarks/tags")
    fun remarkTags(): Observable<List<RemarkTag>>

    //OPERATIONS
    @Headers(Constants.ApiHeader.ACCEPT_HEADER, Constants.ApiHeader.CONTENT_TYPE_HEADER)
    @GET("{path}")
    fun operation(@Path(value = "path", encoded = true) operationPath: String): Observable<Operation>

    //STATISTICS
    @Headers(Constants.ApiHeader.ACCEPT_HEADER, Constants.ApiHeader.CONTENT_TYPE_HEADER)
    @GET("statistics/categories")
    fun loadCategoriesStatistics(): Observable<List<StatisticEntry>>

    @Headers(Constants.ApiHeader.ACCEPT_HEADER, Constants.ApiHeader.CONTENT_TYPE_HEADER)
    @GET("statistics/tags")
    fun loadTagStatistics(): Observable<List<StatisticEntry>>

    @Headers(Constants.ApiHeader.ACCEPT_HEADER, Constants.ApiHeader.CONTENT_TYPE_HEADER)
    @GET("statistics/users")
    fun loadUsersStatistics(): Observable<List<UserStatisticsEntry>>

    //ACCOUNT
    @Headers(Constants.ApiHeader.ACCEPT_HEADER, Constants.ApiHeader.CONTENT_TYPE_HEADER)
    @GET("account")
    fun loadProfile(): Observable<Profile>

    @Headers(Constants.ApiHeader.ACCEPT_HEADER, Constants.ApiHeader.CONTENT_TYPE_HEADER)
    @GET("account/settings/notifications")
    fun settings(): Observable<Settings>

    // POST
    @Headers(Constants.ApiHeader.ACCEPT_HEADER, Constants.ApiHeader.CONTENT_TYPE_HEADER)
    @POST("sign-in")
    fun login(@Body authRequest: AuthRequest): Observable<AuthResponse>

    @Headers(Constants.ApiHeader.ACCEPT_HEADER, Constants.ApiHeader.CONTENT_TYPE_HEADER)
    @POST("sign-in")
    fun facebookLogin(@Body authRequest: FacebookAuthRequest): Observable<AuthResponse>

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
    fun submitRemarkComment(@Path("id") remarkId: String, @Body remarkComment: RemarkCommentPostRequest): Observable<Response<Void>>

    //PUT
    @Headers(Constants.ApiHeader.ACCEPT_HEADER, Constants.ApiHeader.CONTENT_TYPE_HEADER)
    @PUT("remarks/{id}/votes")
    fun submitRemarkVote(@Path("id") remarkId: String, @Body remarkVote: RemarkVote): Observable<Response<Void>>

    @Headers(Constants.ApiHeader.ACCEPT_HEADER, Constants.ApiHeader.CONTENT_TYPE_HEADER)
    @PUT("remarks/{remarkId}/comments/{commentId}/votes")
    fun submitRemarkCommentVote(@Path("remarkId") remarkId: String, @Path("commentId") commentId: String, @Body remarkVote: RemarkVote): Observable<Response<Void>>

    @Headers(Constants.ApiHeader.ACCEPT_HEADER, Constants.ApiHeader.CONTENT_TYPE_HEADER)
    @PUT("account/settings/notifications")
    fun saveSettings(@Body settings: Settings): Observable<Response<Void>>

    @Headers(Constants.ApiHeader.ACCEPT_HEADER, Constants.ApiHeader.CONTENT_TYPE_HEADER)
    @PUT("account/password")
    fun changePassword(@Body body: ChangePasswordRequest): Observable<Response<Void>>

    //DELETE
    @Headers(Constants.ApiHeader.ACCEPT_HEADER)
    @DELETE("remarks/{id}/votes")
    fun deleteRemarkVote(@Path("id") remarkId: String): Observable<Response<Void>>

    @Headers(Constants.ApiHeader.ACCEPT_HEADER)
    @DELETE("remarks/{remarkId}/comments/{commentId}/votes")
    fun deleteRemarkCommentVote(@Path("remarkId") remarkId: String, @Path("commentId") commentId: String): Observable<Response<Void>>
}
