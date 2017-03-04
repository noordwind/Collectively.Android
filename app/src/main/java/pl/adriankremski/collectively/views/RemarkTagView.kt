package pl.adriankremski.collectively.views

import android.content.Context
import android.widget.TextView
import pl.adriankremski.collectively.R
import pl.adriankremski.collectively.model.RemarkTag
import pl.adriankremski.collectively.utils.setBackgroundCompat


class RemarkTagView(context: Context, tag: RemarkTag) : TextView(context) {

    public var isSelected: Boolean? = false

    init {
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

