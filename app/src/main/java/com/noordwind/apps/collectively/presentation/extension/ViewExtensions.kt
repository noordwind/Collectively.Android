package com.noordwind.apps.collectively.presentation.extension

import android.app.Activity
import android.content.res.Resources
import android.graphics.Rect
import android.support.v4.content.ContextCompat
import android.support.v7.app.AlertDialog
import android.util.DisplayMetrics
import android.util.TypedValue
import android.view.TouchDelegate
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.noordwind.apps.collectively.R
import java.util.*




fun TextView.textInInt() : Int = text.toString().toInt()

fun View.setVisible() {
    visibility = View.VISIBLE
}

fun View.setInvisible() {
    visibility = View.INVISIBLE
}

fun View.setGone() {
    visibility = View.GONE
}

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

fun Activity.showChangePasswordErrorDialog(message: String) {
    val builder = AlertDialog.Builder(this)
    builder
            .setTitle(this.getString(R.string.password_not_changed))
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


fun Float.dpToPx(): Int {
    var densityDpi = DisplayMetrics.DENSITY_DEFAULT

    val metrics = Resources.getSystem().displayMetrics
    densityDpi = metrics.densityDpi

    return (this * (densityDpi / 160f)).toInt()
}

fun Float.spToPx(): Int = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, this, Resources.getSystem().displayMetrics).toInt()

fun View.expandTouchArea(padding: Int = 20) {
    this.post(ExpandedAreaRunnable(this, padding))
}

class ExpandedAreaRunnable : Runnable {

    private var view: View? = null
    private var padding = 20

    constructor(view: View) {
        this.view = view
    }

    constructor(view: View, padding: Int) {
        this.view = view
        this.padding = padding
    }

    override fun run() {
        // Post in the parent's message queue to make sure the parent
        // lays out its children before we call getHitRect()
        val delegateArea = Rect()
        val delegate = view
        delegate!!.getHitRect(delegateArea)
        delegateArea.top -= padding
        delegateArea.bottom += padding
        delegateArea.left -= padding
        delegateArea.right += padding
        val expandedArea = TouchDelegate(delegateArea,
                delegate)
        // give the delegate to an ancestor of the view we're
        // delegating the
        // area to
        if (View::class.java.isInstance(delegate.parent)) {
            (delegate.parent as View).touchDelegate = expandedArea
        }
    }
}
