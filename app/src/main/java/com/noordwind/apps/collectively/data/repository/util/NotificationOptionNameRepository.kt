package com.noordwind.apps.collectively.data.repository.util


interface NotificationOptionNameRepository {
    fun remarkCreatedOptionName(): String
    fun remarkProcessedOptionName(): String
    fun remarkResolvedOptionName(): String
    fun remarkCanceledOptionName(): String
    fun remarkRenewedOptionName(): String
    fun newPhotoAddedOptionName(): String
    fun newCommentAddedOptionName(): String
    fun remarkCreatedOptionDescription(): String
    fun remarkProcessedOptionDescription(): String
    fun remarkResolvedOptionDescription(): String
    fun remarkCanceledOptionDescription(): String
    fun remarkRenewedOptionDescription(): String
    fun newPhotoAddedOptionDescription(): String
    fun newCommentAddedOptionDescription(): String
}

