package pl.adriankremski.collectively.presentation.extension

import android.graphics.Color
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import pl.adriankremski.collectively.Constants


fun String.uppercaseFirstLetter(): String {
    if (this.length == 0) {
        return this
    } else {
        return this.substring(0, 1).toUpperCase() + this.substring(1, this.length)
    }
}

fun String.colorOfCategory(): String {
    when (this) {
        Constants.RemarkCategories.ACCIDENT -> {
            return "#DD2C00"
        }
        Constants.RemarkCategories.DAMAGE -> {
            return "#37474F"
        }
        Constants.RemarkCategories.LITTTER -> {
            return "#795548"
        }
        else -> {
            return "#EF6C00"
        }
    }
}

fun String.toBitmapDescriptor(): BitmapDescriptor {
    val hsv = FloatArray(3)
    Color.colorToHSV(Color.parseColor(this), hsv)
    return BitmapDescriptorFactory.defaultMarker(hsv[0])
}
