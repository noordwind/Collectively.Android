package pl.adriankremski.collectively.presentation.views

import android.content.Context
import android.view.View
import android.widget.FrameLayout
import android.widget.TextView
import pl.adriankremski.collectively.R
import pl.adriankremski.collectively.data.model.Remark
import pl.adriankremski.collectively.presentation.remarkpreview.RemarkActivity

class MainScreenRemarkBottomSheet(context: Context?, remark: Remark) : FrameLayout(context) {

    init {
        View.inflate(getContext(), R.layout.view_main_screen_remark_bottom_sheet, this)
        var nameLabel: TextView = findViewById(R.id.name) as TextView
        var addressLabel: TextView = findViewById(R.id.address) as TextView
        nameLabel.text = remark.description
        addressLabel.text = remark.location?.address
        setOnClickListener { RemarkActivity.start(context!!, remark.id) }
    }
}
