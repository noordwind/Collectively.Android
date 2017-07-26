package com.noordwind.apps.collectively.presentation.views.dialogs.mapfilters

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
import com.noordwind.apps.collectively.Constants
import com.noordwind.apps.collectively.R
import com.noordwind.apps.collectively.TheApp
import com.noordwind.apps.collectively.data.datasource.MapFiltersRepository
import com.noordwind.apps.collectively.domain.interactor.remark.filters.*
import com.noordwind.apps.collectively.domain.interactor.remark.filters.map.*
import com.noordwind.apps.collectively.domain.thread.PostExecutionThread
import com.noordwind.apps.collectively.domain.thread.UseCaseThread
import com.noordwind.apps.collectively.presentation.extension.dpToPx
import com.noordwind.apps.collectively.presentation.rxjava.RxBus
import com.noordwind.apps.collectively.presentation.views.FilterView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import javax.inject.Inject

class MapFiltersDialog : DialogFragment(), Constants, MapFiltersMvp.View {
    companion object {
        fun newInstance(): MapFiltersDialog = MapFiltersDialog()
    }

    @Inject
    lateinit var mapFiltersRepository: MapFiltersRepository

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
                LoadMapFiltersUseCase(mapFiltersRepository, ioThread, uiThread),
                AddMapFilterUseCase(mapFiltersRepository, ioThread, uiThread),
                RemoveMapFilterUseCase(mapFiltersRepository, ioThread, uiThread),
                SelectShowOnlyMyRemarksUseCase(mapFiltersRepository, ioThread, uiThread),
                SelectRemarkStatusUseCase(mapFiltersRepository, ioThread, uiThread))
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
            categoriesLayout.addView(FilterView(context, it, selectedFilters.contains(it)), categoriesLayout.childCount)
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
                .getEvents(FilterView.FilterSelectionChangedEvent::class.java)
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
