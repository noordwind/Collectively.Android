package pl.adriankremski.coolector.utils

import android.app.Activity
import android.support.v4.content.ContextCompat
import android.support.v7.app.AlertDialog
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import pl.adriankremski.coolector.R
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

fun <T> ViewGroup.getChildViewsWithType(clazz: Class<T>): List<T> {
    return (0..childCount)
            .map { getChildAt(it) }
            .filter { it != null && it.javaClass == clazz }
            .mapTo(LinkedList<T>()) { it as T }
}

fun Activity.showPasswordResetErrorDialog(message: String) {
    val builder = AlertDialog.Builder(this)
    builder
            .setTitle(this.getString(R.string.password_reset_failed))
            .setMessage(message)
            .setPositiveButton(this.getString(R.string.ok_button), null)
    builder.show()
}

fun Activity.showLoginErrorDialog(message: String) {
    val builder = AlertDialog.Builder(this)
    builder
            .setTitle(this.getString(R.string.login_error_title))
            .setMessage(message)
            .setPositiveButton(this.getString(R.string.ok_button), null)
    builder.show()
}

fun Activity.showRegistrationErrorDialog(message: String) {
    val builder = AlertDialog.Builder(this)
    builder
            .setTitle(this.getString(R.string.registration_error_title))
            .setMessage(message)
            .setPositiveButton(this.getString(R.string.ok_button), null)
    builder.show()
}

fun Activity.showResetPasswordErrorDialog(message: String) {
    val builder = AlertDialog.Builder(this)
    builder
            .setTitle(this.getString(R.string.password_reset_failed))
            .setMessage(message)
            .setPositiveButton(this.getString(R.string.ok_button), null)
    builder.show()
}

