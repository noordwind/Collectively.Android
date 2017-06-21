package com.noordwind.apps.collectively.data.datasource

import android.content.Context
import android.net.Uri
import io.reactivex.Observable
import java.io.File

class FileDataSourceImpl(val context: Context) : FileDataSource {
    override fun fileFromUri(uri: Uri): Observable<File> = Observable.just(File(uri.path))
}
