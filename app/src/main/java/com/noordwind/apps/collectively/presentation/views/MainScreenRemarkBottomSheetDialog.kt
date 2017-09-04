package com.noordwind.apps.collectively.presentation.views

import android.content.Context
import android.location.Location
import android.support.design.widget.BottomSheetDialog
import com.noordwind.apps.collectively.data.model.Remark

class MainScreenRemarkBottomSheetDialog(context: Context, remark: Remark, lastLocation: Location?, remarkLocation: Location) {
    private val bottomSheetDialog: BottomSheetDialog = BottomSheetDialog(context)
    private val bottomSheetView: MainScreenRemarkBottomSheet = MainScreenRemarkBottomSheet(context, remark, lastLocation, remarkLocation)

    init {
        bottomSheetDialog.setContentView(bottomSheetView)
    }

    fun show() : MainScreenRemarkBottomSheetDialog {
        bottomSheetDialog.show()
        return this
    }

    fun isVisible() : Boolean = bottomSheetDialog.isShowing

    fun hide() {
        bottomSheetDialog.hide()
    }
}
