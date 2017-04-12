package pl.adriankremski.collectively.presentation.views

import android.content.Context
import android.widget.TextView
import pl.adriankremski.collectively.R
import pl.adriankremski.collectively.data.model.RemarkTag
import pl.adriankremski.collectively.presentation.extension.setBackgroundCompat


class RemarkTagView(context: Context, tag: RemarkTag, useOnCLickListener: Boolean = false) : TextView(context) {

    public var isSelected: Boolean? = false

    init {
        if (useOnCLickListener) {
            setOnClickListener {
                isSelected = !isSelected!!

                if (isSelected as Boolean) {
                    setBackgroundCompat(R.drawable.remark_tag_selected_background)
                } else {
                    setBackgroundCompat(R.drawable.remark_tag_unselected_background)
                }
            }
        }
    }
}

