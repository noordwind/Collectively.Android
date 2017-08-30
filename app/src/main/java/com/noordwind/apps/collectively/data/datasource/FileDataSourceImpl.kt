package com.noordwind.apps.collectively.data.datasource

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import io.reactivex.Observable
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.InputStream
import java.util.*

class FileDataSourceImpl(val context: Context) : FileDataSource {
    override fun scaledImageFile(uri: Uri): Observable<File> {
        return Observable.create<File> {
            try {
                // BitmapFactory options to downsize the image
                var options = BitmapFactory.Options();

                options.inJustDecodeBounds = true;
                options.inSampleSize = 6;
                // factor of downsizing the image

                var inputStream = inputStreamFromUri(uri)

                BitmapFactory.decodeStream(inputStream, null, options);
                inputStream.close();

                // The new size we want to scale to
                val REQUIRED_SIZE = 300

                // Find the correct scale value. It should be the power of 2.
                var scale = 1
                while (options.outWidth / scale / 2 >= REQUIRED_SIZE && options.outHeight / scale / 2 >= REQUIRED_SIZE) {
                    scale *= 2
                }

                var options2 = BitmapFactory.Options();
                options2.inSampleSize = scale;
                inputStream = inputStreamFromUri(uri)

                var selectedBitmap = BitmapFactory.decodeStream(inputStream, null, options2);
                inputStream.close();

                // here i override the original image file
                var file = File(context.filesDir, "remark_image_" + Date().time + ".jpeg");
                file.createNewFile();
                var outputStream = FileOutputStream(file);

                selectedBitmap.compress(Bitmap.CompressFormat.JPEG, 75, outputStream);

                it.onNext(file)
                it.onComplete()
            } catch (e: Exception) {
                it.onError(e)
            }
        }
    }

    private fun inputStreamFromUri(uri: Uri): InputStream {
        if (uri.path.contains("content")) {
            return context.contentResolver.openInputStream(uri)
        } else {
            return FileInputStream(fileFromUri(uri))
        }
    }

    override fun fileFromUri(uri: Uri): File = File(uri.path)
}
