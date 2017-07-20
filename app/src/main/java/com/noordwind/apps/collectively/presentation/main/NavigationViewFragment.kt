package com.noordwind.apps.collectively.presentation.main

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.noordwind.apps.collectively.R
import com.noordwind.apps.collectively.data.model.Remark
import com.noordwind.apps.collectively.presentation.adapter.MainScreenRemarksListAdapter
import com.noordwind.apps.collectively.presentation.adapter.delegates.MainScreenRemarksAdapterDelegate

class NavigationViewFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView

    var onRemarkSelectedListener: MainScreenRemarksAdapterDelegate.OnRemarkSelectedListener? = null

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val layout = inflater?.inflate(R.layout.fragment_view_navigation, container, false)
        recyclerView = layout?.findViewById(R.id.recycler) as RecyclerView
        recyclerView.setPadding(0, getStatusBarHeight(), 0, 0)
        return layout
    }

    fun getStatusBarHeight(): Int {
        var result = 0
        val resourceId = resources.getIdentifier("status_bar_height", "dimen", "android")
        if (resourceId > 0) {
            result = resources.getDimensionPixelSize(resourceId)
        }
        return result
    }

    fun setRemarks(remarks: List<Remark>) {
        var adapter = MainScreenRemarksListAdapter(onRemarkSelectedListener!!)
        adapter.setData(remarks)
        adapter.initDelegates()
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(context)
        adapter.notifyDataSetChanged()
    }
}
