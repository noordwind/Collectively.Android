package pl.adriankremski.collectively.data.repository

import io.reactivex.Observable
import pl.adriankremski.collectively.data.datasource.ProfileDataSource
import pl.adriankremski.collectively.data.model.Profile

class ProfileRepositoryImpl(val profileDataSource: ProfileDataSource) : ProfileRepository {
    override fun loadProfile(): Observable<Profile> = profileDataSource.profile()
}

