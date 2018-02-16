package com.noordwind.apps.collectively.presentation.views

import android.content.Context
import android.support.design.widget.FloatingActionButton
import android.util.AttributeSet
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.view.animation.Interpolator
import android.view.animation.ScaleAnimation
import com.gordonwong.materialsheetfab.AnimatedFab
import com.noordwind.apps.collectively.R

class Fab : FloatingActionButton, AnimatedFab {

    constructor(context: Context) : super(context) {}

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {}

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {}

    /**
     * Shows the FAB.
     */
    override fun show() {
        show(0f, 0f)
    }

    /**
     * Shows the FAB and sets the FAB's translation.

     * @param translationX translation X value
     * *
     * @param translationY translation Y value
     */
    override fun show(translationX: Float, translationY: Float) {
        // Set FAB's translation
        setTranslation(translationX, translationY)

        // Only use scale animation if FAB is hidden
        if (visibility != View.VISIBLE) {
            // Pivots indicate where the animation begins from
            val pivotX = pivotX + translationX
            val pivotY = pivotY + translationY

            val anim: ScaleAnimation
            // If pivots are 0, that means the FAB hasn't been drawn yet so just use the
            // center of the FAB
            if (pivotX == 0f || pivotY == 0f) {
                anim = ScaleAnimation(0f, 1f, 0f, 1f, Animation.RELATIVE_TO_SELF, 0.5f,
                        Animation.RELATIVE_TO_SELF, 0.5f)
            } else {
                anim = ScaleAnimation(0f, 1f, 0f, 1f, pivotX, pivotY)
            }

            // Animate FAB expanding
            anim.duration = FAB_ANIM_DURATION.toLong()
            anim.interpolator = interpolator
            startAnimation(anim)
        }
        visibility = View.VISIBLE
    }

    /**
     * Hides the FAB.
     */
    override fun hide() {
        // Only use scale animation if FAB is visible
        if (visibility == View.VISIBLE) {
            // Pivots indicate where the animation begins from
            val pivotX = pivotX + translationX
            val pivotY = pivotY + translationY

            // Animate FAB shrinking
            val anim = ScaleAnimation(1f, 0f, 1f, 0f, pivotX, pivotY)
            anim.duration = FAB_ANIM_DURATION.toLong()
            anim.interpolator = interpolator
            startAnimation(anim)
        }
        visibility = View.INVISIBLE
    }

    private fun setTranslation(translationX: Float, translationY: Float) {
        animate().setInterpolator(interpolator).setDuration(FAB_ANIM_DURATION.toLong())
                .translationX(translationX).translationY(translationY)
    }

    private val interpolator: Interpolator
        get() = AnimationUtils.loadInterpolator(context, R.interpolator.msf_interpolator)

    companion object {

        private val FAB_ANIM_DURATION = 200
    }
}