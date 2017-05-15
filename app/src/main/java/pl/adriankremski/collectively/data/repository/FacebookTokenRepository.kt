package pl.adriankremski.collectively.data.repository

import io.reactivex.Observable
import pl.adriankremski.collectively.data.model.Optional

interface FacebookTokenRepository {
    val facebookToken: Observable<Optional<String>>
}
