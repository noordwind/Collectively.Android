package com.noordwind.apps.collectively.data.datasource

import android.app.Service
import android.content.Intent
import android.net.Uri
import android.os.IBinder
import android.widget.Toast
import com.noordwind.apps.collectively.Constants
import com.noordwind.apps.collectively.data.repository.util.OperationRepository
import javax.inject.Inject

class UploadRemarkPhotoService : Service() {

    @Inject
    lateinit var remarkDataSource: RemarksDataSource

    @Inject
    lateinit var operationRepository: OperationRepository

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        intent?.let {
            var id = intent.getStringExtra(Constants.BundleKey.REMARK_ID)
            var imageUri = intent.getParcelableExtra<Uri>(Constants.BundleKey.REMARK_PHOTO_URI)

            Toast.makeText(baseContext, "Uploading photo " + id, Toast.LENGTH_LONG).show()
//        operationRepository.pollOperation(remarksDataSource.uploadRemarkPhoto(it.id, fileDataSource.fileFromUri(remark.imageUri!!))).onErrorReturn {
//            Operation("", true, remarkId!!, "", "")
//        }
        }
        return START_NOT_STICKY
    }

}

