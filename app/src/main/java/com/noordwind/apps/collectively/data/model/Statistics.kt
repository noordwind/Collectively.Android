package com.noordwind.apps.collectively.data.model

import java.io.Serializable

class Statistics(val categoryStatistics: List<StatisticEntry>,
                 val tagStatistics: List<StatisticEntry>,
                 val usersStatistics: List<UserStatisticsEntry>) : Serializable

