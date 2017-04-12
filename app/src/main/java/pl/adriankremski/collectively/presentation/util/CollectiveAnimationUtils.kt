package pl.adriankremski.collectively.presentation.util

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.content.Context
import android.util.Log
import android.view.View
import pl.adriankremski.collectively.presentation.extension.setInvisible
import pl.adriankremski.collectively.presentation.extension.setVisible
import java.util.*

class CollectiveAnimationUtils {

    companion object Companion {
        fun crossFadeViews(context: Context, viewsToHide: List<View>, viewsToShow: List<View>): List<AnimatorListener> {
            val animDuration = context.resources.getInteger(android.R.integer.config_shortAnimTime)

            val animators = viewsToHide.mapTo(LinkedList<AnimatorListener>()) { fadeOut(it, animDuration) }

            viewsToShow.mapTo(animators) { fadeIn(it, animDuration) }

            return animators
        }

        fun fadeIn(view: View, animDuration: Int): AnimatorListener {
            view.alpha = 0f
            view.setVisible()

            val listener = FadeInListener(view)

            view.animate().alpha(1f).setDuration(animDuration.toLong()).setListener(listener)

            return listener
        }

        fun fadeOut(view: View, animDuration: Int): AnimatorListener {
            val listener = FadeOutListener(view)

            view.animate().alpha(0f).setDuration(animDuration.toLong()).setListener(listener)

            return listener
        }
    }

    abstract class AnimatorListener(private val view: View) : AnimatorListenerAdapter() {

        override fun onAnimationStart(animation: Animator) {
            super.onAnimationStart(animation)
            Log.i(Switcher::class.java.simpleName, String.format("fadeIn START: %s", view.context.resources.getResourceName(view.id)))
        }

        override fun onAnimationEnd(animation: Animator) {
            onAnimationEnd()
        }

        abstract fun onAnimationEnd()
    }

    class FadeInListener(private val view: View) : AnimatorListener(view) {
        override fun onAnimationEnd() {
            view.setVisible()
        }
    }

    class FadeOutListener(private val view: View) : AnimatorListener(view) {
        override fun onAnimationEnd() {
            view.setInvisible()
        }
    }
}

