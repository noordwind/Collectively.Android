package pl.adriankremski.coolector.adapter.delegates

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.hannesdorfmann.adapterdelegates.AbsAdapterDelegate
import pl.adriankremski.coolector.R
import pl.adriankremski.coolector.model.Remark


class MainScreenRemarksAdapterDelegate(viewType: Int, val onRemarkSelectedListener: OnRemarkSelectedListener): AbsAdapterDelegate<List<Any>>(viewType) {

    interface OnRemarkSelectedListener {
        fun onRemarkSelected(remark: Remark)
    }

    override fun isForViewType(items: List<Any>, position: Int): Boolean {
        return items[position] is Remark
    }

    override fun onBindViewHolder(items: List<Any>, position: Int, holder: RecyclerView.ViewHolder) {
        var remark = items[position] as Remark
        (holder as RemarkRowHolder).setRemark(remark)
    }

    override fun onCreateViewHolder(parent: ViewGroup?): RecyclerView.ViewHolder {
        var view = LayoutInflater.from(parent?.context).inflate(R.layout.view_remark_row, parent, false)
        return RemarkRowHolder(view, onRemarkSelectedListener)
    }

    class RemarkRowHolder(itemView: View, val onRemarkSelectedListener: OnRemarkSelectedListener) : RecyclerView.ViewHolder(itemView) {

        private var nameLabel: TextView = itemView.findViewById(R.id.name) as TextView
        private var addressLabel: TextView = itemView.findViewById(R.id.address) as TextView
        private var remark: Remark? = null

        init {
            itemView.setOnClickListener { onRemarkSelectedListener.onRemarkSelected(remark!!) }
        }

        fun setRemark(remark: Remark) {
            this.remark = remark
            nameLabel.text = remark.description
            addressLabel.text = remark.location?.address
        }
    }

}

