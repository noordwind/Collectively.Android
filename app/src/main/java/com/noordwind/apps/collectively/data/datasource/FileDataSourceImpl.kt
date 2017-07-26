package com.noordwind.apps.collectively.data.datasource

import android.content.Context
import android.net.Uri
import java.io.File

class FileDataSourceImpl(val context: Context) : FileDataSource {
    override fun fileFromUri(uri: Uri): File = File(uri.path)
}
