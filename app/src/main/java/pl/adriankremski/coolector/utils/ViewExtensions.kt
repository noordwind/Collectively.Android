package pl.adriankremski.coolector.utils

import android.support.v4.content.ContextCompat
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import java.util.*

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

fun TextView.textInString(): String {
    return text.toString()
}

fun <T> ViewGroup.getChildViewsWithType(clazz: Class<T>) : List<T>{
    return (0..childCount)
            .map { getChildAt(it) }
            .filter { it != null && it.javaClass == clazz }
            .mapTo(LinkedList<T>()) { it as T }
}

