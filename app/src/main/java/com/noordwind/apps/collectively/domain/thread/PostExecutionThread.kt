package com.noordwind.apps.collectively.domain.thread

import io.reactivex.Scheduler

interface PostExecutionThread {
    val scheduler: Scheduler
}
