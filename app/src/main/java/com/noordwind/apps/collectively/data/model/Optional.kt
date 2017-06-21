package com.noordwind.apps.collectively.data.model

class Optional<T> {

    private val value: T?

    private constructor() {
        this.value = null
    }

    private constructor(value: T?) {
        if (value == null) throw NullPointerException()
        this.value = value
    }

    fun get(): T? {
        return value
    }

    val isPresent: Boolean
        get() = value != null

    fun orElse(other: T): T {
        return value ?: other
    }

    override fun toString(): String {
        return if (value == null) "Optional.empty" else "Optional[$value]"
    }

    companion object {
        private val EMPTY = Optional<Any>()

        fun <T> of(value: T): Optional<T> {
            return Optional(value)
        }

        fun <T> ofNullable(value: T?): Optional<T> {
            return if (value == null) Optional.empty<T>() else of(value)
        }

        fun <T> empty(): Optional<T> {
            return EMPTY as Optional<T>
        }
    }
}
