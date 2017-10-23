package com.noordwind.apps.collectively.data.repository

import com.noordwind.apps.collectively.data.model.Profile
import io.reactivex.Observable

interface AuthenticationRepository {
    fun signUp(username: String, email: String, password: String): Observable<String>
    fun loginWithEmail(email: String, password: String): Observable<String>
    fun loginWithFacebookToken(token: String): Observable<Pair<Profile, String>>
    fun resetPassword(email: String): Observable<Boolean>
    fun changePassword(currentPassword: String, newPassword: String): Observable<Boolean>
    fun setNickName(name: String): Observable<Boolean>
    fun deleteAccount(): Observable<Boolean>
}
