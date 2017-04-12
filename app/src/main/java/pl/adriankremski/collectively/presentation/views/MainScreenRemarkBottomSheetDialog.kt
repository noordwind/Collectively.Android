package pl.adriankremski.collectively.presentation.views

import android.content.Context
import android.content.DialogInterface
import android.support.design.widget.BottomSheetDialog
import pl.adriankremski.collectively.data.model.Remark

class MainScreenRemarkBottomSheetDialog(context: Context, remark: Remark) {
    private val bottomSheetDialog: BottomSheetDialog = BottomSheetDialog(context)
    private val bottomSheetView: MainScreenRemarkBottomSheet = MainScreenRemarkBottomSheet(context, remark)

    init {
        bottomSheetDialog.setContentView(bottomSheetView)
    }

    fun setOnDismissListener(onDismissListener: DialogInterface.OnDismissListener) {
        bottomSheetDialog.setOnDismissListener(onDismissListener)
    }

    fun show() {
        bottomSheetDialog.show()
    }
}
