package pl.adriankremski.collectively.presentation

import io.reactivex.Scheduler
import io.reactivex.schedulers.Schedulers
import pl.adriankremski.collectively.domain.thread.UseCaseThread
import javax.inject.Singleton

@Singleton
class IOThread : UseCaseThread {

    override val scheduler: Scheduler
        get() = Schedulers.io()
}
