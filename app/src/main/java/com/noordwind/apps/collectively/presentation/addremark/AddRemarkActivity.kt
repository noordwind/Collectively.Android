package com.noordwind.apps.collectively.presentation.addremark

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
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
import com.bumptech.glide.load.resource.drawable.GlideDrawable
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.noordwind.apps.collectively.Constants
import com.noordwind.apps.collectively.R
import com.noordwind.apps.collectively.TheApp
import com.noordwind.apps.collectively.data.model.RemarkCategory
import com.noordwind.apps.collectively.data.model.RemarkNotFromList
import com.noordwind.apps.collectively.data.model.RemarkTag
import com.noordwind.apps.collectively.data.model.UserGroup
import com.noordwind.apps.collectively.data.repository.RemarksRepository
import com.noordwind.apps.collectively.data.repository.UserGroupsRepository
import com.noordwind.apps.collectively.data.repository.util.LocationRepository
import com.noordwind.apps.collectively.domain.interactor.remark.LoadRemarkCategoriesUseCase
import com.noordwind.apps.collectively.domain.interactor.remark.LoadRemarkTagsUseCase
import com.noordwind.apps.collectively.domain.interactor.remark.SaveRemarkUseCase
import com.noordwind.apps.collectively.domain.thread.PostExecutionThread
import com.noordwind.apps.collectively.domain.thread.UseCaseThread
import com.noordwind.apps.collectively.presentation.extension.*
import com.noordwind.apps.collectively.presentation.rxjava.RxBus
import com.noordwind.apps.collectively.presentation.statistics.LoadUserGroupsUseCase
import com.noordwind.apps.collectively.presentation.util.ZoomUtil
import com.noordwind.apps.collectively.presentation.views.RemarkCategoryFlowLayoutView
import com.noordwind.apps.collectively.presentation.views.RemarkTagView
import com.noordwind.apps.collectively.presentation.views.dialogs.addphoto.AddPhotoDialog
import com.noordwind.apps.collectively.usecases.LoadLastKnownLocationUseCase
import com.wefika.flowlayout.FlowLayout
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import jonathanfinerty.once.Once
import kotlinx.android.synthetic.main.activity_add_remark.*
import java.util.*
import javax.inject.Inject


class AddRemarkActivity : com.noordwind.apps.collectively.presentation.BaseActivity(), AddRemarkMvp.View {
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
    lateinit var userGroupsRepository: UserGroupsRepository

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
    private lateinit var categorySelectedEventDisposable: Disposable
    private var capturedImageUri: Uri? = null

    private val mCurrentAnimator: Animator? = null
    private var mShortAnimationDuration: Int = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        TheApp[this].appComponent?.inject(this)
        setContentView(R.layout.activity_add_remark);

        tagsLayout = findViewById(R.id.tags_layout) as FlowLayout

        selectedCategory = intent.getStringExtra(Constants.BundleKey.CATEGORY)
//        categoriesSpinner = findViewById(R.id.remark_categories) as Spinner

        addressLabel = findViewById(R.id.address) as TextView

        descriptionLabel = findViewById(R.id.description) as EditText

        submitButton.setOnClickListener { presenter.saveRemark(getCategory(), getDescription(), getSelectedTags(), capturedImageUri) }

        presenter = AddRemarkPresenter(this, SaveRemarkUseCase(remarksRepository, ioThread, uiThread),
                LoadRemarkTagsUseCase(remarksRepository, ioThread, uiThread),
                LoadRemarkCategoriesUseCase(remarksRepository, ioThread, uiThread),
                LoadLastKnownLocationUseCase(locationRepository, ioThread, uiThread),
                LoadUserGroupsUseCase(userGroupsRepository, ioThread, uiThread))

        presenter.loadRemarkCategories()
        presenter.loadRemarkTags()
        presenter.loadLastKnownAddress()
        presenter.loadUserGroups()

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

