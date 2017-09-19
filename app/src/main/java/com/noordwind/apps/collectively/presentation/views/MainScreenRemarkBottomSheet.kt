package com.noordwind.apps.collectively.presentation.views

import android.content.Context
import android.location.Location
import android.support.v4.content.ContextCompat
import android.text.Html
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import com.noordwind.apps.collectively.Constants
import com.noordwind.apps.collectively.R
import com.noordwind.apps.collectively.data.model.Remark
import com.noordwind.apps.collectively.presentation.extension.formatDistance
import com.noordwind.apps.collectively.presentation.extension.iconOfCategory
import com.noordwind.apps.collectively.presentation.extension.uppercaseFirstLetter
import com.noordwind.apps.collectively.presentation.main.RemarkIconBackgroundResolver
import com.noordwind.apps.collectively.presentation.remarkpreview.RemarkActivity

class MainScreenRemarkBottomSheet(context: Context, remark: Remark, lastLocation: Location?, remarkLocation: Location) : FrameLayout(context) {

    init {
        View.inflate(getContext(), R.layout.view_remark_row, this)

        var categoryIcon: ImageView = findViewById(R.id.category_icon) as ImageView
        var nameLabel: TextView = findViewById(R.id.nameLabel) as TextView
        var distanceLabel: TextView = findViewById(R.id.distanceLabel) as TextView
        var addressLabel: TextView = findViewById(R.id.address) as TextView
        var remarkIconBackgroundImage: ImageView = findViewById(R.id.remarkIconBackground) as ImageView
        var groupLabel: TextView = findViewById(R.id.groupLabel) as TextView
        var votesCountLabel: TextView = findViewById(R.id.votesCountLabel) as TextView
        val remarkIconBackgroundResolver = RemarkIconBackgroundResolver()

        categoryIcon.setImageDrawable(ContextCompat.getDrawable(context, remark.category?.name?.iconOfCategory()!!))

        if (remark.category!!.name.equals(Constants.RemarkCategories.PRAISE, true) || remark.category!!.name.equals(Constants.RemarkCategories.SUGGESTION, true)) {
            nameLabel.text = Html.fromHtml(context.getString(R.string.remark_preview_photo_title_2, remark.category?.translation?.uppercaseFirstLetter(), remark.author?.name))
        } else {
            nameLabel.text = Html.fromHtml(context.getString(R.string.remark_preview_photo_title, remark.category?.translation?.uppercaseFirstLetter(), remark.author?.name))
        }

        remarkIconBackgroundImage.setImageResource(remarkIconBackgroundResolver.iconBackgroundForRemark(remark))

        distanceLabel.visibility = View.GONE
        remark.distanceToRemark?.let {
            distanceLabel.visibility = View.VISIBLE
            distanceLabel.text = Html.fromHtml(context.getString(R.string.remark_distance_label, remark.distanceToRemark!!.formatDistance()))
        }

        var positiveVotes = remark.positiveVotesCount
        var negativeVotes = remark.negativeVotesCount

        if (positiveVotes - negativeVotes >= 0) {
            votesCountLabel.text = "+" + (positiveVotes - negativeVotes)
            votesCountLabel.setTextColor(ContextCompat.getColor(context, R.color.vote_up_remark_color))
        } else {
            votesCountLabel.text = "" + (positiveVotes - negativeVotes)
            votesCountLabel.setTextColor(ContextCompat.getColor(context, R.color.vote_down_remark_color))
        }

        addressLabel.text = remark.location?.address

        if (remark.group != null) {
            groupLabel.visibility = View.VISIBLE
            groupLabel.text = Html.fromHtml(context.getString(R.string.remark_group_label, remark.group.name))
        } else {
            groupLabel.visibility = View.GONE
        }

        setOnClickListener { RemarkActivity.start(context, remark.id) }
    }
}
