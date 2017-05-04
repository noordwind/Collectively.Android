package pl.adriankremski.collectively.data.model


class StatisticEntry(val name: String, val remarks: StatisticRemarksCount) {
    fun reportedCount(): Long = remarks.reportedCount
    fun resolvedCount(): Long = remarks.reportedCount
    fun deletedCount(): Long = remarks.deletedCount
}


class StatisticRemarksCount(val reportedCount: Long, val resolvedCount: Long, val deletedCount: Long)
