package com.noordwind.apps.collectively.presentation.addremark

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.net.Uri
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.view.Gravity
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.GlideDrawable
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.google.android.gms.maps.model.LatLng
import com.noordwind.apps.collectively.Constants
import com.noordwind.apps.collectively.R
import com.noordwind.apps.collectively.TheApp
import com.noordwind.apps.collectively.data.datasource.FiltersTranslationsDataSource
import com.noordwind.apps.collectively.data.model.RemarkCategory
import com.noordwind.apps.collectively.data.model.RemarkNotFromList
import com.noordwind.apps.collectively.data.model.UserGroup
import com.noordwind.apps.collectively.data.repository.RemarksRepository
import com.noordwind.apps.collectively.data.repository.UserGroupsRepository
import com.noordwind.apps.collectively.data.repository.util.ConnectivityRepository
import com.noordwind.apps.collectively.data.repository.util.LocationRepository
import com.noordwind.apps.collectively.domain.interactor.remark.LoadRemarkCategoriesUseCase
import com.noordwind.apps.collectively.domain.interactor.remark.SaveRemarkUseCase
import com.noordwind.apps.collectively.domain.thread.PostExecutionThread
import com.noordwind.apps.collectively.domain.thread.UseCaseThread
import com.noordwind.apps.collectively.presentation.addremark.location.PickRemarkLocationActivity
import com.noordwind.apps.collectively.presentation.extension.*
import com.noordwind.apps.collectively.presentation.receiver.NetworkChangeReceiver
import com.noordwind.apps.collectively.presentation.rxjava.RxBus
import com.noordwind.apps.collectively.presentation.statistics.LoadUserGroupsUseCase
import com.noordwind.apps.collectively.presentation.util.ZoomUtil
import com.noordwind.apps.collectively.presentation.views.RemarkCategoryFlowLayoutView
import com.noordwind.apps.collectively.presentation.views.RemarkTagView
import com.noordwind.apps.collectively.presentation.views.dialogs.addphoto.AddPhotoDialog
import com.noordwind.apps.collectively.presentation.views.toast.ToastManager
import com.noordwind.apps.collectively.usecases.LoadLastKnownLocationUseCase
import com.wefika.flowlayout.FlowLayout
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import jonathanfinerty.once.Once
import kotlinx.android.synthetic.main.activity_add_remark.*
import java.util.*
import java.util.concurrent.TimeUnit
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
    lateinit var connectivityRepository: ConnectivityRepository

    @Inject
    lateinit var translationDataSource: FiltersTranslationsDataSource

    @Inject
    lateinit var ioThread: UseCaseThread

    @Inject
    lateinit var uiThread: PostExecutionThread

    lateinit var presenter: AddRemarkMvp.Presenter

    internal var titleLabel: TextView? = null
    lateinit var descriptionLabel: EditText
    internal var tagsLayout: FlowLayout? = null
    lateinit var addressLabel: TextView
    lateinit var selectedCategory: String

    private lateinit var galleryButtonClickEventDisposable: Disposable
    private lateinit var cameraButtonClickEventDisposable: Disposable
    private lateinit var categorySelectedEventDisposable: Disposable
    private lateinit var locationServiceEnabledEventDisposable: Disposable
    private lateinit var internetEnabledDiposable: Disposable

    private var capturedImageUri: Uri? = null

    private val mCurrentAnimator: Animator? = null
    private var mShortAnimationDuration: Int = 0
    private var networkChangeReceiver: NetworkChangeReceiver = NetworkChangeReceiver()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        TheApp[this].appComponent?.inject(this)
        setContentView(R.layout.activity_add_remark);

        tagsLayout = findViewById(R.id.tags_layout) as FlowLayout

        selectedCategory = translationDataSource.translateFromType(intent.getStringExtra(Constants.BundleKey.CATEGORY))

        addressLabel = findViewById(R.id.address) as TextView

        descriptionLabel = findViewById(R.id.description) as EditText

        submitButton.setOnClickListener {
            if (!presenter.checkInternetConnection()) {
               // skip, and the error will be shown
            } else if (getCategory().equals("")) {
                ToastManager(this, getString(R.string.add_remark_category_not_selected), Toast.LENGTH_SHORT).error().show()
            } else {
                presenter.saveRemark(getGroupName(), getCategory(), getDescription(), getSelectedTags(), capturedImageUri)
            }
        }

        presenter = AddRemarkPresenter(this, SaveRemarkUseCase(remarksRepository, ioThread, uiThread),
                LoadRemarkCategoriesUseCase(remarksRepository, translationDataSource, ioThread, uiThread),
                LoadLastKnownLocationUseCase(locationRepository, ioThread, uiThread),
                LoadUserGroupsUseCase(userGroupsRepository, ioThread, uiThread),
                connectivityRepository)

        presenter.checkInternetConnection()
        presenter.loadRemarkCategories()
        presenter.loadLastKnownAddress()
        presenter.loadUserGroups()

        fab.setOnClickListener { AddPhotoDialog.newInstance().show(supportFragmentManager, AddPhotoDialog::class.java.toString()) }

        addressSectionContainer.setOnClickListener {
            var location: LatLng? = null

            if (presenter.hasAddress()) {
                location = presenter.getLocation()
            }

            PickRemarkLocationActivity.start(this, location)
        }

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
                .subscribe({ selectCategory(it.category, it.isSelected) })

        locationServiceEnabledEventDisposable = RxBus.instance
                .getEvents(String::class.java)
                .filter { it.equals(Constants.RxBusEvent.LOCATION_SERVICE_ENABLED) }
                .delay(3, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ presenter.loadLastKnownAddress() })

        internetEnabledDiposable = RxBus.instance
                .getEvents(String::class.java)
                .filter { it.equals(Constants.RxBusEvent.INTERNET_CONNECTION_ENABLED) }
                .delay(2, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ presenter.onInternetEnabled() })

        mShortAnimationDuration = resources.getInteger(android.R.integer.config_shortAnimTime);

        var intentFilter = IntentFilter();
        intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(networkChangeReceiver, intentFilter);
    }

    override fun showAddressNotSpecifiedDialog() {
        showAddressNotSpecifiedDialogError()
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

    fun getCategory() = translationDataSource.translateToType(selectedCategory)

    fun getGroupName() : String? {
        if (groupsSpinner.adapter.isEmpty) {
            return null
        }

        var groupName = groupsSpinner.selectedItem.toString()
        if (groupName.equals(getString(R.string.add_remark_all_groups_target))) {
            return null
        }
        return groupName
    }

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

            var translation = it.translation
            translation?.let { newView.text = translation!!.uppercaseFirstLetter() }
            newView.gravity = Gravity.CENTER
            newView.setTextColor(ContextCompat.getColor(baseContext, R.color.white))
            newView.setPadding(30, 5, 30, 5)
            val params = FlowLayout.LayoutParams(ViewGroup.MarginLayoutParams.WRAP_CONTENT, 150)
            params.rightMargin = 10
            params.bottomMargin = 10
            newView.layoutParams = params

            if (it.translation.toLowerCase().equals(selectedCategory.toLowerCase())) {
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
        ToastManager(this, getString(R.string.adding_remark_error), Toast.LENGTH_SHORT).error().show()
    }

    override fun showSaveRemarkError(message: String?) {
        submitButton.text = getString(R.string.submit)
        submitProgress.visibility = View.GONE

        if (message.isNullOrBlank()) {
            ToastManager(this, getString(R.string.adding_remark_error), Toast.LENGTH_SHORT).error().show()
        } else {
            showOperationFailedDialog(message!!)
        }
    }

    override fun showSaveRemarkSuccess(newRemark: RemarkNotFromList) {
        submitButton.text = getString(R.string.submit)
        submitProgress.visibility = View.GONE
        ToastManager(this, getString(R.string.remark_added), Toast.LENGTH_SHORT).success().show()
    }

    override fun showNetworkError() {
        ToastManager(this, getString(R.string.no_internet_connection), Toast.LENGTH_SHORT).networkError().show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == RESULT_OK) {
            if (requestCode == Constants.RequestCodes.PICK_PICTURE_FROM_GALLERY) {
                capturedImageUri = data?.data
                loadPhoto(capturedImageUri)
            } else if (requestCode == Constants.RequestCodes.TAKE_PICTURE) {
                loadPhoto(capturedImageUri)
            } else if (requestCode == Constants.RequestCodes.PICK_LOCATION) {
                presenter.setLastKnownAddress(data!!.getStringExtra(Constants.BundleKey.ADDRESS))
                presenter.setLastKnownLocation(data!!.getParcelableExtra<LatLng>(Constants.BundleKey.LOCATION))
                showAddress(data!!.getStringExtra(Constants.BundleKey.ADDRESS))
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
        locationServiceEnabledEventDisposable.dispose()
        internetEnabledDiposable.dispose()
        unregisterReceiver(networkChangeReceiver)
    }
}
