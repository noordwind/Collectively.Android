package pl.adriankremski.collectively.repository

import android.content.Context
import io.reactivex.Observable
import pl.adriankremski.collectively.TheApp
import pl.adriankremski.collectively.model.Profile
import pl.adriankremski.collectively.network.Api
import javax.inject.Inject

class ProfileRepositoryImpl(context: Context) : ProfileRepository {
    @Inject
    lateinit var api: Api

    init {
        TheApp[context].appComponent?.inject(this)
    }

    override fun loadProfile(): Observable<Profile> = api.loadProfile()
}

