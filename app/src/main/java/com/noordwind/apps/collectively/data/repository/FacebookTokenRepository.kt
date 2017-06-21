package com.noordwind.apps.collectively.data.repository

import io.reactivex.Observable
import com.noordwind.apps.collectively.data.model.Optional

interface FacebookTokenRepository {
    val facebookToken: Observable<Optional<String>>
}
