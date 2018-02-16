package com.noordwind.apps.collectively.presentation.remarkpreview

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.text.Html
import android.view.Gravity
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.GlideDrawable
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.like.LikeButton
import com.like.OnLikeListener
import com.noordwind.apps.collectively.Constants
import com.noordwind.apps.collectively.R
import com.noordwind.apps.collectively.TheApp
import com.noordwind.apps.collectively.data.model.*
import com.noordwind.apps.collectively.data.repository.ProfileRepository
import com.noordwind.apps.collectively.data.repository.RemarksRepository
import com.noordwind.apps.collectively.domain.interactor.remark.LoadRemarkPhotoUseCase
import com.noordwind.apps.collectively.domain.interactor.remark.LoadRemarkViewDataUseCase
import com.noordwind.apps.collectively.domain.interactor.remark.votes.DeleteRemarkVoteUseCase
import com.noordwind.apps.collectively.domain.interactor.remark.votes.SubmitRemarkVoteUseCase
import com.noordwind.apps.collectively.domain.thread.PostExecutionThread
import com.noordwind.apps.collectively.domain.thread.UseCaseThread
import com.noordwind.apps.collectively.presentation.adapter.RemarkPreviewTabsAdapter
import com.noordwind.apps.collectively.presentation.extension.expandTouchArea
import com.noordwind.apps.collectively.presentation.extension.iconOfCategory
import com.noordwind.apps.collectively.presentation.extension.setBackgroundCompat
import com.noordwind.apps.collectively.presentation.extension.uppercaseFirstLetter
import com.noordwind.apps.collectively.presentation.main.RemarkIconBackgroundResolver
import com.noordwind.apps.collectively.presentation.remarkpreview.activity.RemarkStatesActivity
import com.noordwind.apps.collectively.presentation.remarkpreview.comments.RemarkCommentsActivity
import com.noordwind.apps.collectively.presentation.util.RequestErrorDecorator
import com.noordwind.apps.collectively.presentation.util.Switcher
import com.noordwind.apps.collectively.presentation.util.ZoomUtil
import com.noordwind.apps.collectively.presentation.views.RemarkTagView
import com.wefika.flowlayout.FlowLayout
import it.sephiroth.android.library.tooltip.Tooltip
import jonathanfinerty.once.Once
import kotlinx.android.synthetic.main.activity_remark_preview.*
import kotlinx.android.synthetic.main.view_error.*
import kotlinx.android.synthetic.main.view_progress.*
import java.util.*
import javax.inject.Inject

class RemarkActivity : com.noordwind.apps.collectively.presentation.BaseActivity(), RemarkPreviewMvp.View {

    companion object {
        fun start(context: Context, id: String) {
            val intent = Intent(context, RemarkActivity::class.java)
            intent.putExtra(Constants.BundleKey.ID, id)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            context.startActivity(intent)
        }
    }

    @Inject
    lateinit var remarksRepository: RemarksRepository

    @Inject
    lateinit var ioThread: UseCaseThread

    @Inject
    lateinit var uiThread: PostExecutionThread

    @Inject
    lateinit var profileRepository: ProfileRepository

    lateinit var presenter: RemarkPreviewMvp.Presenter

    private lateinit var switcher: Switcher
    private lateinit var errorDecorator: RequestErrorDecorator
    private var votedUp: Boolean = false
    private var votedDown: Boolean = false

    private val mCurrentAnimator: Animator? = null
    private var mShortAnimationDuration: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        TheApp[this].appComponent?.inject(this)
        setContentView(R.layout.activity_remark_preview);

        presenter = RemarkPresenter(this,
                LoadRemarkPhotoUseCase(remarksRepository, ioThread, uiThread),
                LoadRemarkViewDataUseCase(profileRepository, remarksRepository, ioThread, uiThread),
                SubmitRemarkVoteUseCase(remarksRepository, profileRepository, ioThread, uiThread),
                DeleteRemarkVoteUseCase(remarksRepository, profileRepository, ioThread, uiThread))

        presenter.onCreate()

