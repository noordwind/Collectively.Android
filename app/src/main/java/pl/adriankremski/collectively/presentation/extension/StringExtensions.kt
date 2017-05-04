package pl.adriankremski.collectively.presentation.extension

import android.graphics.Color
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import pl.adriankremski.collectively.Constants
import pl.adriankremski.collectively.R


fun String.uppercaseFirstLetter(): String {
    if (this.length == 0) {
        return this
    } else {
        return this.substring(0, 1).toUpperCase() + this.substring(1, this.length)
    }
}

fun String.colorOfCategory(): String {
    return "#2C74DA"
}

fun String.iconOfCategory(): Int {
    when (this) {
        Constants.RemarkCategories.DEFECT -> {
            return R.drawable.defect_icon
        }
        Constants.RemarkCategories.ISSUE -> {
            return R.drawable.issue_icon
        }
        Constants.RemarkCategories.SUGGESTION -> {
            return R.drawable.suggestion_icon
        }
        Constants.RemarkCategories.PRAISE -> {
            return R.drawable.praise_icon
        }
        else -> {
            return R.drawable.issue_icon
        }
    }
}

fun String.toBitmapDescriptor(): BitmapDescriptor {
    val hsv = FloatArray(3)
    Color.colorToHSV(Color.parseColor(this), hsv)
    return BitmapDescriptorFactory.defaultMarker(hsv[0])
}
