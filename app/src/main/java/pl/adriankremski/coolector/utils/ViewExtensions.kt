package pl.adriankremski.coolector.utils

import android.support.v4.content.ContextCompat
import android.view.View
import android.view.ViewGroup

fun View.setViewsEnabledInHierarchy(enabled: Boolean) {
    if (this is ViewGroup) {
        for (i in 0..childCount) {
            getChildAt(i).setViewsEnabledInHierarchy(enabled);
        }
    } else {
        isEnabled = enabled;
        isClickable = enabled;
    }
}

fun View.setBackgroundCompat(background: Int) {
    setBackground(ContextCompat.getDrawable(context, background))
}

