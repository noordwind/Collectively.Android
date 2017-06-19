package pl.adriankremski.collectively.presentation.views.dialogs.mapfilters

import android.content.DialogInterface
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v4.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import pl.adriankremski.collectively.Constants
import pl.adriankremski.collectively.R
import pl.adriankremski.collectively.TheApp
import pl.adriankremski.collectively.data.datasource.FiltersRepository
import pl.adriankremski.collectively.domain.interactor.remark.filters.*
import pl.adriankremski.collectively.domain.thread.PostExecutionThread
import pl.adriankremski.collectively.domain.thread.UseCaseThread
import pl.adriankremski.collectively.presentation.extension.dpToPx
import pl.adriankremski.collectively.presentation.rxjava.RxBus
import pl.adriankremski.collectively.presentation.views.MapFilterView
import javax.inject.Inject

class MapFiltersDialog : DialogFragment(), Constants, MapFiltersMvp.View {
    companion object {
        fun newInstance(): MapFiltersDialog = MapFiltersDialog()
    }

    @Inject
    lateinit var filtersRepository: FiltersRepository

    @Inject
    lateinit var uiThread: PostExecutionThread

    @Inject
    lateinit var ioThread: UseCaseThread

    private lateinit var presenter: MapFiltersMvp.Presenter
    private lateinit var categoriesLayout: ViewGroup
    private lateinit var resolvedFilterButton: TextView
    private lateinit var unresolvedFilterButton: TextView
    private lateinit var showOnlyMyRemarksCheckButton: CheckBox
    private lateinit var mMapFilterSelectedEventDisposable: Disposable
    private lateinit var dismissListener: DialogInterface.OnDismissListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NO_TITLE, android.R.style.Theme_Holo_Light_Dialog)
        TheApp[context].appComponent?.inject(this)

        presenter = MapFiltersPresenter(this,
                LoadMapFiltersUseCase(filtersRepository, ioThread, uiThread),
                AddFilterUseCase(filtersRepository, ioThread, uiThread),
                RemoveFilterUseCase(filtersRepository, ioThread, uiThread),
                SelectShowOnlyMyRemarksUseCase(filtersRepository, ioThread, uiThread),
                SelectRemarkStatusUseCase(filtersRepository, ioThread, uiThread))
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val rootView = inflater!!.inflate(R.layout.fragment_filter_remarks_dialog, container, false)
        categoriesLayout = rootView.findViewById(R.id.categoriesLayout) as ViewGroup

        rootView.findViewById(R.id.background).setOnClickListener { dismiss() }

        resolvedFilterButton = rootView.findViewById(R.id.resolvedFilterButton) as TextView
        unresolvedFilterButton = rootView.findViewById(R.id.unresolvedFilterButton) as TextView
        showOnlyMyRemarksCheckButton = rootView.findViewById((R.id.showOnlyMineCheckButton)) as CheckBox

        resolvedFilterButton.setOnClickListener {
            presenter.selectRemarkStatus(resolvedFilterButton.text.toString())
            selectResolvedFilterButton()
        }

        unresolvedFilterButton.setOnClickListener {
            presenter.selectRemarkStatus(unresolvedFilterButton.text.toString())
            selectUnresolvedFilterButton()
        }


        showOnlyMyRemarksCheckButton.setOnCheckedChangeListener { button, isChecked -> presenter.toggleShouldShowOnlyMyRemarksFilter(isChecked)  }

        presenter.loadFilters()
        return rootView
    }

    fun selectResolvedFilterButton() {
        unresolvedFilterButton.setBackgroundResource(0)
        unresolvedFilterButton.setTextColor(ContextCompat.getColor(context, R.color.font_dark_hint))
        unresolvedFilterButton.setPadding(8f.dpToPx(), 8f.dpToPx(), 8f.dpToPx(), 8f.dpToPx())
        resolvedFilterButton.setTextColor(ContextCompat.getColor(context, android.R.color.white))
        resolvedFilterButton.setBackgroundResource(R.drawable.button_unselected)
        resolvedFilterButton.setPadding(8f.dpToPx(), 8f.dpToPx(), 8f.dpToPx(), 8f.dpToPx())
    }

    fun selectUnresolvedFilterButton() {
        resolvedFilterButton.setBackgroundResource(0)
        resolvedFilterButton.setTextColor(ContextCompat.getColor(context, R.color.font_dark_hint))
        resolvedFilterButton.setPadding(8f.dpToPx(), 8f.dpToPx(), 8f.dpToPx(), 8f.dpToPx())
        unresolvedFilterButton.setTextColor(ContextCompat.getColor(context, android.R.color.white))
        unresolvedFilterButton.setBackgroundResource(R.drawable.button_unselected)
        unresolvedFilterButton.setPadding(8f.dpToPx(), 8f.dpToPx(), 8f.dpToPx(), 8f.dpToPx())
    }

    override fun showFilters(selectedFilters: List<String>, allFilters: List<String>) {
        categoriesLayout.removeAllViews()
        allFilters.forEach {
            categoriesLayout.addView(MapFilterView(context, it, selectedFilters.contains(it)), categoriesLayout.childCount)
        }
    }

    override fun selectRemarkStatusFilter(status: String) {
        if (context.getString(R.string.resolved_filter_api).equals(status)) {
            selectResolvedFilterButton()
        } else if (context.getString(R.string.unresolved_filter_api).equals(status)) {
            selectUnresolvedFilterButton()
        }
    }

    override fun selectShowOnlyMineRemarksFilter(showOnlyMine: Boolean) {
        showOnlyMyRemarksCheckButton.isChecked = showOnlyMine
    }

    fun setOnDismissListener(dismissListener: DialogInterface.OnDismissListener) {
        this.dismissListener = dismissListener
    }

    override fun onStart() {
        super.onStart()
        val d = dialog
        dialog.setOnDismissListener(dismissListener)
        dialog.setCanceledOnTouchOutside(true)
        if (d != null) {
            val width = ViewGroup.LayoutParams.MATCH_PARENT
            val height = ViewGroup.LayoutParams.WRAP_CONTENT
            d.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            d.window!!.setLayout(width, height)
        }

        mMapFilterSelectedEventDisposable = RxBus.instance
                .getEvents(MapFilterView.MapFilterSelectionChangedEvent::class.java)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ presenter.toggleFilter(it.filter, it.selected) })
    }

    override fun onStop() {
        super.onStop()

        if (mMapFilterSelectedEventDisposable != null && !mMapFilterSelectedEventDisposable.isDisposed) {
            mMapFilterSelectedEventDisposable.dispose()
        }
    }
}