        categorySelectedEventDisposable = RxBus.instance
                .getEvents(RemarkCategoryFlowLayoutView.CategorySelectedEvent::class.java)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ selectCategory(it.category, it.isSelected)})

        mShortAnimationDuration = resources.getInteger(android.R.integer.config_shortAnimTime);
    }

    fun selectCategory(category: RemarkCategory, isSelected: Boolean) {
        if (isSelected) {
            selectedCategory = category.name

            remarkCategoriesLayout.getChildViewsWithType(RemarkCategoryFlowLayoutView::class.java).forEach {
                if (!category.name.equals(it.category.name)) {
                    it.select(false)
                }
            }
        } else {
            selectedCategory = ""
        }
    }

    fun getCategory() = selectedCategory

    fun getDescription() = descriptionLabel.text.toString()

    fun getSelectedTags(): List<String> {
        var tags = LinkedList<String>()
        var tagViews = tagsLayout!!.getChildViewsWithType(RemarkTagView::class.java)
        tagViews.filter { it.isSelected!! }.forEach { tags.add(it.text.toString()) }
        return tags
    }

    override fun showAvailableRemarkCategories(categories: List<RemarkCategory>) {

        categories.forEach {
            val newView = RemarkCategoryFlowLayoutView(this, it, true)
            newView.setBackgroundCompat(R.drawable.remark_tag_unselected_background)
            newView.text = it.name
            newView.gravity = Gravity.CENTER
            newView.setTextColor(ContextCompat.getColor(baseContext, R.color.white))
            newView.setPadding(30, 5, 30, 5)
            val params = FlowLayout.LayoutParams(ViewGroup.MarginLayoutParams.WRAP_CONTENT, 150)
            params.rightMargin = 10
            params.bottomMargin = 10
            newView.layoutParams = params

            if (it.name.toLowerCase().equals(selectedCategory.toLowerCase())) {
                newView.select(true)
            }

            remarkCategoriesLayout.addView(newView)
        }
    }

    override fun showAvailableUserGroups(userGroups: List<UserGroup>) {
        val groupNames = LinkedList<String>()
        var initialSelection = 0

        groupNames.add(getString(R.string.add_remark_all_groups_target))
        userGroups.forEachIndexed { i, group ->
            groupNames.add(group.name.uppercaseFirstLetter())
        }

        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, groupNames)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        groupsSpinner.adapter = adapter
        groupsSpinner.setSelection(initialSelection)
    }

    override fun showAvailableRemarkTags(categories: List<RemarkTag>) {
//        categories.forEach {
//
//            val newView = RemarkTagView(this, it, true)
//            newView.setBackgroundCompat(R.drawable.remark_tag_unselected_background)
//            newView.text = it.name
//            newView.gravity = Gravity.CENTER
//            newView.setTextColor(ContextCompat.getColor(baseContext, R.color.white))
//            newView.setPadding(30, 5, 30, 5)
//            val params = FlowLayout.LayoutParams(ViewGroup.MarginLayoutParams.WRAP_CONTENT, 150)
//            params.rightMargin = 10
//            params.bottomMargin = 10
//            newView.layoutParams = params
//
//            tagsLayout?.addView(newView)
//        }
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
                loadPhoto(capturedImageUri)
            } else if (requestCode == Constants.RequestCodes.TAKE_PICTURE) {
                loadPhoto(capturedImageUri)
            }
        }
    }

    fun loadPhoto(imageUri: Uri?) {
        imageUri.let {
            var listener = object : RequestListener<Uri, GlideDrawable> {
                override fun onResourceReady(resource: GlideDrawable?, model: Uri?, target: Target<GlideDrawable>?, isFromMemoryCache: Boolean, isFirstResource: Boolean): Boolean {
                    remarkImage.setOnClickListener {
                        zoomImage()
                        tapToZoomView.visibility = View.GONE
                        Once.markDone(Constants.OnceKey.SHOW_TAP_TO_ZOOM_ICON)
                    }


                    if (!Once.beenDone(Once.THIS_APP_INSTALL, Constants.OnceKey.SHOW_TAP_TO_ZOOM_ICON)) {
                        tapToZoomView.visibility = View.VISIBLE
                    }

                    return false
                }

                override fun onException(e: Exception?, model: Uri?, target: Target<GlideDrawable>?, isFirstResource: Boolean): Boolean {
                    return false
                }
            }

            Glide.with(baseContext)
                    .load(imageUri)
                    .listener(listener)
                    .into(remarkImage)

            Glide.with(baseContext).load(imageUri).into(expandedRemarkImage)
        }
    }

    fun zoomImage() {
        ZoomUtil.zoomImageFromThumb(mCurrentAnimator, mShortAnimationDuration, remarkImage, expandedRemarkImage, expandedRemarkImageContainer, container, object : AnimatorListenerAdapter() {
            override fun onAnimationCancel(animation: Animator?) {
                super.onAnimationCancel(animation)
            }

            override fun onAnimationEnd(animation: Animator?) {
                super.onAnimationEnd(animation)
            }
        })
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
        categorySelectedEventDisposable.dispose()
    }
}
