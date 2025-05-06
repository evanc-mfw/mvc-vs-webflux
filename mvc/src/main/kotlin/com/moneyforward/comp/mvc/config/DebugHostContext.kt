package com.moneyforward.comp.mvc.config

object DebugHostContext {
    val threadLocal = ThreadLocal<String>()

    var host
        get() = threadLocal.get()
        set(value) {
            threadLocal.set(value)
        }
}