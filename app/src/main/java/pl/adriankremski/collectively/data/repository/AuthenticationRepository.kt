package pl.adriankremski.collectively.data.repository

import io.reactivex.Observable

interface AuthenticationRepository {
    fun signUp(username: String, email: String, password: String): Observable<String>
    fun loginWithEmail(email: String, password: String): Observable<String>
    fun loginWithFacebookToken(token: String): Observable<String>
    fun resetPassword(email: String): Observable<Boolean>
    fun changePassword(currentPassword: String, newPassword: String): Observable<Boolean>
}
