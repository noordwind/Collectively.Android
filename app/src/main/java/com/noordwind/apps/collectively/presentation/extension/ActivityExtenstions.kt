package com.noordwind.apps.collectively.presentation.extension

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import com.noordwind.apps.collectively.Constants
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*


@Throws(IOException::class)
fun Activity.openGalleryPicker(): Uri {
    val tempFileFromSource = File.createTempFile("choose", ".png", externalCacheDir)
    val tempUriFromSource = Uri.fromFile(tempFileFromSource)
    val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
    intent.type = "image/*"
    intent.putExtra(MediaStore.EXTRA_OUTPUT, tempUriFromSource)
    startActivityForResult(intent, Constants.RequestCodes.PICK_PICTURE_FROM_GALLERY)
    return tempUriFromSource
}

fun Activity.openCamera(): Uri? {
    val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

    if (intent.resolveActivity(packageManager) != null) {
        var photoFile: File? = null;
        try {
            photoFile = createImageFile();
        } catch (ex: IOException) {
        }

        var outputFileUri = Uri.fromFile(photoFile);

        photoFile.let {
            intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri)
            startActivityForResult(intent, Constants.RequestCodes.TAKE_PICTURE)
            return outputFileUri
        }
    }
    return null

}


@Throws(IOException::class)
fun Activity.createImageFile(): File {
    // Create an image file name
    val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
    val imageFileName = "JPEG_" + timeStamp + "_"
    val storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
    val image = File.createTempFile(
            imageFileName, /* prefix */
            ".jpg", /* suffix */
            storageDir      /* directory */
    )
    return image
}