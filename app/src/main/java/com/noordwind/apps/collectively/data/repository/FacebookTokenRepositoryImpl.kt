package com.noordwind.apps.collectively.data.repository

import com.facebook.AccessToken
import io.reactivex.Observable
import com.noordwind.apps.collectively.data.model.Optional

class FacebookTokenRepositoryImpl : FacebookTokenRepository {
    override val facebookToken: Observable<Optional<String>>
        get() = if (currentAccessToken() == null) {
            Observable.just(Optional.empty())
        } else {
            Observable.just(Optional.of(currentAccessToken()!!.token))
        }

    fun currentAccessToken() : AccessToken? = AccessToken.getCurrentAccessToken()
}
