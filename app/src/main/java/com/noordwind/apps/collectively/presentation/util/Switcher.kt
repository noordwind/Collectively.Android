package com.noordwind.apps.collectively.presentation.util

import android.content.Context
import android.view.View
import java.util.*

class Switcher {

    private var contentViews: List<View> = LinkedList()
    private var errorViews: List<View> = LinkedList()
    private var emptyViews: List<View> = LinkedList()
    private var progressViews: List<View> = LinkedList()

    private lateinit var context: Context

    private var animators: List<CollectiveAnimationUtils.AnimatorListener> = LinkedList()

    private fun setContext(context: Context) {
        this.context = context
    }

    private fun setContentViews(contentViews: List<View>) {
        this.contentViews = contentViews
    }

    private fun setErrorViews(errorViews: List<View>) {
        this.errorViews = errorViews
    }

    private fun setEmptyViews(emptyViews: List<View>) {
        this.emptyViews = emptyViews
    }

    private fun setProgressViews(progressViews: List<View>) {
        this.progressViews = progressViews
    }

    fun showProgressViews() {
        cancelCurrentAnimators()

        val viewsToHide = LinkedList<View>()
        viewsToHide.addAll(contentViews)
        viewsToHide.addAll(emptyViews)
        viewsToHide.addAll(errorViews)

        animators = CollectiveAnimationUtils.crossFadeViews(context, viewsToHide, progressViews)
    }

    fun showContentViews() {
        cancelCurrentAnimators()

        val viewsToHide = LinkedList<View>()
        viewsToHide.addAll(progressViews)
        viewsToHide.addAll(emptyViews)
        viewsToHide.addAll(errorViews)

        animators = CollectiveAnimationUtils.crossFadeViews(context, viewsToHide, contentViews)
    }

    fun showErrorViews() {
        cancelCurrentAnimators()

        val viewsToHide = LinkedList<View>()
        viewsToHide.addAll(contentViews)
        viewsToHide.addAll(emptyViews)
        viewsToHide.addAll(errorViews)

        animators = CollectiveAnimationUtils.crossFadeViews(context, viewsToHide, errorViews)
    }

    private fun cancelCurrentAnimators() {
        if (animators != null) {
            for (animator in animators!!) {
                animator.onAnimationEnd()
            }
        }
    }

    fun showEmptyViews() {
        cancelCurrentAnimators()

        val viewsToHide = LinkedList<View>()
        viewsToHide.addAll(contentViews)
        viewsToHide.addAll(errorViews)
        viewsToHide.addAll(progressViews)

        animators = CollectiveAnimationUtils.crossFadeViews(context, viewsToHide, emptyViews)
    }

    class Builder {
        private val switcher = Switcher()

        fun withContentViews(contentView: List<View>): Builder {
            switcher.setContentViews(contentView)
            return this
        }

        fun withErrorViews(errorView: List<View>): Builder {
            switcher.setErrorViews(errorView)
            return this
        }

        fun withEmptyViews(emptyView: List<View>): Builder {
            switcher.setEmptyViews(emptyView)
            return this
        }

        fun withProgressViews(progressView: List<View>): Builder {
            switcher.setProgressViews(progressView)
            return this
        }

        fun build(context: Context): Switcher {
            switcher.setContext(context)
            return switcher
        }
    }

    fun showProgressViewsImmediately() {
        setViewsVisibility(contentViews, View.INVISIBLE)
        setViewsVisibility(errorViews, View.INVISIBLE)
        setViewsVisibility(emptyViews, View.INVISIBLE)

        setViewsVisibility(progressViews, View.VISIBLE)
    }

    fun showContentViewsImmediately() {
        setViewsVisibility(progressViews, View.INVISIBLE)
        setViewsVisibility(errorViews, View.INVISIBLE)
        setViewsVisibility(emptyViews, View.INVISIBLE)

        setViewsVisibility(contentViews, View.VISIBLE)
    }

    fun showErrorViewsImmediately() {
        setViewsVisibility(progressViews, View.INVISIBLE)
        setViewsVisibility(contentViews, View.INVISIBLE)
        setViewsVisibility(emptyViews, View.INVISIBLE)

        setViewsVisibility(errorViews, View.VISIBLE)
    }

    private fun setViewsVisibility(views: List<View>, visibility: Int) {
        for (view in views) {
            view.visibility = visibility
        }
    }
}
