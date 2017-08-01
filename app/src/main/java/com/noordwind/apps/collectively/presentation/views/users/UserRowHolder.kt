package com.noordwind.apps.collectively.presentation.views.remark.states

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView
import com.noordwind.apps.collectively.R
import com.noordwind.apps.collectively.data.model.User
import com.noordwind.apps.collectively.presentation.profile.ProfileActivity

class UserRowHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
    private var nameLabel: TextView = itemView.findViewById(R.id.nameLabel) as TextView

    fun setUser(user: User) {
        nameLabel.text = user.name
        itemView.setOnClickListener { ProfileActivity.start(itemView.context, user) }
    }
}
