package pl.adriankremski.collectively.addremark

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
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.wefika.flowlayout.FlowLayout
import io.reactivex.disposables.CompositeDisposable
import pl.adriankremski.collectively.BaseActivity
import pl.adriankremski.collectively.R
import pl.adriankremski.collectively.TheApp
import pl.adriankremski.collectively.model.RemarkCategory
import pl.adriankremski.collectively.model.RemarkNotFromList
import pl.adriankremski.collectively.model.RemarkTag
import pl.adriankremski.collectively.repository.LocationRepository
import pl.adriankremski.collectively.repository.RemarksRepository
import pl.adriankremski.collectively.usecases.LoadLastKnownLocationUseCase
import pl.adriankremski.collectively.usecases.LoadRemarkCategoriesUseCase
import pl.adriankremski.collectively.usecases.LoadRemarkTagsUseCase
import pl.adriankremski.collectively.usecases.SaveRemarkUseCase
import pl.adriankremski.collectively.utils.getChildViewsWithType
import pl.adriankremski.collectively.utils.setBackgroundCompat
import pl.adriankremski.collectively.utils.uppercaseFirstLetter
import pl.adriankremski.collectively.views.RemarkTagView
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
    lateinit var remarksRepository: RemarksRepository

    @Inject
    lateinit var locationRepository: LocationRepository
    lateinit var presenter: AddRemarkMvp.Presenter

    internal var titleLabel: TextView? = null
    lateinit var categoriesSpinner: Spinner
    lateinit var descriptionLabel: EditText
    internal var tagsLayout: FlowLayout? = null
    lateinit var addressLabel: TextView
    lateinit var submitButton: View

    private var compositeDisposable: CompositeDisposable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        TheApp[this].appComponent?.inject(this)
        setContentView(R.layout.activity_add_remark);
        var span = SpannableString(getString(R.string.add_remark_screen_title))
        span.setSpan(RelativeSizeSpan(1.2f), 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        span.setSpan(StyleSpan(Typeface.BOLD), 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        titleLabel = findViewById(R.id.title) as TextView
        titleLabel?.text = span;

        tagsLayout = findViewById(R.id.tags_layout) as FlowLayout

        categoriesSpinner = findViewById(R.id.remark_categories) as Spinner

        addressLabel = findViewById(R.id.address) as TextView

        descriptionLabel = findViewById(R.id.description) as EditText

        submitButton = findViewById(R.id.submit) as View
        submitButton.setOnClickListener { presenter.saveRemark(getCategory(), getDescription(), getSelectedTags()) }

        compositeDisposable = CompositeDisposable();

        presenter = AddRemarkPresenter(this, SaveRemarkUseCase(remarksRepository), LoadRemarkTagsUseCase(remarksRepository),
                LoadRemarkCategoriesUseCase(remarksRepository), LoadLastKnownLocationUseCase(locationRepository))
        presenter.loadRemarkCategories()
        presenter.loadRemarkTags()
        presenter.loadLastKnownAddress()
    }

    fun getCategory() = categoriesSpinner.selectedItem.toString()

    fun getDescription() = descriptionLabel.text.toString()

    fun getSelectedTags(): List<String> {
        var tags = LinkedList<String>()
        var tagViews = tagsLayout!!.getChildViewsWithType(RemarkTagView::class.java)
        tagViews.filter { it.isSelected!! }.forEach { tags.add(it.text.toString())}
        return tags
    }

    override fun showAvailableRemarkCategories(categories: List<RemarkCategory>) {
        val categoryNames = LinkedList<String>()
        categories.forEach { categoryNames.add(it.name.uppercaseFirstLetter()) }
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, categoryNames)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        categoriesSpinner.adapter = adapter
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

            tagsLayout?.addView(newView)
        }
    }

    override fun showAddress(addressPretty: String) {
        addressLabel.text = addressPretty
    }

    override fun showSaveRemarkLoading() {
    }

    override fun showSaveRemarkError() {
        Toast.makeText(this, "Remark Not ADDED", Toast.LENGTH_SHORT).show()
    }

    override fun showSaveRemarkSuccess(newRemark: RemarkNotFromList) {
        Toast.makeText(this, "Remark Addded", Toast.LENGTH_SHORT).show()
        finish()
    }
}
