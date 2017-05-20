package pl.adriankremski.collectively.domain.model


data class UserProfileData(val name: String, val userId: String, val avatarUrl: String?, val resolvedRemarksCount: Int, val reportedRemarksCount: Int)
