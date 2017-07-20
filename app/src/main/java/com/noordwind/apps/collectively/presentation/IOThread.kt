package com.noordwind.apps.collectively.presentation

import io.reactivex.Scheduler
import io.reactivex.schedulers.Schedulers
import com.noordwind.apps.collectively.domain.thread.UseCaseThread
import javax.inject.Singleton

@Singleton
class IOThread : UseCaseThread {

    override val scheduler: Scheduler
        get() = Schedulers.io()
}
