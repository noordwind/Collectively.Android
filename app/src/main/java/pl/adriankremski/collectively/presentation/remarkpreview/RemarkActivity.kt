package pl.adriankremski.collectively.presentation.remarkpreview

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.text.Html
import android.view.Gravity
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.like.LikeButton
import com.like.OnLikeListener
import com.wefika.flowlayout.FlowLayout
import kotlinx.android.synthetic.main.activity_remark_preview.*
import kotlinx.android.synthetic.main.view_error.*
import kotlinx.android.synthetic.main.view_progress.*
import pl.adriankremski.collectively.Constants
import pl.adriankremski.collectively.R
import pl.adriankremski.collectively.TheApp
import pl.adriankremski.collectively.data.model.RemarkComment
import pl.adriankremski.collectively.data.model.RemarkPreview
import pl.adriankremski.collectively.data.model.RemarkState
import pl.adriankremski.collectively.data.model.RemarkTag
import pl.adriankremski.collectively.data.repository.ProfileRepository
import pl.adriankremski.collectively.data.repository.RemarksRepository
import pl.adriankremski.collectively.domain.interactor.remark.LoadRemarkViewDataUseCase
import pl.adriankremski.collectively.domain.interactor.remark.votes.DeleteRemarkVoteUseCase
import pl.adriankremski.collectively.domain.interactor.remark.votes.SubmitRemarkVoteUseCase
import pl.adriankremski.collectively.domain.thread.PostExecutionThread
import pl.adriankremski.collectively.domain.thread.UseCaseThread
import pl.adriankremski.collectively.presentation.BaseActivity
import pl.adriankremski.collectively.presentation.extension.dpToPx
import pl.adriankremski.collectively.presentation.extension.setBackgroundCompat
import pl.adriankremski.collectively.presentation.extension.textInInt
import pl.adriankremski.collectively.presentation.extension.uppercaseFirstLetter
import pl.adriankremski.collectively.presentation.util.RequestErrorDecorator
import pl.adriankremski.collectively.presentation.util.Switcher
import pl.adriankremski.collectively.presentation.views.RemarkCommentView
import pl.adriankremski.collectively.presentation.views.RemarkStateView
import pl.adriankremski.collectively.presentation.views.RemarkTagView
import pl.adriankremski.collectively.presentation.views.ShowRemarkStatesButton
import pl.adriankremski.collectively.presentation.views.remark.ShowRemarkCommentsButton
import java.util.*
import javax.inject.Inject

class RemarkActivity : BaseActivity(), RemarkPreviewMvp.View {

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        TheApp[this].appComponent?.inject(this)
        setContentView(R.layout.activity_remark_preview);

