package com.noordwind.apps.collectively.presentation

import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import com.noordwind.apps.collectively.domain.thread.PostExecutionThread
import javax.inject.Singleton

@Singleton
class UIThread : PostExecutionThread {

    override val scheduler: Scheduler
        get() = AndroidSchedulers.mainThread()
}
