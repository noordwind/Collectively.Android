package pl.adriankremski.coolector.addremark

import android.content.Context
import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.text.Spannable
import android.text.SpannableString
import android.text.style.RelativeSizeSpan
import android.text.style.StyleSpan
import android.view.Gravity
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import com.wefika.flowlayout.FlowLayout
import io.reactivex.disposables.CompositeDisposable
import pl.adriankremski.coolector.BaseActivity
import pl.adriankremski.coolector.R
import pl.adriankremski.coolector.TheApp
import pl.adriankremski.coolector.authentication.login.AddRemarkPresenter
import pl.adriankremski.coolector.model.RemarkCategory
import pl.adriankremski.coolector.model.RemarkTag
import pl.adriankremski.coolector.repository.RemarksRepository
import pl.adriankremski.coolector.utils.setBackgroundCompat
import pl.adriankremski.coolector.utils.uppercaseFirstLetter
import pl.adriankremski.coolector.views.RemarkTagView
import java.util.*
import javax.inject.Inject



class AddRemarkActivity : BaseActivity(), AddRemarkMvp.View {

    companion object {
        fun start(context: Context) {
            val intent = Intent(context, AddRemarkActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            context.startActivity(intent)
        }
    }

    @Inject
    lateinit var mRemarksRepository: RemarksRepository
    lateinit var mPresenter: AddRemarkMvp.Presenter

    internal var mTitleLabel: TextView? = null
    lateinit var mCategoriesSpinner: Spinner
    lateinit var mDescriptionLabel: EditText
    internal var mTagsLayout: FlowLayout? = null

    private var mCompositeDisposable: CompositeDisposable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        TheApp[this].appComponent?.inject(this)
        setContentView(R.layout.activity_add_remark);
        var span = SpannableString(getString(R.string.add_remark_screen_title))
        span.setSpan(RelativeSizeSpan(1.2f), 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        span.setSpan(StyleSpan(Typeface.BOLD), 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        mTitleLabel = findViewById(R.id.title) as TextView
        mTitleLabel?.text = span;

        mTagsLayout = findViewById(R.id.tags_layout) as FlowLayout

        mCategoriesSpinner = findViewById(R.id.remark_categories) as Spinner

        mCompositeDisposable = CompositeDisposable();

        mPresenter = AddRemarkPresenter(this, mRemarksRepository)
        mPresenter.loadRemarkCategories()
        mPresenter.loadRemarkTags()
    }

    override fun showAvailableRemarkCategories(categories: List<RemarkCategory>) {
        val categoryNames = LinkedList<String>()
        categories.forEach { categoryNames.add(it.name.uppercaseFirstLetter()) }
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, categoryNames)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        mCategoriesSpinner.adapter = adapter
    }

    override fun showAvailableRemarkTags(categories: List<RemarkTag>) {
        categories.forEach {

            val newView = RemarkTagView(this, it)
            newView.setBackgroundCompat(R.drawable.remark_tag_unselected_background)
            newView.text = it.name
            newView.gravity = Gravity.CENTER
            newView.setTextColor(ContextCompat.getColor(baseContext, R.color.white))
            newView.setPadding(30, 10, 30, 10)
            val params = FlowLayout.LayoutParams(ViewGroup.MarginLayoutParams.WRAP_CONTENT, 150)
            params.rightMargin = 10
            params.bottomMargin = 10
            newView.layoutParams = params

            mTagsLayout?.addView(newView)
        }


    }
}
