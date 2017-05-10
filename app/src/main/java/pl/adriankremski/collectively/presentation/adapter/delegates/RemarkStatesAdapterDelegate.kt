package pl.adriankremski.collectively.presentation.adapter.delegates

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.hannesdorfmann.adapterdelegates.AbsAdapterDelegate
import pl.adriankremski.collectively.R
import pl.adriankremski.collectively.data.model.RemarkState
import pl.adriankremski.collectively.presentation.views.remark.comment.RemarkStateRowHolder

class RemarkStatesAdapterDelegate(viewType: Int): AbsAdapterDelegate<List<Any>>(viewType) {

    override fun isForViewType(items: List<Any>, position: Int): Boolean {
        return items[position] is RemarkState
    }

    override fun onBindViewHolder(items: List<Any>, position: Int, holder: RecyclerView.ViewHolder) {
        var remarkState = items[position] as RemarkState
        (holder as RemarkStateRowHolder).setRemarkState(remarkState)
    }

    override fun onCreateViewHolder(parent: ViewGroup?): RecyclerView.ViewHolder {
        var view = LayoutInflater.from(parent?.context).inflate(R.layout.view_remark_state_row_holder, parent, false)
        return RemarkStateRowHolder(view)
    }
}

