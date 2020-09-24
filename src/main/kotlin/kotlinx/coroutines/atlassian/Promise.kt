package kotlinx.coroutines.atlassian

import com.google.common.util.concurrent.Uninterruptibles
import io.atlassian.util.concurrent.Promise
import kotlinx.coroutines.suspendCancellableCoroutine
import java.util.concurrent.ExecutionException
import kotlin.coroutines.resumeWithException

suspend fun <T> Promise<T>.await(): T {
    try {
        if (isDone) return Uninterruptibles.getUninterruptibly(this)
    } catch (e: ExecutionException) {
        throw e.nonNullCause()
    }

    return suspendCancellableCoroutine { cont ->

        fail {
            cont.resumeWithException(it)
        }

        done {
            cont.resumeWith(Result.success(it))
        }

        cont.invokeOnCancellation {
            cancel(false)
        }
    }
}

private fun ExecutionException.nonNullCause(): Throwable {
    return this.cause!!
}