        errorDecorator = RequestErrorDecorator(switcherErrorImage, switcherErrorTitle, switcherErrorFooter)
        val contentViews = LinkedList<View>()
        contentViews.add(contentLayout)
        switcher = Switcher.Builder()
                .withContentViews(contentViews)
                .withErrorViews(listOf<View>(switcherError))
                .withProgressViews(listOf<View>(switcherProgress))
                .build(this)

        loadRemark()

        switcherErrorButton.setOnClickListener { loadRemark() }

        var voteUpButtonLikeListener = object : OnLikeListener {
            override fun liked(p0: LikeButton?) {
                if (votedDown) {
                    presenter.decrementNegativeVote()
                }
                presenter.submitPositiveVote()
                voteUpLiked()
            }

            override fun unLiked(p0: LikeButton?) {
                presenter.deletePositiveVote()
                voteUpUnliked()
            }
        }
        voteUpButton.setOnLikeListener(voteUpButtonLikeListener)

        var voteDownButtonLikeListener = object : OnLikeListener {
            override fun liked(p0: LikeButton?) {
                if (votedUp) {
                    presenter.decrementPostiveVote()
                }
                presenter.submitNegativeVote()
                voteDownLiked()
            }

            override fun unLiked(p0: LikeButton?) {
                presenter.deleteNegativeVote()
                voteDownUnliked()
            }
        }
        voteDownButton.setOnLikeListener(voteDownButtonLikeListener)

        navigateButton.setOnClickListener { navigate() }

