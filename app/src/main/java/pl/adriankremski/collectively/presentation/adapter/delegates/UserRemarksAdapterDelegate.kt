package pl.adriankremski.collectively.presentation.adapter.delegates

import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.hannesdorfmann.adapterdelegates.AbsAdapterDelegate
import pl.adriankremski.collectively.R
import pl.adriankremski.collectively.data.model.Remark
import pl.adriankremski.collectively.presentation.extension.iconOfCategory
import pl.adriankremski.collectively.presentation.extension.uppercaseFirstLetter
import pl.adriankremski.collectively.presentation.remarkpreview.RemarkActivity


class UserRemarksAdapterDelegate(viewType: Int) : AbsAdapterDelegate<List<Any>>(viewType) {

    override fun isForViewType(items: List<Any>, position: Int): Boolean {
        return items[position] is Remark
    }

    override fun onBindViewHolder(items: List<Any>, position: Int, holder: RecyclerView.ViewHolder) {
        var remark = items[position] as Remark
        (holder as RemarkRowHolder).setRemark(remark)
    }

    override fun onCreateViewHolder(parent: ViewGroup?): RecyclerView.ViewHolder {
        var view = LayoutInflater.from(parent?.context).inflate(R.layout.view_user_remark_row, parent, false)
        return RemarkRowHolder(view)
    }


    class RemarkRowHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private var categoryIcon: ImageView = itemView.findViewById(R.id.category_icon) as ImageView
        private var nameLabel: TextView = itemView.findViewById(R.id.name) as TextView
        private var addressLabel: TextView = itemView.findViewById(R.id.address) as TextView
        private var statusLabel: TextView = itemView.findViewById(R.id.statusLabel) as TextView
        private var remark: Remark? = null

        fun setRemark(remark: Remark) {
            this.remark = remark
            itemView.setOnClickListener { RemarkActivity.start(itemView.context, remark.id) }
            categoryIcon.setImageDrawable(ContextCompat.getDrawable(itemView.context, remark.category?.name?.iconOfCategory()!!))
            nameLabel.text = remark.description
            addressLabel.text = remark.location?.address
            statusLabel.text = remark.state.state.uppercaseFirstLetter()
        }
    }

}

