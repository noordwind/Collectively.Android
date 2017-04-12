package pl.adriankremski.collectively.presentation

import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import pl.adriankremski.collectively.domain.thread.PostExecutionThread
import javax.inject.Singleton

@Singleton
class UIThread : PostExecutionThread {

    override val scheduler: Scheduler
        get() = AndroidSchedulers.mainThread()
}
