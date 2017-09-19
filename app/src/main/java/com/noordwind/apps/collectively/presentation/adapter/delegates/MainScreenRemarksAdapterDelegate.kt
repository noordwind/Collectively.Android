package com.noordwind.apps.collectively.presentation.adapter.delegates

import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.hannesdorfmann.adapterdelegates.AbsAdapterDelegate
import com.noordwind.apps.collectively.Constants
import com.noordwind.apps.collectively.R
import com.noordwind.apps.collectively.data.model.Remark
import com.noordwind.apps.collectively.presentation.adapter.MainScreenRemarksListAdapter
import com.noordwind.apps.collectively.presentation.extension.formatDistance
import com.noordwind.apps.collectively.presentation.extension.iconOfCategory
import com.noordwind.apps.collectively.presentation.extension.uppercaseFirstLetter
import com.noordwind.apps.collectively.presentation.main.RemarkIconBackgroundResolver


class MainScreenRemarksAdapterDelegate(viewType: Int, val onRemarkSelectedListener: MainScreenRemarksListAdapter.OnRemarkSelectedListener) : AbsAdapterDelegate<List<Any>>(viewType) {


    override fun isForViewType(items: List<Any>, position: Int): Boolean {
        return items[position] is Remark && !((items[position] as Remark).hasMediumPhoto())
    }

    override fun onBindViewHolder(items: List<Any>, position: Int, holder: RecyclerView.ViewHolder) {
        var remark = items[position] as Remark
        (holder as RemarkRowHolder).setRemark(remark)
    }

    override fun onCreateViewHolder(parent: ViewGroup?): RecyclerView.ViewHolder {
        var view = LayoutInflater.from(parent?.context).inflate(R.layout.view_remark_row, parent, false)
        return RemarkRowHolder(view, onRemarkSelectedListener)
    }

    class RemarkRowHolder(itemView: View, val onRemarkSelectedListener: MainScreenRemarksListAdapter.OnRemarkSelectedListener) : RecyclerView.ViewHolder(itemView) {

        private var categoryIcon: ImageView = itemView.findViewById(R.id.category_icon) as ImageView
        private var nameLabel: TextView = itemView.findViewById(R.id.nameLabel) as TextView
        private var distanceLabel: TextView = itemView.findViewById(R.id.distanceLabel) as TextView
        private var addressLabel: TextView = itemView.findViewById(R.id.address) as TextView
        private var remarkIconBackgroundImage: ImageView = itemView.findViewById(R.id.remarkIconBackground) as ImageView
        private var votesCountLabel: TextView = itemView.findViewById(R.id.votesCountLabel) as TextView
        private var groupLabel: TextView = itemView.findViewById(R.id.groupLabel) as TextView
        private var remark: Remark? = null
        private val remarkIconBackgroundResolver = RemarkIconBackgroundResolver()

        init {
            itemView.setOnClickListener { onRemarkSelectedListener.onRemarkItemSelected(remark!!) }
        }

        fun setRemark(remark: Remark) {
            this.remark = remark
            categoryIcon.setImageDrawable(ContextCompat.getDrawable(itemView.context, remark.category?.name?.iconOfCategory()!!))

            if (remark.category!!.name.equals(Constants.RemarkCategories.PRAISE, true) || remark.category!!.name.equals(Constants.RemarkCategories.SUGGESTION, true)) {
                nameLabel.text = Html.fromHtml(itemView.context.getString(R.string.remark_preview_photo_title_2, remark.category?.translation?.uppercaseFirstLetter(), remark.author?.name))
            } else {
                nameLabel.text = Html.fromHtml(itemView.context.getString(R.string.remark_preview_photo_title, remark.category?.translation?.uppercaseFirstLetter(), remark.author?.name))
            }

            remarkIconBackgroundImage.setImageResource(remarkIconBackgroundResolver.iconBackgroundForRemark(remark))

            distanceLabel.visibility = View.GONE
            remark.distanceToRemark?.let {
                distanceLabel.visibility = View.VISIBLE
                distanceLabel.text = Html.fromHtml(itemView.context.getString(R.string.remark_distance_label, remark.distanceToRemark!!.formatDistance()))
            }

            var positiveVotes = remark.positiveVotesCount
            var negativeVotes = remark.negativeVotesCount

            if (positiveVotes - negativeVotes >= 0) {
                votesCountLabel.text = "+" + (positiveVotes - negativeVotes)
                votesCountLabel.setTextColor(ContextCompat.getColor(itemView.context, R.color.vote_up_remark_color))
            } else {
                votesCountLabel.text = "" + (positiveVotes - negativeVotes)
                votesCountLabel.setTextColor(ContextCompat.getColor(itemView.context, R.color.vote_down_remark_color))
            }

            addressLabel.text = remark.location?.address

            if (remark.group != null) {
                groupLabel.visibility = View.VISIBLE
                groupLabel.text = Html.fromHtml(itemView.context.getString(R.string.remark_group_label, remark.group.name))
            } else {
                groupLabel.visibility = View.GONE
            }
        }
    }

}

