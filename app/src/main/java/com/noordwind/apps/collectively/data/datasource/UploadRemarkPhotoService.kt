package com.noordwind.apps.collectively.data.datasource

import android.app.Service
import android.content.Intent
import android.net.Uri
import android.os.IBinder
import com.noordwind.apps.collectively.Constants
import com.noordwind.apps.collectively.R
import com.noordwind.apps.collectively.TheApp
import com.noordwind.apps.collectively.data.model.Operation
import com.noordwind.apps.collectively.data.repository.util.BarNotificationRepository
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

    @Inject
    lateinit var barNotificationRepository: BarNotificationRepository

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

            var scaleImageObservable = fileDataSource.scaledImageFile(imageUri)
            var uploadImageObservable = scaleImageObservable.flatMap {
                remarksDataSource.uploadRemarkPhoto(id, it)
            }

            barNotificationRepository.showProgressNotification(
                    tag = id,
                    smallIconResourceId = R.drawable.ic_place_white_24dp,
                    title = baseContext.getString(R.string.uploading_remark_photo),
                    message = null)

            operationRepository.pollOperation(uploadImageObservable).onErrorReturn {
                Operation("", true, id, "", "")
            }.flatMap {
                remarksDataSource.loadRemarkPreview(id)
            }.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                            {
                                barNotificationRepository.removeNotification(id)
                                barNotificationRepository.showNotification(
                                        tag = id,
                                        smallIconResourceId = R.drawable.ic_place_white_24dp,
                                        title = baseContext.getString(R.string.remark_photo_uploaded_title),
                                        message = baseContext.getString(R.string.remark_photo_uploaded_message))
                            },
                            {
                                barNotificationRepository.showNotification(
                                        tag = id,
                                        smallIconResourceId = R.drawable.ic_place_white_24dp,
                                        title = baseContext.getString(R.string.error_uploading_remark_photo_title),
                                        message = baseContext.getString(R.string.error_uploading_remark_photo_message),
                                        isHeadsUp = true)
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

