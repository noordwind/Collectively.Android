package pl.adriankremski.coolector.repository

import android.content.Context
import io.reactivex.Observable
import pl.adriankremski.coolector.TheApp
import pl.adriankremski.coolector.model.Profile
import pl.adriankremski.coolector.network.Api
import javax.inject.Inject

class ProfileRepositoryImpl(context: Context) : ProfileRepository {
    @Inject
    lateinit var mApi: Api

    init {
        TheApp[context].appComponent?.inject(this)
    }

    override fun loadProfile(): Observable<Profile> = mApi.loadProfile()
}

