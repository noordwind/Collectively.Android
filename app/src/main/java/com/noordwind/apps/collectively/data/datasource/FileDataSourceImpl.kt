package com.noordwind.apps.collectively.data.datasource

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.net.Uri
import android.os.Build
import android.support.media.ExifInterface
import com.noordwind.apps.collectively.presentation.extension.getFilePath
import io.reactivex.Observable
import java.io.*
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
                selectedBitmap.recycle()

                it.onNext(file)
                it.onComplete()
            } catch (e: Exception) {
                it.onError(e)
            }
        }
    }

    @Throws(IOException::class)
    private fun rotateImageIfRequired(context: Context, img: Bitmap, selectedImage: Uri): Bitmap {
        val input = context.contentResolver.openInputStream(selectedImage)
        val ei: ExifInterface
        if (Build.VERSION.SDK_INT > 23)
            ei = ExifInterface(input)
        else
            ei = ExifInterface(selectedImage.path)

        val orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL)

        when (orientation) {
            ExifInterface.ORIENTATION_ROTATE_90 -> return rotateImage(img, 90)
            ExifInterface.ORIENTATION_ROTATE_180 -> return rotateImage(img, 180)
            ExifInterface.ORIENTATION_ROTATE_270 -> return rotateImage(img, 270)
            else -> return img
        }
    }

    private fun rotateImage(img: Bitmap, degree: Int): Bitmap {
        val matrix = Matrix()
        matrix.postRotate(degree.toFloat())
        val rotatedImg = Bitmap.createBitmap(img, 0, 0, img.width, img.height, matrix, true)
        img.recycle()
        return rotatedImg
    }

    private fun inputStreamFromUri(uri: Uri): InputStream {
        return FileInputStream(File(uri.getFilePath(context, uri)))
    }

    override fun fileFromUri(uri: Uri): File = File(uri.path)
}
