package pl.adriankremski.collectively.presentation.addremark

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.view.Gravity
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.bumptech.glide.Glide
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import com.wefika.flowlayout.FlowLayout
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.activity_add_remark.*
import pl.adriankremski.collectively.Constants
import pl.adriankremski.collectively.R
import pl.adriankremski.collectively.TheApp
import pl.adriankremski.collectively.data.model.RemarkCategory
import pl.adriankremski.collectively.data.model.RemarkNotFromList
import pl.adriankremski.collectively.data.model.RemarkTag
import pl.adriankremski.collectively.data.repository.RemarksRepository
import pl.adriankremski.collectively.data.repository.util.LocationRepository
import pl.adriankremski.collectively.domain.interactor.remark.LoadRemarkCategoriesUseCase
import pl.adriankremski.collectively.domain.interactor.remark.LoadRemarkTagsUseCase
import pl.adriankremski.collectively.domain.interactor.remark.SaveRemarkUseCase
import pl.adriankremski.collectively.domain.thread.PostExecutionThread
import pl.adriankremski.collectively.domain.thread.UseCaseThread
import pl.adriankremski.collectively.presentation.BaseActivity
import pl.adriankremski.collectively.presentation.extension.*
import pl.adriankremski.collectively.presentation.rxjava.RxBus
import pl.adriankremski.collectively.presentation.views.RemarkTagView
import pl.adriankremski.collectively.presentation.views.dialogs.mapfilters.AddPhotoDialog
import pl.adriankremski.collectively.usecases.LoadLastKnownLocationUseCase
import java.util.*
import javax.inject.Inject


class AddRemarkActivity : BaseActivity(), AddRemarkMvp.View {
    companion object {
        fun start(context: Context, category: String) {
            val intent = Intent(context, AddRemarkActivity::class.java)
            intent.putExtra(Constants.BundleKey.CATEGORY, category)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            context.startActivity(intent)
        }
    }

    @Inject
    lateinit var remarksRepository: RemarksRepository

    @Inject
    lateinit var locationRepository: LocationRepository

    @Inject
    lateinit var ioThread: UseCaseThread

    @Inject
    lateinit var uiThread: PostExecutionThread

    lateinit var presenter: AddRemarkMvp.Presenter

    internal var titleLabel: TextView? = null
    lateinit var categoriesSpinner: Spinner
    lateinit var descriptionLabel: EditText
    internal var tagsLayout: FlowLayout? = null
    lateinit var addressLabel: TextView
    lateinit var selectedCategory: String

    private lateinit var galleryButtonClickEventDisposable: Disposable
    private lateinit var cameraButtonClickEventDisposable: Disposable
    private var capturedImageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        TheApp[this].appComponent?.inject(this)
        setContentView(R.layout.activity_add_remark);

        tagsLayout = findViewById(R.id.tags_layout) as FlowLayout

        selectedCategory = intent.getStringExtra(Constants.BundleKey.CATEGORY)
        categoriesSpinner = findViewById(R.id.remark_categories) as Spinner

        addressLabel = findViewById(R.id.address) as TextView

        descriptionLabel = findViewById(R.id.description) as EditText

        submitButton.setOnClickListener { presenter.saveRemark(getCategory(), getDescription(), getSelectedTags()) }

        presenter = AddRemarkPresenter(this, SaveRemarkUseCase(remarksRepository, ioThread, uiThread),
                LoadRemarkTagsUseCase(remarksRepository, ioThread, uiThread),
                LoadRemarkCategoriesUseCase(remarksRepository, ioThread, uiThread),
                LoadLastKnownLocationUseCase(locationRepository, ioThread, uiThread))

        presenter.loadRemarkCategories()
        presenter.loadRemarkTags()
        presenter.loadLastKnownAddress()

        fab.setOnClickListener { AddPhotoDialog.newInstance().show(supportFragmentManager, AddPhotoDialog::class.java.toString()) }

        galleryButtonClickEventDisposable = RxBus.instance
                .getEvents(String::class.java)
                .filter { it.equals(Constants.RxBusEvent.GALLERY_EVENT) }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ capturedImageUri = openGalleryPicker() })

        cameraButtonClickEventDisposable = RxBus.instance
                .getEvents(String::class.java)
                .filter { it.equals(Constants.RxBusEvent.CAMERA_EVENT) }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ capturedImageUri = openCamera() })
    }

    fun getCategory() = categoriesSpinner.selectedItem.toString()

    fun getDescription() = descriptionLabel.text.toString()

    fun getSelectedTags(): List<String> {
        var tags = LinkedList<String>()
        var tagViews = tagsLayout!!.getChildViewsWithType(RemarkTagView::class.java)
        tagViews.filter { it.isSelected!! }.forEach { tags.add(it.text.toString()) }
        return tags
    }

    override fun showAvailableRemarkCategories(categories: List<RemarkCategory>) {
        val categoryNames = LinkedList<String>()
        var initialSelection = 0

        categories.forEachIndexed { i, remarkCategory ->
            if (remarkCategory.name.toLowerCase().equals(selectedCategory.toLowerCase())) {
                initialSelection = i
            }
            categoryNames.add(remarkCategory.name.uppercaseFirstLetter())
        }

        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, categoryNames)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        categoriesSpinner.adapter = adapter
        categoriesSpinner.setSelection(initialSelection)
    }

    override fun showAvailableRemarkTags(categories: List<RemarkTag>) {
        categories.forEach {

            val newView = RemarkTagView(this, it, true)
            newView.setBackgroundCompat(R.drawable.remark_tag_unselected_background)
            newView.text = it.name
            newView.gravity = Gravity.CENTER
            newView.setTextColor(ContextCompat.getColor(baseContext, R.color.white))
            newView.setPadding(30, 5, 30, 5)
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
        submitButton.text = getString(R.string.saving_remark)
        submitProgress.visibility = View.VISIBLE
    }

    override fun showSaveRemarkError() {
        submitButton.text = getString(R.string.submit)
        submitProgress.visibility = View.GONE
        Toast.makeText(this, "Remark Not ADDED", Toast.LENGTH_SHORT).show()
    }

    override fun showSaveRemarkSuccess(newRemark: RemarkNotFromList) {
        submitButton.text = getString(R.string.saving_photo)
        submitProgress.visibility = View.GONE
        Toast.makeText(this, "Remark Addded", Toast.LENGTH_SHORT).show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)


        if (resultCode == RESULT_OK) {
            if (requestCode == Constants.RequestCodes.PICK_PICTURE_FROM_GALLERY) {
                capturedImageUri = data?.data
//                capturedImageUri.let { Glide.with(baseContext).load(capturedImageUri).into(remarkImage) }

                CropImage.activity(capturedImageUri)
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .setAllowRotation(true)
                        .setAspectRatio(remarkImage.width, remarkImage.height)
                        .start(this);

            } else if (requestCode == Constants.RequestCodes.TAKE_PICTURE) {
                CropImage.activity(capturedImageUri)
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .setMaxCropResultSize(remarkImage.width, remarkImage.height)
                        .start(this);

            } else if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
                var result = CropImage.getActivityResult(data) as CropImage.ActivityResult
                if (resultCode == RESULT_OK) {
                    var resultUri = result.uri;
                    resultUri.let { Glide.with(baseContext).load(resultUri).into(remarkImage) }
                } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                    var error = result.error;
                }
            }

        } else if (resultCode == Activity.RESULT_CANCELED) {

        }
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
        galleryButtonClickEventDisposable.dispose()
        cameraButtonClickEventDisposable.dispose()
    }
}
