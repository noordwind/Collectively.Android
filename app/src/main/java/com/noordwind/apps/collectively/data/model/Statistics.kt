package com.noordwind.apps.collectively.data.model

import java.io.Serializable

class Statistics(val categoryStatistics: List<StatisticEntry>,
                 val tagStatistics: List<StatisticEntry>,
                 val usersStatistics: List<UserStatisticsEntry>) : Serializable

class StatisticEntry(val name: String, val remarks: StatisticRemarksCount) : Serializable{
    fun reportedCount(): Long = remarks.reportedCount
    fun resolvedCount(): Long = remarks.resolvedCount
    fun deletedCount(): Long = remarks.deletedCount
}

class UserStatisticsEntry(val userId : String, val name: String, val avatarUrl: String?, val remarks: StatisticRemarksCount) : Serializable {
    fun reportedCount(): Long = remarks.reportedCount
    fun resolvedCount(): Long = remarks.resolvedCount
    fun deletedCount(): Long = remarks.deletedCount
}

class StatisticRemarksCount(val reportedCount: Long, val resolvedCount: Long, val deletedCount: Long) : Serializable