        mShortAnimationDuration = resources.getInteger(android.R.integer.config_shortAnimTime);
    }

    override fun disableVoteButtons() {
        voteUpButton.isEnabled = false
        voteDownButton.isEnabled = false
    }

    override fun enableVoteButtons() {
        voteUpButton.isEnabled = true
        voteDownButton.isEnabled = true
    }

    fun voteUpLiked() {
        if (votedDown) {
            voteDownUnliked()
            voteDownButton.setLiked(false)
        }

        votedUp = true
        votedDown = false

        refreshVotesCountLabel()
        invalidateLikesProgress()
    }

    fun voteUpUnliked() {
        votedUp = false
        votedDown = false

        refreshVotesCountLabel()
        invalidateLikesProgress()
    }

    fun voteDownLiked() {
        if (votedUp) {
            voteUpUnliked()
            voteUpButton.setLiked(false)
        }

        votedDown = true
        votedUp = false

        refreshVotesCountLabel()
        invalidateLikesProgress()
    }

    fun voteDownUnliked() {
        votedUp = false
        votedDown = false
        refreshVotesCountLabel()
        invalidateLikesProgress()
    }

    override fun showUserVotedPositively() {
        votedUp = true
        votedDown = false

        refreshVotesCountLabel()
    }

    override fun showUserVotedNegatively() {
        votedDown = true
        votedUp = false

        refreshVotesCountLabel()
    }


    private fun loadRemark() {
        presenter.loadRemark(intent.getStringExtra(Constants.BundleKey.ID))
    }

    override fun showRemarkLoading() {
        switcher.showProgressViewsImmediately()
    }

    override fun showRemarkLoadingNetworkError() {
        errorDecorator.onNetworkError(getString(R.string.error_loading_remark_no_network))
        switcher.showErrorViewsImmediately()
    }

    override fun showRemarkLoadingError(message: String) {
        errorDecorator.onServerError(message)
        switcher.showErrorViewsImmediately()
    }

    override fun showRemarkPhotoLoading() {
        processingImage.visibility = View.VISIBLE
        processingImageProgress.visibility = View.VISIBLE
        processingImageLabel.text = getString(R.string.remark_image_is_being_processed)
        processingImageLabel.setOnClickListener { }
    }

    override fun showRemarkPhoto(firstBigPhoto: RemarkPhoto?) {
        processingImage.visibility = View.GONE

        firstBigPhoto?.let {
            var listener = object : RequestListener<String, GlideDrawable> {
                override fun onResourceReady(resource: GlideDrawable?, model: String?, target: Target<GlideDrawable>?, isFromMemoryCache: Boolean, isFirstResource: Boolean): Boolean {
                    imageView.setOnClickListener {
                        zoomImage()
                        tapToZoomView.visibility = View.GONE
                        Once.markDone(Constants.OnceKey.SHOW_TAP_TO_ZOOM_ICON)
                    }


                    if (!Once.beenDone(Once.THIS_APP_INSTALL, Constants.OnceKey.SHOW_TAP_TO_ZOOM_ICON)) {
                        tapToZoomView.visibility = View.VISIBLE
                    }

                    return false
                }

                override fun onException(e: Exception?, model: String?, target: Target<GlideDrawable>?, isFirstResource: Boolean): Boolean {
                    return false
                }
            }

            Glide.with(baseContext)
                    .load(firstBigPhoto?.url)
                    .listener(listener)
                    .into(imageView)

            Glide.with(baseContext).load(firstBigPhoto?.url).into(expandedRemarkImage)
        }
    }

    override fun showRemarkPhotoLoadingError() {
        processingImage.visibility = View.VISIBLE

        processingImageProgress.visibility = View.GONE
        processingImageLabel.text = getString(R.string.error_loading_remark_photo)
        processingImageLabel.setOnClickListener { presenter.loadRemarkPhoto() }
    }

    override fun showLoadedRemark(remark: RemarkPreview) {
        switcher.showContentViewsImmediately()

        if (remark.offering != null) {
            categoryIcon.visibility = View.GONE
            remarkIconBackground.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.bitcoin_small))
        } else {
            categoryIcon.setImageDrawable(ContextCompat.getDrawable(this, remark.category?.name?.iconOfCategory()!!))
            remarkIconBackground.setImageResource(RemarkIconBackgroundResolver().iconBackgroundForRemark(remark))
        }

        locationLabel.text = remark.location.address

        if (remark.category!!.name.equals(Constants.RemarkCategories.PRAISE, true) || remark.category!!.name.equals(Constants.RemarkCategories.SUGGESTION, true)) {
            remarkPhotoTitle.text = Html.fromHtml(getString(R.string.remark_preview_photo_title_2, remark.category?.translation?.uppercaseFirstLetter(), remark.author?.name))
        } else {
            remarkPhotoTitle.text = Html.fromHtml(getString(R.string.remark_preview_photo_title, remark.category?.translation?.uppercaseFirstLetter(), remark.author?.name))
        }

        remark.tags.forEach {
            val newView = RemarkTagView(this, RemarkTag("", ""), useOnCLickListener = false)
            newView.setBackgroundCompat(R.drawable.remark_tag_selected_background)
            newView.text = it
            newView.gravity = Gravity.CENTER
            newView.setTextColor(ContextCompat.getColor(baseContext, R.color.white))
            newView.setPadding(30, 10, 30, 10)
            val params = FlowLayout.LayoutParams(ViewGroup.MarginLayoutParams.WRAP_CONTENT, 100)
            params.rightMargin = 10
            params.bottomMargin = 10
            newView.layoutParams = params
//            tagsLayout?.addView(newView)
        }

        descriptionLabel.visibility = if (remark.description.isEmpty()) View.GONE else View.VISIBLE
        descriptionHeader.visibility = if (remark.description.isEmpty()) View.GONE else View.VISIBLE
        descriptionLabel.text = remark.description
        findViewById(R.id.expand_collapse).expandTouchArea()


        if (!Once.beenDone(Once.THIS_APP_INSTALL, Constants.OnceKey.SHOW_VOTE_TOOLTIP)) {
            Once.markDone(Constants.OnceKey.SHOW_VOTE_TOOLTIP)
            Tooltip.make(this,
                    Tooltip.Builder(101)
                            .anchor(voteUpButton, Tooltip.Gravity.TOP)
                            .closePolicy(Tooltip.ClosePolicy()
                                    .insidePolicy(true, false)
                                    .outsidePolicy(true, false), 3000000)
                            .text(getString(R.string.click_vote_button_tooltip))
                            .maxWidth(500)
                            .withStyleId(R.style.ToolTipLayoutCustomStyle)
                            .withArrow(true)
                            .withOverlay(true)
                            .floatingAnimation(Tooltip.AnimationBuilder.DEFAULT)
                            .build()
            ).show()
        }

        if (remark.offering != null) {
            Tooltip.make(this,
                    Tooltip.Builder(101)
                            .anchor(remarkIconBackground, Tooltip.Gravity.TOP)
                            .closePolicy(Tooltip.ClosePolicy()
                                    .insidePolicy(true, false)
                                    .outsidePolicy(true, false), 3000000)
                            .text("Rozwiąż zgłoszenie aby zyskać Bitcoiny!")
                            .withStyleId(R.style.ToolTipLayoutCustomStyle)
                            .maxWidth(500)
                            .withArrow(true)
                            .withOverlay(true)
                            .floatingAnimation(Tooltip.AnimationBuilder.DEFAULT)
                            .build()
            ).show()
        }

        if (remark.group != null) {
            groupHeaderLabel.visibility = View.VISIBLE
            groupLabel.visibility = View.VISIBLE
            groupLabel.text = remark.group.name.trim()
        } else {
            groupHeaderLabel.visibility = View.GONE
            groupLabel.visibility = View.GONE
        }

    }

    fun zoomImage() {
        ZoomUtil.zoomImageFromThumb(mCurrentAnimator, mShortAnimationDuration, imageView, expandedRemarkImage, expandedRemarkImageContainer, container, object : AnimatorListenerAdapter() {
            override fun onAnimationCancel(animation: Animator?) {
                super.onAnimationCancel(animation)
            }

            override fun onAnimationEnd(animation: Animator?) {
                super.onAnimationEnd(animation)
            }
        })
    }

    fun navigate() {
        var latitude = presenter.remarkLatitude().toString()
        var longitude = presenter.remarkLongitude().toString()

        val gmmIntentUri = Uri.parse("google.navigation:q=$latitude,$longitude")
        val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
        mapIntent.`package` = "com.google.android.apps.maps"
        startActivity(mapIntent)
    }

    override fun showCommentsAndStates(comments: List<RemarkComment>, states: List<RemarkState>) {
        var adapter = RemarkPreviewTabsAdapter(baseContext, supportFragmentManager, comments, states, presenter.userId(), presenter.remarkId())

        historyButton.setOnClickListener { RemarkStatesActivity.start(baseContext, presenter.remarkId(), presenter.userId()) }
        commentsButton.setOnClickListener { RemarkCommentsActivity.start(baseContext, presenter.remarkId(), presenter.userId())}
        contentPager.adapter = adapter
        tabsLayout.setupWithViewPager(contentPager)
    }

    override fun refreshVotesCountLabel() {
        var positiveVotes = presenter.positivesVotes()
        var negativeVotes = presenter.negativeVotes()

        if (votedDown) {
            voteDownButton.setLiked(true)
            voteUpButton.setLiked(false)
        } else if (votedUp) {
            voteUpButton.setLiked(true)
            voteDownButton.setLiked(false)
        } else {
            voteUpButton.setLiked(false)
            voteDownButton.setLiked(false)
        }

        if (positiveVotes - negativeVotes >= 0) {
            votesCountLabel.text = "+" + (positiveVotes - negativeVotes)
            votesCountLabel.setTextColor(ContextCompat.getColor(baseContext, R.color.vote_up_remark_color))
        } else {
            votesCountLabel.text = "" + (positiveVotes - negativeVotes)
            votesCountLabel.setTextColor(ContextCompat.getColor(baseContext, R.color.vote_down_remark_color))
        }
    }

    override fun invalidateLikesProgress() {
        circularProgressBar.postDelayed({
            var negativeProgress = 50 // Default to half full circle

            var likes = presenter.positivesVotes()
            var dislikes = presenter.negativeVotes()

            if (dislikes > 0 || likes > 0) {
                negativeProgress = (dislikes / (likes + dislikes).toFloat() * 100).toInt()
            }

            circularProgressBar.setProgress(negativeProgress, true)
        }, 500)
    }

    override fun onStart() {
        super.onStart()
        presenter.onStart()
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            android.R.id.home -> {
                onBackPressed()
                return true;
            }
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.destroy()
    }
}
