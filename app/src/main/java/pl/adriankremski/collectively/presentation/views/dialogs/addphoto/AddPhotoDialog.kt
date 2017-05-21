package pl.adriankremski.collectively.presentation.views.dialogs.mapfilters

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import pl.adriankremski.collectively.Constants
import pl.adriankremski.collectively.R
import pl.adriankremski.collectively.presentation.rxjava.RxBus

class AddPhotoDialog : DialogFragment(), Constants {
    companion object {
        fun newInstance(): AddPhotoDialog = AddPhotoDialog()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NO_TITLE, android.R.style.Theme_Holo_Light_Dialog)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val rootView = inflater!!.inflate(R.layout.fragment_add_photo_dialog, container, false)
        rootView.findViewById(R.id.cameraButton).setOnClickListener {
            RxBus.instance.postEvent(Constants.RxBusEvent.CAMERA_EVENT)
            dialog.dismiss()
        }
        rootView.findViewById(R.id.galleryButton).setOnClickListener {
            RxBus.instance.postEvent(Constants.RxBusEvent.GALLERY_EVENT)
            dialog.dismiss()
        }
        return rootView
    }
    override fun onStart() {
        super.onStart()
        val d = dialog
        dialog.setCanceledOnTouchOutside(true)
        if (d != null) {
            val width = ViewGroup.LayoutParams.MATCH_PARENT
            val height = ViewGroup.LayoutParams.WRAP_CONTENT
            d.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            d.window!!.setLayout(width, height)
        }
    }
}
