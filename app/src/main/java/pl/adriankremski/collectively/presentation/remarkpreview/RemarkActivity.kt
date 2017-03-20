package pl.adriankremski.collectively.presentation.remarkpreview

import android.content.Context
import android.content.Intent
import android.os.Bundle
import pl.adriankremski.collectively.presentation.BaseActivity
import pl.adriankremski.collectively.R
import pl.adriankremski.collectively.TheApp
import pl.adriankremski.collectively.domain.thread.PostExecutionThread
import pl.adriankremski.collectively.domain.thread.UseCaseThread
import pl.adriankremski.collectively.data.repository.RemarksRepository
import pl.adriankremski.collectively.usecases.LoadRemarkUseCase
import pl.adriankremski.collectively.data.model.RemarkPreview
import android.support.v4.content.ContextCompat
import android.text.Html
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.wefika.flowlayout.FlowLayout
import kotlinx.android.synthetic.main.activity_remark_preview.*
import kotlinx.android.synthetic.main.view_error.*
import kotlinx.android.synthetic.main.view_progress.*
import pl.adriankremski.collectively.data.model.RemarkTag
import pl.adriankremski.collectively.data.repository.ProfileRepository
import pl.adriankremski.collectively.presentation.extension.uppercaseFirstLetter
import pl.adriankremski.collectively.presentation.util.RequestErrorDecorator
import pl.adriankremski.collectively.presentation.util.Switcher
import pl.adriankremski.collectively.presentation.views.RemarkTagView
import pl.adriankremski.collectively.domain.interactor.LoadUserIdUseCase
import pl.adriankremski.collectively.presentation.extension.setBackgroundCompat
import java.util.*
import javax.inject.Inject

class RemarkActivity : BaseActivity(), RemarkPreviewMvp.View {

    companion object {
        fun start(context: Context) {
            val intent = Intent(context, RemarkActivity::class.java)
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        TheApp[this].appComponent?.inject(this)
        setContentView(R.layout.activity_remark_preview);

        presenter = RemarkPresenter(this, LoadRemarkUseCase(remarksRepository, ioThread, uiThread), LoadUserIdUseCase(profileRepository, ioThread, uiThread))
        presenter.loadRemark("4cb5918d-35c2-4024-a278-e596d15e4844")
        errorDecorator = RequestErrorDecorator(switcherErrorImage, switcherErrorTitle, switcherErrorFooter)
        setupSwitcher()
        loadRemark()

        switcherErrorButton.setOnClickListener { loadRemark() }
    }

    private fun loadRemark() {
        presenter.loadRemark("da5938dc-bdae-4b57-9eac-0e3c0b12eb9f")
    }

    private fun setupSwitcher() {
        val contentViews = LinkedList<View>()
        contentViews.add(contentLayout)
        switcher = Switcher.Builder()
                .withContentViews(contentViews)
                .withErrorViews(listOf<View>(switcherError))
                .withProgressViews(listOf<View>(switcherProgress))
                .build(this)
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

            tagsLayout?.addView(newView)
        }

        descriptionLabel.text = remark.description
    }


    override fun showPositiveVotes(positiveVotesCount: Int) {
        positiveVotesCountLabel.text = positiveVotesCount.toString()
    }

    override fun showNegativeVotes(negativeVotesCount: Int) {
        negativeVotesCountLabel.text = negativeVotesCount.toString()
    }

    override fun showUserVotedPositively() {
        voteUpButton.setLiked(true)
        positiveVotesCountLabel.setTextColor(ContextCompat.getColor(baseContext, R.color.vote_up_remark_color))

        voteDownButton.setLiked(false)
        negativeVotesCountLabel.setTextColor(ContextCompat.getColor(baseContext, R.color.font_dark_hint))
    }

    override fun showUserVotedNegatively() {
        voteDownButton.setLiked(true)
        negativeVotesCountLabel.setTextColor(ContextCompat.getColor(baseContext, R.color.vote_down_remark_color))

        voteUpButton.setLiked(false)
        positiveVotesCountLabel.setTextColor(ContextCompat.getColor(baseContext, R.color.font_dark_hint))
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.destroy()
    }
}
