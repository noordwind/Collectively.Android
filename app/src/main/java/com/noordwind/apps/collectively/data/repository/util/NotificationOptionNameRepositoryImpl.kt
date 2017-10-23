package com.noordwind.apps.collectively.data.repository.util

import android.content.Context
import com.noordwind.apps.collectively.R
import com.noordwind.apps.collectively.domain.repository.NotificationOptionNameRepository


class NotificationOptionNameRepositoryImpl(val context : Context) : NotificationOptionNameRepository {
    override fun remarkCreatedOptionName(): String = context.getString(R.string.notification_option_name_remark_created)
    override fun remarkProcessedOptionName(): String = context.getString(R.string.notification_option_name_remark_processed)
    override fun remarkResolvedOptionName(): String = context.getString(R.string.notification_option_name_remark_resolved)
    override fun remarkCanceledOptionName(): String = context.getString(R.string.notification_option_name_remark_canceled)
    override fun remarkRenewedOptionName(): String = context.getString(R.string.notification_option_name_remark_renewed)
    override fun newPhotoAddedOptionName(): String = context.getString(R.string.notification_option_name_new_photo_added)
    override fun newCommentAddedOptionName(): String = context.getString(R.string.notification_option_name_new_comment_added)

    override fun remarkCreatedOptionDescription(): String = context.getString(R.string.notification_option_description_remark_created)
    override fun remarkProcessedOptionDescription(): String = context.getString(R.string.notification_option_description_remark_processed)
    override fun remarkResolvedOptionDescription(): String = context.getString(R.string.notification_option_description_remark_resolved)
    override fun remarkCanceledOptionDescription(): String = context.getString(R.string.notification_option_description_remark_canceled)
    override fun remarkRenewedOptionDescription(): String = context.getString(R.string.notification_option_description_remark_renewed)
    override fun newPhotoAddedOptionDescription(): String = context.getString(R.string.notification_option_description_new_photo_added)
    override fun newCommentAddedOptionDescription(): String = context.getString(R.string.notification_option_description_new_comment_added)
}

