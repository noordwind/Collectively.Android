package pl.adriankremski.collectively.presentation.views

import android.animation.AnimatorSet
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import pl.adriankremski.collectively.R
import pl.adriankremski.collectively.presentation.extension.dpToPx

class CircularLikeProgressBar @JvmOverloads constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int = 0) : View(context, attrs, defStyleAttr) {

    private val mStartAngle = -90f                  // Always start from top (default is: "3 o'clock on a watch.")
    private val mPaint: Paint                 // Allocate paint outside onDraw to avoid unnecessary object creation
    private var mViewWidth: Int = 0
    private var mViewHeight: Int = 0
    private val mMinSweepAngle = 0f                       // Min degrees to sweep
    private val mMaxSweepAngle = 360f                     // Max degrees to sweep = full circle
    private val mStrokeWidth = 12f.dpToPx() // Width of outline
    private val mStrokeExtra = 1f.dpToPx()  // Width of extra spacing to add
    private val mAnimationDuration = 350                    // Animation duration for progress change
    private val mMaxProgress = 100                          // Max progress to use 0 - 100
    private val mProgressColorPos = resources.getColor(R.color.vote_up_remark_color)
    private val mProgressColorNeg = resources.getColor(R.color.vote_down_remark_color)
    private var mNegativeSweepAngle = -1f      // How long to sweep from mStartAngle. Rest will get interpreted as a positive sweep angle
    private var mPositiveSweepAngle = -1f      // How long to sweep from mNegativeSweepAngle until end.
    private var mAnimatorSet: AnimatorSet? = null

    init {
        mPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        mPaint.strokeWidth = mStrokeWidth.toFloat()
        mPaint.isAntiAlias = true
        mPaint.style = Paint.Style.STROKE
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        initMeasurments()
        drawOutlineArc(canvas)
    }

    private fun initMeasurments() {
        mViewWidth = width
        mViewHeight = height
    }

    private fun drawOutlineArc(canvas: Canvas) {
        val diameter = Math.min(mViewWidth, mViewHeight) - mStrokeWidth / 2
        val outerOval = RectF((mStrokeWidth / 2).toFloat(), (mStrokeWidth / 2).toFloat(), diameter.toFloat(), diameter.toFloat())

        if (mNegativeSweepAngle > -1) {
            mPaint.color = mProgressColorNeg
            canvas.drawArc(outerOval, mStartAngle, mNegativeSweepAngle, false, mPaint)
        }

        if (mPositiveSweepAngle > 1) {
            mPaint.color = mProgressColorPos
            canvas.drawArc(outerOval, mStartAngle + mNegativeSweepAngle - mStrokeExtra,
                    mPositiveSweepAngle + mStrokeExtra * 2, false, mPaint)
        }
    }

    private fun calcSweepAngleFromProgress(progress: Int): Float {
        return mMaxSweepAngle / mMaxProgress * progress
    }

    /**
     * Set progress of the circular progress bar.

     * @param negativeProgress progress between 0 and [.mMaxSweepAngle].
     */
    fun setProgress(negativeProgress: Int, animate: Boolean) {
        mNegativeSweepAngle = -1f
        mPositiveSweepAngle = -1f

        val negativeSweepAngle = calcSweepAngleFromProgress(negativeProgress)
        val positiveSweepAngle = calcSweepAngleFromProgress(mMaxProgress - negativeProgress)

        if (mAnimatorSet != null) {
            mAnimatorSet!!.cancel()
            mAnimatorSet = null
        }

        if (animate) {
            val negAnimator = ValueAnimator.ofFloat(mMinSweepAngle, negativeSweepAngle)
            negAnimator.addUpdateListener { valueAnimator ->
                mNegativeSweepAngle = valueAnimator.animatedValue as Float
                invalidate()
            }

            val posAnimator = ValueAnimator.ofFloat(mMinSweepAngle, positiveSweepAngle)
            posAnimator.addUpdateListener { valueAnimator ->
                mPositiveSweepAngle = valueAnimator.animatedValue as Float
                invalidate()
            }

            mAnimatorSet = AnimatorSet()
            if (negativeSweepAngle > 0f) {
                mAnimatorSet!!.playSequentially(negAnimator, posAnimator)
            } else {
                mAnimatorSet!!.playSequentially(posAnimator)
            }

            mAnimatorSet!!.duration = mAnimationDuration.toLong()
            mAnimatorSet!!.interpolator = AccelerateDecelerateInterpolator()
            mAnimatorSet!!.start()

        } else {
            mNegativeSweepAngle = negativeSweepAngle
            mPositiveSweepAngle = positiveSweepAngle
            invalidate()
        }
    }
}
