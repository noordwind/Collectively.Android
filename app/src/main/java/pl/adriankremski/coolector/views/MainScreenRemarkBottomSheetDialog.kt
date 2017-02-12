package pl.adriankremski.coolector.views

import android.content.Context
import android.content.DialogInterface
import android.support.design.widget.BottomSheetDialog
import pl.adriankremski.coolector.model.Remark

class MainScreenRemarkBottomSheetDialog(context: Context, remark: Remark) {
    private val mBottomSheetDialog: BottomSheetDialog = BottomSheetDialog(context)
    private val mBottomSheetView: MainScreenRemarkBottomSheet = MainScreenRemarkBottomSheet(context, remark)

    init {
        mBottomSheetDialog.setContentView(mBottomSheetView)
    }

    fun setOnDismissListener(onDismissListener: DialogInterface.OnDismissListener) {
        mBottomSheetDialog.setOnDismissListener(onDismissListener)
    }

    fun show() {
        mBottomSheetDialog.show()
    }
}
