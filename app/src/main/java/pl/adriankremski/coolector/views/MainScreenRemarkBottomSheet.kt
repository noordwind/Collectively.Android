package pl.adriankremski.coolector.views

import android.content.Context
import android.view.View
import android.widget.FrameLayout
import android.widget.TextView
import pl.adriankremski.coolector.R
import pl.adriankremski.coolector.model.Remark

class MainScreenRemarkBottomSheet(context: Context?, val mRemark: Remark) : FrameLayout(context) {

    init {
        View.inflate(getContext(), R.layout.view_main_screen_remark_bottom_sheet, this)
        var mNameLabel: TextView = findViewById(R.id.name) as TextView
        var mAddressLabel: TextView = findViewById(R.id.address) as TextView
        mNameLabel.text = mRemark.description
        mAddressLabel.text = mRemark.location?.address
    }
}
