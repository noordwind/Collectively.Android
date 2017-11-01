package com.noordwind.apps.collectively.presentation.extension

import android.annotation.SuppressLint
import android.content.ContentUris
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.DocumentsContract
import android.provider.MediaStore
import java.io.*


/**
 * Created by adriankremski on 06/09/17.
 */
@SuppressLint("NewApi")
        /**
         * Get a file path from a Uri. This will get the the path for Storage Access
         * Framework Documents, as well as the _data field for the MediaStore and
         * other file-based ContentProviders.

         * @param context The context.
         * *
         * @param uri The Uri to query.
         * *
         * @author paulburke
         */
fun Uri.getFilePath(context: Context, uri: Uri): String? {

    val isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT

    // DocumentProvider
    if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
        // ExternalStorageProvider
        if (isExternalStorageDocument(uri)) {
            val docId = DocumentsContract.getDocumentId(uri)
            val split = docId.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            val type = split[0]

            if ("primary".equals(type, ignoreCase = true)) {
                return Environment.getExternalStorageDirectory().absolutePath + "/" + split[1]
            }

            // TODO handle non-primary volumes
        } else if (isDownloadsDocument(uri)) {

            val id = DocumentsContract.getDocumentId(uri)
            val contentUri = ContentUris.withAppendedId(
                    Uri.parse("content://downloads/public_downloads"), java.lang.Long.valueOf(id)!!)

            return getDataColumn(context, contentUri, null, null)
        } else if (isMediaDocument(uri)) {
            val docId = DocumentsContract.getDocumentId(uri)
            val split = docId.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            val type = split[0]

            var contentUri: Uri? = null
            if ("image" == type) {
                contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            } else if ("video" == type) {
                contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI
            } else if ("audio" == type) {
                contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
            }

            val selection = "_id=?"
            val selectionArgs = arrayOf(split[1])

            return getDataColumn(context, contentUri!!, selection, selectionArgs)
        }// MediaProvider
        // DownloadsProvider
    } else if ("content".equals(uri.getScheme(), ignoreCase = true)) {
//        return getDataColumn(context, uri, null, null)
        return getImagePathFromInputStreamUri(context, uri)
    } else if ("file".equals(uri.getScheme(), ignoreCase = true)) {
        return uri.getPath()
    }// File
    // MediaStore (and general)

    return null
}

/**
 * Get the value of the data column for this Uri. This is useful for
 * MediaStore Uris, and other file-based ContentProviders.

 * @param context The context.
 * *
 * @param uri The Uri to query.
 * *
 * @param selection (Optional) Filter used in the query.
 * *
 * @param selectionArgs (Optional) Selection arguments used in the query.
 * *
 * @return The value of the _data column, which is typically a file path.
 */
fun getDataColumn(context: Context, uri: Uri, selection: String?,
                  selectionArgs: Array<String>?): String? {

    var cursor: Cursor? = null
    val column = "_data"
    val projection = arrayOf(column)

    try {
        cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null)
        if (cursor != null && cursor!!.moveToFirst()) {
            val column_index = cursor!!.getColumnIndexOrThrow(column)
            return cursor!!.getString(column_index)
        }
    } finally {
        if (cursor != null)
            cursor!!.close()
    }
    return null
}


/**
 * @param uri The Uri to check.
 * *
 * @return Whether the Uri authority is ExternalStorageProvider.
 */
fun isExternalStorageDocument(uri: Uri): Boolean {
    return "com.android.externalstorage.documents" == uri.getAuthority()
}

/**
 * @param uri The Uri to check.
 * *
 * @return Whether the Uri authority is DownloadsProvider.
 */
fun isDownloadsDocument(uri: Uri): Boolean {
    return "com.android.providers.downloads.documents" == uri.getAuthority()
}

/**
 * @param uri The Uri to check.
 * *
 * @return Whether the Uri authority is MediaProvider.
 */
fun isMediaDocument(uri: Uri): Boolean {
    return "com.android.providers.media.documents" == uri.getAuthority()
}


fun getImagePathFromInputStreamUri(context: Context, uri: Uri): String? {
    var inputStream: InputStream? = null
    var filePath: String? = null

    if (uri.authority != null) {
        try {
            inputStream = context.getContentResolver().openInputStream(uri) // context needed
            val photoFile = createTemporalFileFrom(context, inputStream)

            filePath = photoFile?.getPath()
        } catch (e: FileNotFoundException) {
            // log
        } catch (e: IOException) {
            // log
        } finally {
            try {
                inputStream!!.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }

        }
    }

    return filePath
}

@Throws(IOException::class)
private fun createTemporalFileFrom(context: Context, inputStream: InputStream?): File? {
    var targetFile: File? = null

    if (inputStream != null) {
        var read: Int = 0
        val buffer = ByteArray(8 * 1024)

        targetFile = createTemporalFile(context)
        val outputStream = FileOutputStream(targetFile)

        read = inputStream!!.read(buffer)
        while ((read) != -1) {
            outputStream.write(buffer, 0, read)
            read = inputStream!!.read(buffer)
        }
        outputStream.flush()

        try {
            outputStream.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }

    }

    return targetFile
}

private fun createTemporalFile(context: Context): File {
    return File(context.getExternalCacheDir(), "tempFile.jpg") // context needed
}