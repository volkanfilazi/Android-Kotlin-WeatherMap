package com.example.projekt1rain.Room

import android.os.Handler
import android.os.Looper
import androidx.core.util.Consumer
import androidx.core.util.Supplier
import java.util.concurrent.Executor
import java.util.concurrent.LinkedBlockingQueue
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit


class TaskRunner {
    private val threadPoolExecutor: Executor = ThreadPoolExecutor(
        5, 128, 1,
        TimeUnit.SECONDS, LinkedBlockingQueue()
    )
    private val handler = Handler(Looper.getMainLooper())
    fun <R> executeAsync(callable: Supplier<R>, callback: Consumer<R>) {
        threadPoolExecutor.execute {
            val result = callable.get()
            handler.post { callback.accept(result) }
        }
    }
}