        presenter = RemarkPresenter(this,
                LoadRemarkViewDataUseCase(profileRepository, remarksRepository, ioThread, uiThread),
                SubmitRemarkVoteUseCase(remarksRepository, profileRepository, ioThread, uiThread),
                DeleteRemarkVoteUseCase(remarksRepository, profileRepository, ioThread, uiThread))

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
                voteUpLiked()
                presenter.submitPositiveVote()
            }

            override fun unLiked(p0: LikeButton?) {
                voteUpUnliked()
                presenter.deletePositiveVote()
            }
        }
        voteUpButton.setOnLikeListener(voteUpButtonLikeListener)

        var voteDownButtonLikeListener = object : OnLikeListener {
            override fun liked(p0: LikeButton?) {
                voteDownLiked()
                presenter.submitNegativeVote()
            }

            override fun unLiked(p0: LikeButton?) {
                voteDownUnliked()
                presenter.deleteNegativeVote()
            }
        }
        voteDownButton.setOnLikeListener(voteDownButtonLikeListener)
    }

    fun voteUpLiked() {
        var votesCount = positiveVotesCountLabel.textInInt()
        positiveVotesCountLabel.text = Integer.toString(votesCount+1)

        positiveVotesCountLabel.setTextColor(ContextCompat.getColor(baseContext, R.color.vote_up_remark_color))
        negativeVotesCountLabel.setTextColor(ContextCompat.getColor(baseContext, R.color.font_dark_hint))

        if (votedDown) {
            voteDownUnliked()
            voteDownButton.setLiked(false)
        }

        votedUp = true
        votedDown = false
    }

    fun voteUpUnliked() {
        var votesCount = positiveVotesCountLabel.textInInt()
        positiveVotesCountLabel.text = Integer.toString(votesCount-1)

        positiveVotesCountLabel.setTextColor(ContextCompat.getColor(baseContext, R.color.font_dark_hint))

        votedUp = false
    }

    fun voteDownLiked() {
        var votesCount = negativeVotesCountLabel.textInInt()
        negativeVotesCountLabel.text = Integer.toString(votesCount+1)

        positiveVotesCountLabel.setTextColor(ContextCompat.getColor(baseContext, R.color.font_dark_hint))
        negativeVotesCountLabel.setTextColor(ContextCompat.getColor(baseContext, R.color.vote_down_remark_color))

        if (votedUp) {
            voteUpUnliked()
            voteUpButton.setLiked(false)
        }

        votedDown = true
        votedUp = false
    }

    fun voteDownUnliked() {
        var votesCount = negativeVotesCountLabel.textInInt()
        negativeVotesCountLabel.text = Integer.toString(votesCount-1)

        negativeVotesCountLabel.setTextColor(ContextCompat.getColor(baseContext, R.color.font_dark_hint))

        votedDown = false
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

    override fun showLoadedRemark(remark: RemarkPreview) {
        switcher.showContentViewsImmediately()

        Glide.with(this).load(remark.getFirstBigPhoto()?.url).into(imageView)

        locationLabel.text = remark.location.address
        remarkPhotoTitle.text = Html.fromHtml(getString(R.string.remark_preview_photo_title, remark.category.name.uppercaseFirstLetter(), remark.author.name))
        negativeVotesCountLabel.text = remark.negativeVotesCount().toString()

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
        descriptionLabel.text = remark.description

        historyButton.setOnClickListener { selectHistoryButton() }
        commentsButton.setOnClickListener { selectCommentsButton() }
    }

    fun selectHistoryButton() {
        commentsButton.setBackgroundResource(0)
        commentsButton.setTextColor(ContextCompat.getColor(this, R.color.font_dark_hint))
        commentsButton.setPadding(8f.dpToPx(), 8f.dpToPx(), 8f.dpToPx(), 8f.dpToPx())
        historyButton.setTextColor(ContextCompat.getColor(this, android.R.color.white))
        historyButton.setBackgroundResource(R.drawable.button_selected_no_corners)
        historyButton.setPadding(8f.dpToPx(), 8f.dpToPx(), 8f.dpToPx(), 8f.dpToPx())

        historyLayout.visibility = View.VISIBLE
        commentsLayout.visibility = View.GONE
    }

    fun selectCommentsButton() {
        historyButton.setBackgroundResource(0)
        historyButton.setTextColor(ContextCompat.getColor(this, R.color.font_dark_hint))
        historyButton.setPadding(8f.dpToPx(), 8f.dpToPx(), 8f.dpToPx(), 8f.dpToPx())
        commentsButton.setTextColor(ContextCompat.getColor(this, android.R.color.white))
        commentsButton.setBackgroundResource(R.drawable.button_selected_no_corners)
        commentsButton.setPadding(8f.dpToPx(), 8f.dpToPx(), 8f.dpToPx(), 8f.dpToPx())

        historyLayout.visibility = View.GONE
        commentsLayout.visibility = View.VISIBLE
    }

    override fun showEmptyComments() {
        commentsLayout.addView(ShowRemarkCommentsButton(baseContext, getString(R.string.add_comment), presenter.userId(), presenter.remarkId()), commentsLayout.childCount)
    }

    override fun showComments(comments: List<RemarkComment>) {
        commentsLayout.removeAllViews()
        comments.forEach {
            commentsLayout.addView(RemarkCommentView(baseContext, it), commentsLayout.childCount)
        }
    }

    override fun showShowCommentsButton() {
        commentsLayout.addView(ShowRemarkCommentsButton(baseContext, getString(R.string.show_comments), presenter.userId(), presenter.remarkId()), commentsLayout.childCount)
    }

    override fun showShowMoreCommentsButton() {
        commentsLayout.addView(ShowRemarkCommentsButton(baseContext, getString(R.string.show_more_comments), presenter.userId(), presenter.remarkId()), commentsLayout.childCount)
    }

    override fun showStates(states: List<RemarkState>) {
        historyLayout.removeAllViews()
        states.forEach {
            historyLayout.addView(RemarkStateView(baseContext, it), historyLayout.childCount)
        }
        historyLayout.addView(ShowRemarkStatesButton(baseContext, getString(R.string.show_activity), presenter.userId(), presenter.remarkId()), historyLayout.childCount)
    }


    override fun showPositiveVotes(positiveVotesCount: Int) {
        positiveVotesCountLabel.text = positiveVotesCount.toString()
    }

    override fun showNegativeVotes(negativeVotesCount: Int) {
        negativeVotesCountLabel.text = negativeVotesCount.toString()
    }

    override fun showUserVotedPositively() {
        votedUp = true
        votedDown = false

        voteUpButton.setLiked(true)
        positiveVotesCountLabel.setTextColor(ContextCompat.getColor(baseContext, R.color.vote_up_remark_color))

        voteDownButton.setLiked(false)
        negativeVotesCountLabel.setTextColor(ContextCompat.getColor(baseContext, R.color.font_dark_hint))
    }

    override fun showUserVotedNegatively() {
        votedDown = true
        votedUp = false

        voteDownButton.setLiked(true)
        negativeVotesCountLabel.setTextColor(ContextCompat.getColor(baseContext, R.color.vote_down_remark_color))

        voteUpButton.setLiked(false)
        positiveVotesCountLabel.setTextColor(ContextCompat.getColor(baseContext, R.color.font_dark_hint))
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item?.itemId) {
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
