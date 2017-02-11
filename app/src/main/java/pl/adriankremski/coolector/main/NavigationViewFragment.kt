package pl.adriankremski.coolector.main

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import pl.adriankremski.coolector.R
import pl.adriankremski.coolector.adapter.MainScreenRemarksListAdapter
import pl.adriankremski.coolector.model.Remark

class NavigationViewFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val layout = inflater?.inflate(R.layout.fragment_view_navigation, container, false)
        recyclerView = layout?.findViewById(R.id.recycler) as RecyclerView
        return layout
    }

    fun setRemarks(remarks: List<Remark>) {
        var adapter = MainScreenRemarksListAdapter()
        adapter.setData(remarks)
        adapter.initDelegates()
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(context)
        adapter.notifyDataSetChanged()
    }
}
