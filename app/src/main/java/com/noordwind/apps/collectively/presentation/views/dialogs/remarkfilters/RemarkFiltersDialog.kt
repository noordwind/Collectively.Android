package com.noordwind.apps.collectively.presentation.views.dialogs.mapfilters

import android.content.DialogInterface
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.noordwind.apps.collectively.Constants
import com.noordwind.apps.collectively.R
import com.noordwind.apps.collectively.TheApp
import com.noordwind.apps.collectively.data.datasource.RemarkFiltersRepository
import com.noordwind.apps.collectively.domain.interactor.remark.filters.map.*
import com.noordwind.apps.collectively.domain.thread.PostExecutionThread
import com.noordwind.apps.collectively.domain.thread.UseCaseThread
import com.noordwind.apps.collectively.presentation.rxjava.RxBus
import com.noordwind.apps.collectively.presentation.views.FilterView
import com.noordwind.apps.collectively.presentation.views.dialogs.remarkfilters.FiltersMvp
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import javax.inject.Inject

class RemarkFiltersDialog : DialogFragment(), Constants, FiltersMvp.View {

    interface OnFilter {
        fun filter()
    }

    companion object {
        fun newInstance(): RemarkFiltersDialog = RemarkFiltersDialog()
    }

    @Inject
    lateinit var filtersRepository: RemarkFiltersRepository

    @Inject
    lateinit var uiThread: PostExecutionThread

    @Inject
    lateinit var ioThread: UseCaseThread

    private lateinit var presenter: FiltersMvp.Presenter

    private lateinit var categoriesLayout: ViewGroup
    private lateinit var statesLayout: ViewGroup

    private var dismissListener: DialogInterface.OnDismissListener? = null

    private lateinit var disposable: Disposable

    private lateinit var filterListener: OnFilter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NO_TITLE, android.R.style.Theme_Holo_Light_Dialog)
        TheApp[context].appComponent?.inject(this)

        presenter = RemarkFiltersPresenter(this,
                loadRemarkFiltersUseCase = LoadRemarkFiltersUseCase(filtersRepository, ioThread, uiThread),
                addCategoryFilterUseCase = AddCategoryFilterUseCase(filtersRepository, ioThread, uiThread),
                addStatusFilterUseCase = AddStatusFilterUseCase(filtersRepository, ioThread, uiThread),
                removeCategoryFilterUseCase = RemoveCategoryFilterUseCase(filtersRepository, ioThread, uiThread),
                removeStatusFilterUseCase = RemoveStatusFilterUseCase(filtersRepository, ioThread, uiThread))
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val rootView = inflater!!.inflate(R.layout.fragment_filter_user_remarks_dialog, container, false)
        categoriesLayout = rootView.findViewById(R.id.categoriesLayout) as ViewGroup
        statesLayout = rootView.findViewById(R.id.statesLayout) as ViewGroup
        rootView.findViewById(R.id.background).setOnClickListener { dismiss() }
        rootView.findViewById(R.id.filter).setOnClickListener {
            dismiss()
            filterListener.filter()
        }
        presenter.loadFilters()
        return rootView
    }

    override fun showRemarkCategoryFilters(selectedFilters: List<String>, allFilters: List<String>) {
        categoriesLayout.removeAllViews()
        allFilters.forEach {
            categoriesLayout.addView(FilterView(context = context,
                    filter = it,
                    isChecked = selectedFilters.contains(it),
                    showIcon = true,
                    type = "category"), categoriesLayout.childCount)
        }
    }

    override fun showRemarkStatusFilters(selectedFilters: List<String>, allFilters: List<String>) {
        statesLayout.removeAllViews()
        allFilters.forEach {
            statesLayout.addView(FilterView(context,
                    filter = it,
                    isChecked = selectedFilters.contains(it),
                    showIcon = false,
                    type = "state"), statesLayout.childCount)
        }
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

        disposable = RxBus.instance
                .getEvents(FilterView.FilterSelectionChangedEvent::class.java)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    var event = it
                    event.type.let {
                        if (it.equals("category")) {
                            presenter.toggleRemarkCategoryFilter(event.filter, event.selected)
                        } else {
                            presenter.toggleRemarkStatusFilter(event.filter, event.selected)
                        }
                    }
                })
    }

    fun setOnFilterListener(filterListener: OnFilter) {
        this.filterListener = filterListener
    }

    override fun onStop() {
        super.onStop()

        if (disposable != null && !disposable.isDisposed) {
            disposable.dispose()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.destroy()
    }
}
