package com.noordwind.apps.collectively.presentation.views.remark.states

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.noordwind.apps.collectively.R
import com.noordwind.apps.collectively.data.model.User
import com.noordwind.apps.collectively.presentation.profile.ProfileActivity
import de.hdodenhof.circleimageview.CircleImageView

class UserRowHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
    private var nameLabel: TextView = itemView.findViewById(R.id.nameLabel) as TextView
    private var userImage: ImageView = itemView.findViewById(R.id.userImage) as CircleImageView

    fun setUser(user: User) {
        nameLabel.text = user.name
        itemView.setOnClickListener { ProfileActivity.start(itemView.context, user) }

        userImage.setImageResource(R.drawable.ic_person_grey_48dp);
        if (user.avatarUrl != null) {
            Glide.with(itemView.context).load(user.avatarUrl).into(userImage)
        }
    }
}
