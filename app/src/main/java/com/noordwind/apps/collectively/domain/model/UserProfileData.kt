package com.noordwind.apps.collectively.domain.model

import com.noordwind.apps.collectively.data.model.Remark


data class UserProfileData(val name: String,
                           val userId: String,
                           val avatarUrl: String?,
                           val resolvedRemarks: List<Remark>,
                           val reportedRemarks: List<Remark>)
