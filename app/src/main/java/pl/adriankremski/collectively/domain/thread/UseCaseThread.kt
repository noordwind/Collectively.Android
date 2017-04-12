package pl.adriankremski.collectively.domain.thread

import io.reactivex.Scheduler

interface UseCaseThread {
    val scheduler: Scheduler
}
