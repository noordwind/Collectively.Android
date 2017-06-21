package com.noordwind.apps.collectively.data.datasource

import io.reactivex.Observable
import com.noordwind.apps.collectively.data.model.Profile

interface ProfileDataSource{
    fun profile() : Observable<Profile>
}

