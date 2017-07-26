package com.noordwind.apps.collectively.presentation.views

import android.content.Context
import android.location.Location
import android.view.View
import android.widget.FrameLayout
import android.widget.TextView
import com.noordwind.apps.collectively.R
import com.noordwind.apps.collectively.data.model.Remark
import com.noordwind.apps.collectively.presentation.extension.getLongRemarkStateTranslation
import com.noordwind.apps.collectively.presentation.extension.hideIfEmptyText
import com.noordwind.apps.collectively.presentation.extension.uppercaseFirstLetter
import com.noordwind.apps.collectively.presentation.remarkpreview.RemarkActivity
import kotlinx.android.synthetic.main.view_main_screen_remark_bottom_sheet.view.*

class MainScreenRemarkBottomSheet(context: Context, remark: Remark, lastLocation: Location?, remarkLocation: Location) : FrameLayout(context) {

    init {
        View.inflate(getContext(), R.layout.view_main_screen_remark_bottom_sheet, this)
        var nameLabel: TextView = findViewById(R.id.name) as TextView
        var addressLabel: TextView = findViewById(R.id.address) as TextView
        var statusLabel: TextView = findViewById(R.id.statusLabel) as TextView

        nameLabel.text = remark.description
        nameLabel.hideIfEmptyText()

        addressLabel.text = remark.location?.address
        statusLabel.text = remark.state.state.getLongRemarkStateTranslation(context).uppercaseFirstLetter()

        setOnClickListener { RemarkActivity.start(context, remark.id) }
        lastLocation.let {
            val distanceInMeters = lastLocation!!.distanceTo(remarkLocation)
            distanceToRemarkLabel.text = distanceInMeters.toInt().toString() + "m"
        }
    }
}
