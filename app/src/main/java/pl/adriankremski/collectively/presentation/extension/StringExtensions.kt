package pl.adriankremski.collectively.presentation.extension

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
    when (this) {
        Constants.RemarkCategories.DEFECT -> {
            return "#37474F"
        }
        Constants.RemarkCategories.ISSUE -> {
            return "#DD2C00"
        }
        Constants.RemarkCategories.SUGGESTION -> {
            return "#FDD835"
        }
        Constants.RemarkCategories.PRAISE -> {
            return "#66BB6A"
        }
        else -> {
            return "#2C74DA"
        }
    }
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

fun String.markerBitmapOfCategory(): BitmapDescriptor {
    when (this) {
        Constants.RemarkCategories.DEFECT -> {
            return BitmapDescriptorFactory.fromResource(R.drawable.defect_marker)
        }
        Constants.RemarkCategories.ISSUE -> {
            return BitmapDescriptorFactory.fromResource(R.drawable.issue_marker)
        }
        Constants.RemarkCategories.SUGGESTION -> {
            return BitmapDescriptorFactory.fromResource(R.drawable.suggestion_marker)
        }
        Constants.RemarkCategories.PRAISE -> {
            return BitmapDescriptorFactory.fromResource(R.drawable.praise_marker)
        }
        else -> {
            return BitmapDescriptorFactory.fromResource(R.drawable.praise_marker)
        }
    }
}
