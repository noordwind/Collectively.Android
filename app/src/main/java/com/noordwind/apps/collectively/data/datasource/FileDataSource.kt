package com.noordwind.apps.collectively.data.datasource

import android.net.Uri
import io.reactivex.Observable
import java.io.File

interface FileDataSource {
    fun fileFromUri(uri: Uri): Observable<File>
}
