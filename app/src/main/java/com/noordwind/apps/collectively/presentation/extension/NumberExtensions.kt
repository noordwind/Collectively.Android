package com.noordwind.apps.collectively.presentation.extension

/**
 * Created by adriankremski on 04/08/17.
 */

fun Int.formatDistance(): String {
    if (this in 1000..99999){
        var km = (this - (this % 1000)) / 1000
        var meters = this - (this - (this % 1000))
        meters = (meters - meters % 100)/100

        return km.toString() + "." + meters.toString() + "km"
    } else if (this >= 100000){
        return ((this - (this % 1000))/1000).toString() + "km"
    } else {
        return this.toString() + "m"
    }
}

