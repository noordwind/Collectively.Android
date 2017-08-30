package com.noordwind.apps.collectively.data.datasource

import android.app.Service
import android.content.Intent
import android.net.Uri
import android.os.IBinder
import android.widget.Toast
import com.noordwind.apps.collectively.Constants
import com.noordwind.apps.collectively.TheApp
import com.noordwind.apps.collectively.data.model.Operation
import com.noordwind.apps.collectively.data.repository.util.OperationRepository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class UploadRemarkPhotoService : Service() {

    @Inject
    lateinit var remarksDataSource: RemarksDataSource

    @Inject
    lateinit var fileDataSource: FileDataSource

    @Inject
    lateinit var operationRepository: OperationRepository

    var disposables: CompositeDisposable? = null

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        TheApp[baseContext]!!.appComponent!!.inject(this)
        disposables = CompositeDisposable()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        intent?.let {
            var id = intent.getStringExtra(Constants.BundleKey.REMARK_ID)
            var imageUri = intent.getParcelableExtra<Uri>(Constants.BundleKey.REMARK_PHOTO_URI)

            Toast.makeText(baseContext, "Uploading photo " + id, Toast.LENGTH_LONG).show()

            var scaleImageObservable = fileDataSource.scaledImageFile(imageUri)
            var uploadImageObservable = scaleImageObservable.flatMap {
                remarksDataSource.uploadRemarkPhoto(id, it)
            }

            operationRepository.pollOperation(uploadImageObservable).onErrorReturn {
                Operation("", true, id, "", "")
            }.flatMap {
                remarksDataSource.loadRemarkPreview(id)
            }.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                            {
                                Toast.makeText(baseContext, "Photo uploaded" + id, Toast.LENGTH_LONG).show()
                            },
                            {
                                Toast.makeText(baseContext, "Photo uploading error" + id, Toast.LENGTH_LONG).show()
                            })

        }
        return START_NOT_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        disposables?.let(CompositeDisposable::dispose)
        disposables = null
    }

}

